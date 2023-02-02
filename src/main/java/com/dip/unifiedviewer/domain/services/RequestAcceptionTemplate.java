package com.dip.unifiedviewer.domain.services;

import org.json.JSONObject;
import org.springframework.scheduling.annotation.Async;

import com.dip.unifiedviewer.domain.model.responses.UnifiedResponseNotificationMsg;

public abstract class RequestAcceptionTemplate {
	protected abstract void removeListener(String channelName);
	protected abstract UnifiedResponseNotificationMsg updateRequestInfo(String channelName, JSONObject response);
	protected abstract void saveUnifiedViewSearchLog(JSONObject responseNotificationInfo);
	protected abstract void publishResponseNotification(UnifiedResponseNotificationMsg responseNotificationMsg);

    @Async("threadPoolTaskExecutor")
	public final void initiateUnifiedViewAcceptionNotificationRequest(JSONObject response) {
    	String requestKey = response.getString("publishedChannelName");
//    	removeListener(channelName);
    	UnifiedResponseNotificationMsg responseNotificationMsg = updateRequestInfo(requestKey, response);
    	publishResponseNotification(responseNotificationMsg);
	}
}
