package com.dip.unifiedviewer.domain.services.impls;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.dip.unifiedviewer.constansts.RedisConstants;
import com.dip.unifiedviewer.domain.aspects.RedisLock;
import com.dip.unifiedviewer.domain.factory.PersistencePortFactory;
import com.dip.unifiedviewer.domain.model.requests.BaseDataSourceRequestModel;
import com.dip.unifiedviewer.domain.model.responses.UnifiedResponseNotificationMsg;
import com.dip.unifiedviewer.domain.services.AuthenticationService;
import com.dip.unifiedviewer.domain.services.RequestInitiatorTemplate;
import com.dip.unifiedviewer.gateway.services.QueryServiceProGatewayService;
import com.dip.unifiedviewer.redis.publisher.services.MessagePublisher;
import com.dip.unifiedviewer.storage.redis.datahandler.RedisDataHandler;
import com.dip.unifiedviewer.storage.redis.service.RedisListenerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RequestInitiatorImpl extends RequestInitiatorTemplate {

	private static final Logger logger = LoggerFactory.getLogger(RequestInitiatorImpl.class);
	private final RedisDataHandler redisDataHandler;
	private final RedisMessageListenerContainer redisMessageListenerContainer;
	private final MessageListenerAdapter messageListenerAdapter;
	private final RedisListenerService redisListenerService;
	private final TaskExecutor executor;
	private final ObjectMapper jsonMapper;
	private final MessagePublisher messagePublisher;
	private final QueryServiceProGatewayService queryServiceProGatewayService;

	public RequestInitiatorImpl(RedisDataHandler redisDataHandler,
			RedisMessageListenerContainer redisMessageListenerContainer, MessageListenerAdapter messageListenerAdapter,
			RedisListenerService redisListenerService, @Qualifier("threadPoolTaskExecutor") TaskExecutor executor,
			ObjectMapper jsonMapper, MessagePublisher messagePublisher,
			QueryServiceProGatewayService queryServiceProGatewayService) {
		this.redisDataHandler = redisDataHandler;
		this.redisMessageListenerContainer = redisMessageListenerContainer;
		this.messageListenerAdapter = messageListenerAdapter;
		this.redisListenerService = redisListenerService;
		this.executor = executor;
		this.jsonMapper = jsonMapper;
		this.messagePublisher = messagePublisher;
		this.queryServiceProGatewayService = queryServiceProGatewayService;
	}

	@Override
	protected UnifiedResponseNotificationMsg generateRequestInfo(BaseDataSourceRequestModel dataSourceRequestBodyModel) {
		String unifiedNotificationStr = "";
//		JSONObject request = requestBody.getJSONObject("requestBody");

		UnifiedResponseNotificationMsg unifiedNotification = UnifiedResponseNotificationMsg.builder()
				.currentFailureCount(0)
				.currentSuccessCount(0)
				.id(dataSourceRequestBodyModel.getId())
				.totalCount(dataSourceRequestBodyModel.getTotalCount())
				.recievedCount(0)
				.channleToPublish(dataSourceRequestBodyModel.getChannelToPublish())
				.searchIdentifier(dataSourceRequestBodyModel.getSearchIdentifier())
//				.searchIdentifier(dataSourceRequestBodyModel.getSearchIdentifier())
				.build();
		return unifiedNotification;
	}

	@Override
	protected String addLockKeyForRequestInitiator(BaseDataSourceRequestModel dataSourceRequestBodyModel) {
		String initiatorLockKey = RedisConstants.REDIS_UNIFIED_REQUEST_KEY_PREFIX
				+ dataSourceRequestBodyModel.getRequestBody().get("searchIdentifier").toString() + "--"
				+ dataSourceRequestBodyModel.getId().toString();

		redisDataHandler.addOrUpdateRequestInfoObject(initiatorLockKey, "{}");

		return initiatorLockKey;
	}

	@RedisLock(keyPrefix = RedisConstants.REDIS_UNIFIED_REQUEST_KEY_PREFIX)
	@Override
	protected String insertNewRequestInfo(String requestInitiatorLockKey, String requestKey,
			UnifiedResponseNotificationMsg unifiedResponseNotification) {
		String requestInfoObject = redisDataHandler.getRequestInfoObject(requestKey);
		String unifiedResponseInfo = "{}";

		if (Objects.equals(requestInfoObject, null)) {
			try {
				addRedisListener(requestKey, null);
				unifiedResponseInfo = jsonMapper.writeValueAsString(unifiedResponseNotification);
				redisDataHandler.addOrUpdateRequestInfoObject(requestKey, unifiedResponseInfo);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		}
		return unifiedResponseInfo;
	}

	@RedisLock(keyPrefix = RedisConstants.REDIS_UNIFIED_REQUEST_KEY_PREFIX)
	@Override
	protected String updateRequestRequestInfo(String requestKey, BaseDataSourceRequestModel dataSourceRequestBodyModel,
			UnifiedResponseNotificationMsg unifiedResponseNotification) {
		String requestInfoObjectStr = redisDataHandler.getRequestInfoObject(requestKey);

		if (Objects.equals(requestInfoObjectStr, null)) {

			try {
				requestInfoObjectStr = jsonMapper.writeValueAsString(unifiedResponseNotification);
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			addRedisListener(requestKey, dataSourceRequestBodyModel);
			redisDataHandler.addOrUpdateRequestInfoObject(requestKey, requestInfoObjectStr);
		} else {
			try {
				requestInfoObjectStr = jsonMapper.writeValueAsString(unifiedResponseNotification);

				redisDataHandler.addOrUpdateRequestInfoObject(requestKey, requestInfoObjectStr);
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		redisDataHandler.addOrUpdateRequestInfoObject(requestKey, requestInfoObjectStr);
		return requestInfoObjectStr;
	}

//	@Override
	private void addRedisListener(String channelName, BaseDataSourceRequestModel dataSourceRequestBodyModel) {
		boolean listenerExists = redisDataHandler.containsKey(channelName);

		if (!listenerExists) {
			boolean isSubscriptionSuccessful = redisListenerService
					.addListenerByChannelName(redisMessageListenerContainer, messageListenerAdapter, channelName)
					.exceptionally(ex -> {
						return false;
					}).join();
		}
	}

	@Override
	protected void publishInitiateNotificationRequest(String channelName, String unifiedResponseNotification) {
		try {
		channelName = "redisChannel";
		messagePublisher.publish(channelName, unifiedResponseNotification);
		} catch (Exception e) {
			log.error("Cant publish notification message in channel: " + channelName, e);
		}
	}

	@Override
	protected void initiateRequest(String channelName, BaseDataSourceRequestModel dataSourceRequestBodyModel) {
		// For demo purpose only:
//		channelName = "redisChannel_for_unified_view_test";
		Map<String, Object> requestBody = dataSourceRequestBodyModel.getRequestBody();
		JSONObject requestBodyJson = new JSONObject(requestBody);

		requestBodyJson.getJSONArray("channels").put(channelName);
		Object type = requestBody.get("type");

		queryServiceProGatewayService.postRequest(requestBodyJson.toString(), type.toString());
	}

}
