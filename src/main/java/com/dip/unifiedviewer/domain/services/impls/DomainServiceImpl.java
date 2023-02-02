//package com.dip.unifiedviewer.domain.services.impls;
//
//import com.dip.unifiedviewer.constansts.PublicApiWorkerTaskType;
//import com.dip.unifiedviewer.constansts.Status;
//import com.dip.unifiedviewer.domain.factory.MasterTreeFactory;
//import com.dip.unifiedviewer.domain.factory.PersistencePortFactory;
//import com.dip.unifiedviewer.domain.model.JsonBuilder;
//import com.dip.unifiedviewer.domain.model.QueryJsonBuilder;
//import com.dip.unifiedviewer.domain.model.StatusJsonBuilder;
//import com.dip.unifiedviewer.domain.model.TreeNode;
//import com.dip.unifiedviewer.domain.model.requests.BaseRequestBodyModel;
//import com.dip.unifiedviewer.domain.model.responses.CheckStatusResponseBodyModel;
//import com.dip.unifiedviewer.domain.model.responses.InitiateRequestResponseBodyModel;
//import com.dip.unifiedviewer.domain.persistent.services.DomainPersistenceServicePort;
//import com.dip.unifiedviewer.domain.services.AuthenticationService;
//import com.dip.unifiedviewer.domain.services.DomainService;
//import com.dip.unifiedviewer.storage.redis.datahandler.RedisDataHandler;
//import com.dip.unifiedviewer.storage.redis.service.RedisListenerService;
//
//import org.json.JSONArray;
//import org.json.JSONObject;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.core.task.TaskExecutor;
//import org.springframework.data.redis.listener.RedisMessageListenerContainer;
//import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Service;
//
//import java.util.Objects;
//import java.util.Optional;
//import java.util.Set;
//import java.util.concurrent.CompletableFuture;
//
//import static com.dip.unifiedviewer.constansts.JsonConstants.*;
//import static com.dip.unifiedviewer.util.JSONUtil.getErrorJson;
//import static com.dip.unifiedviewer.util.JSONUtil.getInitialDataJson;
//
//@Service
//public class DomainServiceImpl implements DomainService {
//
//	private static final Logger logger = LoggerFactory.getLogger(DomainServiceImpl.class);
//
//	private final PersistencePortFactory persistencePortFactory;
//	private final MasterTreeFactory masterTreeFactory;
//	private final RedisDataHandler redisDataHandler;
//	private final RedisMessageListenerContainer redisMessageListenerContainer;
//	private final MessageListenerAdapter messageListenerAdapter;
//	private final RedisListenerService redisListenerService;
//	private final AuthenticationService authenticationService;
//	private final TaskExecutor executor;
//	private final ResponseUpdaterImpl responseUpdater;
//
//	public DomainServiceImpl(PersistencePortFactory persistencePortFactory, MasterTreeFactory masterTreeFactory,
//							 RedisDataHandler redisDataHandler, RedisMessageListenerContainer redisMessageListenerContainer,
//							 MessageListenerAdapter messageListenerAdapter, RedisListenerService redisListenerService,
//							 AuthenticationService authenticationService, @Qualifier("threadPoolTaskExecutor") TaskExecutor executor, ResponseUpdaterImpl responseUpdater) {
//		this.persistencePortFactory = persistencePortFactory;
//		this.masterTreeFactory = masterTreeFactory;
//		this.redisDataHandler = redisDataHandler;
//		this.redisMessageListenerContainer = redisMessageListenerContainer;
//		this.messageListenerAdapter = messageListenerAdapter;
//		this.redisListenerService = redisListenerService;
//		this.authenticationService = authenticationService;
//		this.executor = executor;
//		this.responseUpdater = responseUpdater;
//	}
//
//	@Override
//	public InitiateRequestResponseBodyModel executeRequest(BaseRequestBodyModel bodyModel) {
//		String accessToken = authenticationService.authenticateUser(bodyModel);
//
//		PublicApiWorkerTaskType taskType = bodyModel.getRule();
//
//		// Need to update it synchronously so request id is available as soon as
//		// response is returned returned
//		redisDataHandler.updateFetchability(bodyModel.getRequestId(), JSON_FETCHABILITY_VALUE_NOT_READY);
//
//		boolean isSubscriptionSuccessful = redisListenerService.addListenerByChannelName(redisMessageListenerContainer,
//				messageListenerAdapter, bodyModel.getRequestId()).exceptionally(ex -> {
//					logger.error("Error occured while waiting for data for request id: " + bodyModel.getRequestId()
//							+ " for agency: " + bodyModel.getAgencyId() + " for userId: " + bodyModel.getUserId()
//							+ " for search: " + bodyModel.getRequestedFor(), ex);
//					return false;
//				}).join();
//
//		logger.info("Subscription to: " + bodyModel.getRequestId() + " is " + (isSubscriptionSuccessful ? "" : "not")
//				+ " successful for: " + bodyModel.getRequestId() + " for request: " + bodyModel);
//
//		if (isSubscriptionSuccessful)
//			CompletableFuture.runAsync(() -> {
//				initiateRequest(bodyModel, taskType);
//			}, executor).exceptionally(ex -> {
//				logger.error("Problem occured while initiating request for: " + bodyModel.getRequestId()
//						+ "For request body model: " + bodyModel, ex);
//				return null;
//			});
//
//		return new InitiateRequestResponseBodyModel(taskType.getValue(), accessToken,
//				isSubscriptionSuccessful ? bodyModel.getRequestId() : null);
//	}
//
//	private void initiateRequest(BaseRequestBodyModel bodyModel, PublicApiWorkerTaskType taskType) {
//		logger.info("subscribed to redis listener for request id: " + bodyModel.getRequestId());
//		authenticationService.adjustParameters(bodyModel);
//		JSONArray queryJson = getQueryJson(taskType, bodyModel.getRequestedFor());
//		DomainPersistenceServicePort portImpl = persistencePortFactory.getPortImpl(taskType);
//		updateRedisForRequest(bodyModel);
//		portImpl.executeRequest(bodyModel, queryJson);
//
//	}
//
//	@Override
//	public CheckStatusResponseBodyModel executeStatusCheckRequest(BaseRequestBodyModel bodyModel) {
//		return constructStatusCheckRequestResponseWithRequestId(bodyModel.getRequestId());
//	}
//
//	@Override
//	public Optional<String> executeDataFetchRequest(BaseRequestBodyModel bodyModel) {
//		return Optional.ofNullable(constructFetchDataRequestResponseWithRequestId(bodyModel.getRequestId()));
//	}
//
//	private void updateRedisForRequest(BaseRequestBodyModel bodyModel) {
//		String requestId = bodyModel.getRequestId();
//		logger.info("Writing initial data on redis for request id: " + requestId);
//		PublicApiWorkerTaskType taskType = bodyModel.getRule();
//		JSONArray statusJson = getStatusJson(taskType, bodyModel.getRequestedFor());
//		logger.info(statusJson.toString());
//
//		JSONObject initialData = prepareInitialData(bodyModel.getRule().getValue(), bodyModel.getRequestedFor(),
//				bodyModel.getLimitExceededList());
//		redisDataHandler.updateDataList(requestId, initialData);
//		redisDataHandler.updateRequestedWith(requestId, taskType.getValue());
//		redisDataHandler.updateRequestedFor(requestId, bodyModel.getRequestedFor());
//		redisDataHandler.updateStatusJson(requestId, statusJson);
//
//		logger.info("Written initial data to redis for request id: " + requestId);
//	}
//
//	private JSONObject prepareInitialData(String rule, Set<String> availableList, Set<String> exceededList) {
//		JSONObject jsonObject = new JSONObject();
//		for (String type : availableList) {
//			jsonObject.put(PublicApiWorkerTaskType.valueOf(type).getKey(), getInitialDataJson());
//		}
//
//		for (String type : exceededList) {
//			String errorMessage = "Limit exceeded for " + type + " by " + rule;
//			jsonObject.put(PublicApiWorkerTaskType.valueOf(type).getKey(),
//					getErrorJson(errorMessage, HttpStatus.TOO_MANY_REQUESTS));
//		}
//
//		return jsonObject;
//	}
//
//	private boolean traverseTree(TreeNode node, Set<String> requestedFor, JsonBuilder builder) {
//		boolean used = requestedFor.contains(node.getType().getValue());
//		for (TreeNode child : node.getChildren()) {
//			used = traverseTree(child, requestedFor, builder) || used;
//		}
//		if (used) {
//			builder.addJson(node);
//		}
//		return used;
//	}
//
//	private JSONArray getStatusJson(PublicApiWorkerTaskType taskType, Set<String> requestedFor) {
//		JsonBuilder jsonBuilder = new StatusJsonBuilder();
//		traverseTree(masterTreeFactory.getTreeByType(taskType), requestedFor, jsonBuilder);
//		return jsonBuilder.build();
//	}
//
//	private JSONArray getQueryJson(PublicApiWorkerTaskType taskType, Set<String> requestedFor) {
//		JsonBuilder jsonBuilder = new QueryJsonBuilder();
//		traverseTree(masterTreeFactory.getTreeByType(taskType), requestedFor, jsonBuilder);
//		return jsonBuilder.build();
//	}
//
//	private CheckStatusResponseBodyModel constructStatusCheckRequestResponseWithRequestId(String requestId) {
//		String fetchable = redisDataHandler.getFetchability(requestId);
//		if (Objects.equals(fetchable, null)) {
//			return new CheckStatusResponseBodyModel(Status.NOT_FOUND);
//		}
//
//		if (Objects.equals(fetchable, JSON_FETCHABILITY_VALUE_READY)) {
//			return new CheckStatusResponseBodyModel(Status.READY);
//		}
//		return new CheckStatusResponseBodyModel(Status.NOT_READY);
//	}
//
//	private String constructFetchDataRequestResponseWithRequestId(String requestId) {
//		String fetchable = redisDataHandler.getFetchability(requestId);
//		if (Objects.equals(fetchable, JSON_FETCHABILITY_VALUE_READY)) {
//			JSONObject response = redisDataHandler.getDataJson(requestId);
//			JSONObject data = response.getJSONObject(JSON_KEY_DATA);
//
//			if ((data.has(JSON_KEY_ESAF) && data.has(JSON_KEY_MNP)) || data.has(JSON_KEY_ESAF)) {
//				response.put(JSON_KEY_DATA, responseUpdater.esafReponseUpdate(data));
//			}
//
//			return response.toString();
//		}
//		return "{\"requestId\": \"" + requestId + "\", \"data\": []" + "}";
//	}
//}
