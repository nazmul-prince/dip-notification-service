package com.dip.unifiedviewer.infrastructure.request_handlers;

public interface RequestHandler {
    void executeRequest(String url, String jsonPayload);
}
