package com.dip.unifiedviewer.domain.services;

import org.json.JSONObject;

import java.util.concurrent.CompletableFuture;

public interface DataUpdater {
    CompletableFuture<Boolean> updateStatus(String requestId, String responseDataType, JSONObject jsonResponse);
    void updateData(String requestId, String responseDataType, JSONObject jsonResponse);
    void updateRequestInfo(String key, JSONObject requestInfo);
}
