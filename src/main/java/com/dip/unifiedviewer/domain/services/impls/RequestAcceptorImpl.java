package com.dip.unifiedviewer.domain.services.impls;

import java.util.Objects;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.dip.unifiedviewer.constansts.RedisConstants;
import com.dip.unifiedviewer.constansts.WorkerTaskType;
import com.dip.unifiedviewer.domain.aspects.RedisLock;
import com.dip.unifiedviewer.domain.factory.PersistencePortFactory;
import com.dip.unifiedviewer.domain.model.responses.UnifiedResponseNotificationMsg;
import com.dip.unifiedviewer.domain.services.AuthenticationService;
import com.dip.unifiedviewer.domain.services.RequestAcceptionTemplate;
import com.dip.unifiedviewer.domain.services.RequestInitiatorTemplate;
import com.dip.unifiedviewer.gateway.services.QueryServiceProGatewayService;
import com.dip.unifiedviewer.redis.publisher.services.MessagePublisher;
import com.dip.unifiedviewer.storage.redis.datahandler.RedisDataHandler;
import com.dip.unifiedviewer.storage.redis.service.RedisListenerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class RequestAcceptorImpl extends RequestAcceptionTemplate {

	private static final Logger logger = LoggerFactory.getLogger(RequestAcceptorImpl.class);

	private final RedisDataHandler redisDataHandler;
	private final RedisMessageListenerContainer redisMessageListenerContainer;
	private final MessageListenerAdapter messageListenerAdapter;
	private final RedisListenerService redisListenerService;
	private final TaskExecutor executor;
	private final ObjectMapper jsonMapper;
	private final MessagePublisher messagePublisher;

	public RequestAcceptorImpl(RedisDataHandler redisDataHandler, RedisMessageListenerContainer redisMessageListenerContainer,
							 MessageListenerAdapter messageListenerAdapter, RedisListenerService redisListenerService,
							 @Qualifier("threadPoolTaskExecutor") TaskExecutor executor,
							 ObjectMapper jsonMapper,
							 MessagePublisher messagePublisher) {
		this.redisDataHandler = redisDataHandler;
		this.redisMessageListenerContainer = redisMessageListenerContainer;
		this.messageListenerAdapter = messageListenerAdapter;
		this.redisListenerService = redisListenerService;
		this.executor = executor;
		this.jsonMapper = jsonMapper;
		this.messagePublisher = messagePublisher;
	}

	@Override
	protected void removeListener(String channelName) {
		redisListenerService.removeListenerByChannelName(redisMessageListenerContainer, messageListenerAdapter, channelName);
	}

	@RedisLock(keyPrefix = RedisConstants.REDIS_UNIFIED_REQUEST_KEY_PREFIX)
	@Override
	protected UnifiedResponseNotificationMsg updateRequestInfo(String channelName, JSONObject response) {
		UnifiedResponseNotificationMsg notificationInfo = new UnifiedResponseNotificationMsg().builder().build();
		String requestInfoObjectStr = redisDataHandler.getRequestInfoObject(channelName);
		
		try {
			notificationInfo = jsonMapper.readValue(requestInfoObjectStr,
					UnifiedResponseNotificationMsg.class);
			
			int incrementedValue = getIncrementedValue(response);
			notificationInfo.setRecievedCount(notificationInfo.getRecievedCount() + incrementedValue);

			Object error = response.get("error");
			
			if(Objects.equals(error, null)) {
				notificationInfo.setCurrentSuccessCount(notificationInfo.getCurrentSuccessCount() + incrementedValue);
			} else {
				notificationInfo.setCurrentFailureCount(notificationInfo.getCurrentFailureCount() + incrementedValue);
			}
			
			if(notificationInfo.isFinished()) {
				redisListenerService.removeListenerByChannelName(redisMessageListenerContainer, messageListenerAdapter, channelName);
			} else {
				requestInfoObjectStr = jsonMapper.writeValueAsString(notificationInfo);
				redisDataHandler.addOrUpdateRequestInfoObject(channelName, requestInfoObjectStr);
			}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return notificationInfo;
	}

	@Override
	protected void saveUnifiedViewSearchLog(JSONObject responseNotificationInfo) {
	}

	@Override
	protected void publishResponseNotification(UnifiedResponseNotificationMsg responseNotificationMsg) {
		//For demo purpose only:
//		channelName = "redisChannel";
		String channelName = responseNotificationMsg.getChannleToPublish();
		String responseNotificationMsgStr = "{}";
		try {
			responseNotificationMsgStr = jsonMapper.writeValueAsString(responseNotificationMsg);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		messagePublisher.publish(channelName, responseNotificationMsgStr);
	}
	
	private int getIncrementedValue(JSONObject response) {
		try {
			String type = response.getString("type");
			Object fromCache = response.get("fromCache");
			
			if(Objects.equals(type.toLowerCase(), WorkerTaskType.ESAF.getIndex().toLowerCase()) &&
					(!Objects.equals(fromCache, null) && Objects.equals(fromCache.toString(), "1"))) {
				return 4;
			}
		} catch (Exception e) {
			logger.error("Error while getting incremented value", e);
		}
		return 1;
	}
}
