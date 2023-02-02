package com.dip.unifiedviewer.domain.services;

import java.util.Objects;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;

import com.dip.unifiedviewer.constansts.RedisConstants;
import com.dip.unifiedviewer.domain.model.requests.BaseDataSourceRequestModel;
import com.dip.unifiedviewer.domain.model.responses.UnifiedResponseNotificationMsg;

public abstract class RequestInitiatorTemplate {
	private final Logger log = LoggerFactory.getLogger(RequestInitiatorTemplate.class);

	protected abstract UnifiedResponseNotificationMsg generateRequestInfo(
			BaseDataSourceRequestModel dataSourceRequestBodyModel);

	protected abstract String addLockKeyForRequestInitiator(BaseDataSourceRequestModel dataSourceRequestBodyModel);

	protected abstract String insertNewRequestInfo(String requestInitiatorLockKey, String requestInfoKey,
			UnifiedResponseNotificationMsg unifiedResponseNotification);

	protected abstract String updateRequestRequestInfo(String requestKey,
			BaseDataSourceRequestModel dataSourceRequestBodyModel,
			UnifiedResponseNotificationMsg unifiedResponseNotification);

//	protected abstract void addRedisListener(String channelName, BaseDataSourceRequestModel dataSourceRequestBodyModel);

	protected abstract void publishInitiateNotificationRequest(String channelName, String unifiedResponseNotification);

	protected abstract void initiateRequest(String channelName, BaseDataSourceRequestModel dataSourceRequestBodyModel);

	public final void initiateUnifiedRequest(BaseDataSourceRequestModel dataSourceRequestBodyModel) {
		// for demo purpose
//		String requestKey = "redisChannel_for_unified_view_test";
		UnifiedResponseNotificationMsg unifiedResponseNotification = null;
		String unifiedNotificationRequestInfo = null;
		
		String requestKey = RedisConstants.REDIS_UNIFIED_REQUEST_KEY_PREFIX +
				dataSourceRequestBodyModel.getRequestBody().get("searchIdentifier").toString() + "_"
				+ dataSourceRequestBodyModel.getId().toString();
		unifiedResponseNotification = generateRequestInfo(dataSourceRequestBodyModel);

		try {
			String requestInitiatorLockKey = addLockKeyForRequestInitiator(dataSourceRequestBodyModel);
			unifiedNotificationRequestInfo = insertNewRequestInfo(requestInitiatorLockKey, requestKey,
					unifiedResponseNotification);
		} catch (Exception e) {
			log.error("Error while initiating request: " + dataSourceRequestBodyModel, e);
		}

		
		if(Objects.equals(unifiedNotificationRequestInfo, null)) {
			log.info("Cant generate notification message for: " + dataSourceRequestBodyModel);
			unifiedResponseNotification = generateRequestInfo(dataSourceRequestBodyModel);
		}
		publishInitiateNotificationRequest(requestKey, unifiedNotificationRequestInfo);
//		addRedisListener(requestKey, dataSourceRequestBodyModel);
		initiateRequest(requestKey, dataSourceRequestBodyModel);
	}
}
