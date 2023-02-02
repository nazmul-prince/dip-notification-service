package com.dip.unifiedviewer.storage.redis.datahandler;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.dip.unifiedviewer.storage.redis.service.DistributedLockService;

import static com.dip.unifiedviewer.constansts.ApiExposureConstants.STRING_SEPERATOR;
import static com.dip.unifiedviewer.constansts.JsonConstants.JSON_KEY_DATA;
import static com.dip.unifiedviewer.constansts.RedisConstants.*;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class RedisDataHandler {

  private final DistributedLockService redisDistributedLockService;

  public RedisDataHandler(DistributedLockService redisDistributedLockService) {
    this.redisDistributedLockService = redisDistributedLockService;
  }

  public JSONObject getDataJson(String requestId) {
    String fullData = redisDistributedLockService.getData(REDIS_KEY_DATA_PREFIX + requestId);
    return new JSONObject(Objects.equals(fullData, null)? "{}" : fullData);
  }

  public JSONObject getStatusJson(String requestId) {
    String dataRetrievalStatus =
        redisDistributedLockService.getData(REDIS_UNIFIED_REQUEST_KEY_PREFIX + requestId);
    return new JSONObject(Objects.equals(dataRetrievalStatus, null)? "{}" : dataRetrievalStatus);
  }

  public String getRequestedWith(String requestId) {
    return redisDistributedLockService.getData(REDIS_KEY_REQUESTED_WITH_PREFIX + requestId);
  }

  public List<String> getRequestedForList(String requestId) {
    String rowValue = redisDistributedLockService.getData(REDIS_KEY_REQUESTED_FOR_PREFIX + requestId);
    return Arrays.asList(rowValue.split(STRING_SEPERATOR));
  }

  public String getFetchability(String requestId) {
    return redisDistributedLockService.getData(REDIS_KEY_FETCHABLE_PREFIX + requestId);
  }

  public void updateStatusJson(String requestId, JSONArray jsonArray) {
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("requestId", requestId);
    jsonObject.put("status", jsonArray);
    redisDistributedLockService.putData(REDIS_UNIFIED_REQUEST_KEY_PREFIX + requestId, jsonObject.toString());
  }

  public void updateDataList(String requestId, JSONObject dataObject) {
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("requestId", requestId);
    jsonObject.put(JSON_KEY_DATA, dataObject);
    redisDistributedLockService.putData(REDIS_KEY_DATA_PREFIX + requestId, jsonObject.toString());
  }

  public void updateFetchability(String requestId, String value) {
    redisDistributedLockService.putData(REDIS_KEY_FETCHABLE_PREFIX + requestId, value);
  }

  public void updateRequestedWith(String requestId, String value) {
    redisDistributedLockService.putData(REDIS_KEY_REQUESTED_WITH_PREFIX + requestId, value);
  }

  public void updateRequestedFor(String requestId, Set<String> values) {
    String preparedString = String.join(STRING_SEPERATOR, values);
    redisDistributedLockService.putData(REDIS_KEY_REQUESTED_FOR_PREFIX + requestId, preparedString);
  }

  public void addOrUpdateRequestInfoObject(String key, JSONObject requestInfo) {
	  redisDistributedLockService.putData(key, requestInfo.toString());
  }
  
  public void addOrUpdateRequestInfoObject(String key, String requestInfo) {
	  redisDistributedLockService.putData(key, requestInfo);
  }
  
  public String getRequestInfoObject(String key) {
	  return redisDistributedLockService.getData(key);
  }
  
  public boolean containsKey(String key) {
	  return redisDistributedLockService.containsKey(key);
  }
}
