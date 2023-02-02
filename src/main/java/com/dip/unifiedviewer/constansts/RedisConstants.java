package com.dip.unifiedviewer.constansts;

public interface RedisConstants {

    String REDIS_MAP_KEY = "dip-unified-viewer-service";

    String REDIS_KEY_FETCHABLE_PREFIX = "public-api-response-fetchable-";
//    String REDIS_KEY_STATUS_PREFIX = "public-api-response-status-";
    String REDIS_UNIFIED_REQUEST_KEY_PREFIX = "dip-unified-viewer-";
    String REDIS_KEY_DATA_PREFIX = "public-api-response-data-";
    String REDIS_KEY_REQUESTED_WITH_PREFIX = "public-api-response-requestedWith-";
    String REDIS_KEY_REQUESTED_FOR_PREFIX = "public-api-response-requestedFor-";
}
