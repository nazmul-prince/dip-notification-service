package com.dip.unifiedviewer.domain.services.impls;

import com.dip.unifiedviewer.constansts.ApiExposureConstants;
import com.dip.unifiedviewer.constansts.PublicApiWorkerTaskType;
import com.dip.unifiedviewer.constansts.RedisConstants;
import com.dip.unifiedviewer.domain.aspects.RedisLock;
import com.dip.unifiedviewer.domain.response.processors.ResponseProcessor;
import com.dip.unifiedviewer.domain.response.processors.ResponseProcessorServiceFactory;
import com.dip.unifiedviewer.domain.services.DataUpdater;
import com.dip.unifiedviewer.domain.services.ErrorResponseGenerator;
import com.dip.unifiedviewer.storage.redis.datahandler.RedisDataHandler;
import com.dip.unifiedviewer.storage.redis.service.RedisListenerService;
import com.dip.unifiedviewer.util.JSONUtil;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import static com.dip.unifiedviewer.constansts.JsonConstants.*;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Component
public class RedisDataUpdater implements DataUpdater {

    private static final Logger logger = LoggerFactory.getLogger(RedisDataUpdater.class);
    private final RedisDataHandler redisDataHandler;
    private final RedisMessageListenerContainer redisMessageListenerContainer;
    private final MessageListenerAdapter messageListenerAdapter;
    private final RedisListenerService redisListenerService;

    private final ResponseProcessorServiceFactory responseProcessorServiceFactory;
    private final ErrorResponseGenerator errorResponseGenerator;

    public RedisDataUpdater(RedisDataHandler redisDataHandler,
                            ResponseProcessorServiceFactory responseProcessorServiceFactory,
                            ErrorResponseGenerator errorResponseGenerator,
                            @Lazy RedisMessageListenerContainer redisMessageListenerContainer,
                            @Lazy MessageListenerAdapter messageListenerAdapter,
                            @Lazy RedisListenerService redisListenerService) {
        this.redisDataHandler = redisDataHandler;
        this.redisMessageListenerContainer = redisMessageListenerContainer;
        this.messageListenerAdapter = messageListenerAdapter;
        this.redisListenerService = redisListenerService;
        this.responseProcessorServiceFactory = responseProcessorServiceFactory;
        this.errorResponseGenerator = errorResponseGenerator;
    }

    @RedisLock(keyPrefix = RedisConstants.REDIS_UNIFIED_REQUEST_KEY_PREFIX)
    @Async("threadPoolTaskExecutor")
    @Override
    public CompletableFuture<Boolean> updateStatus(String requestId, String responseDataType, JSONObject jsonResponse) {
        try {
            JSONObject statusJsonByRequestId = redisDataHandler.getStatusJson(requestId);
            logger.info("Getting status before updating status for request id: " + requestId);

            JSONArray statusArray = statusJsonByRequestId.getJSONArray("status");

            String statusJsonSearchKey = responseDataType.equals(PublicApiWorkerTaskType.LOCAL_ESAF.getValue())
                    ? PublicApiWorkerTaskType.ESAF.getValue()
                    : responseDataType;
            // select needed json object.
            JSONObject statusJsonInstance = null;
            for (int i = 0; i < statusArray.length(); i++) {
                JSONObject jsonObject = statusArray.getJSONObject(i);
                if (Objects.equals(jsonObject.getString(JSON_KEY_RESPONSE_TYPE), statusJsonSearchKey)) {
                    statusJsonInstance = jsonObject;
                    break;
                }
            }

            if (Objects.equals(statusJsonInstance, null)) {
                return CompletableFuture.completedFuture(Boolean.FALSE);
            }

            String searchedWith = redisDataHandler.getRequestedWith(requestId);

            int numOfRecordFound = 1;
            // when response is ESAF and requested with NID, need to check if it's returned
            // from cache.
            if (Objects.equals(responseDataType, PublicApiWorkerTaskType.ESAF.getValue())) {
                if (!Objects.equals(searchedWith, ApiExposureConstants.REQUEST_TYPE_MSISDN)
                        && jsonResponse.optInt(JSON_KEY_FROM_CACHE, 0) == 1) {
                    numOfRecordFound = 4;
                }
            } else if (Objects.equals(responseDataType, PublicApiWorkerTaskType.LOCAL_ESAF.getValue())) {
                if (jsonResponse.isNull(JSON_KEY_DATA))
                    return CompletableFuture.completedFuture(false);

                if (!Objects.equals(searchedWith, ApiExposureConstants.REQUEST_TYPE_MSISDN))
                    numOfRecordFound = 4;
            }

            logger.info("For request id: " + requestId + " no of records found: " + numOfRecordFound);
            statusJsonInstance.put(JSON_KEY_CURRENT_COUNT,
                    statusJsonInstance.optInt(JSON_KEY_CURRENT_COUNT, 0) + numOfRecordFound);

            redisDataHandler.updateStatusJson(requestId, statusArray);
            logger.info("Response data type: " + responseDataType + " num of record: " + numOfRecordFound + " status: "
                    + statusArray);

            if (hasGotAllResponse(statusArray)) {
                redisDataHandler.updateFetchability(requestId, JSON_FETCHABILITY_VALUE_READY);
                redisListenerService
                        .removeListenerByChannelName(redisMessageListenerContainer, messageListenerAdapter, requestId)
                        .thenAccept((voidResponse) -> logger.info("Unsubscribed from channel " + requestId))
                        .exceptionally(ex -> {
                            logger.error("Exception while unsubscribing from channel: " + requestId, ex);
                            return null;
                        });
            }

            return CompletableFuture.completedFuture(Boolean.TRUE);
        } catch (Exception e) {
            logger.error("Error while update status from redis data updater for channel: " + requestId, e);
            return CompletableFuture.completedFuture(Boolean.FALSE);
        }
    }

    private boolean hasGotAllResponse(JSONArray statusArray) {
        for (int i = 0; i < statusArray.length(); i++) {
            JSONObject status = statusArray.getJSONObject(i);
            int expectedCount = status.optInt(JSON_KEY_EXPECTED_COUNT, 0);
            int currentCount = status.optInt(JSON_KEY_CURRENT_COUNT, 0);

            if (expectedCount != currentCount)
                return false;
        }
        return true;
    }

    @RedisLock(keyPrefix = RedisConstants.REDIS_KEY_DATA_PREFIX)
    @Override
    public void updateData(String requestId, String responseDataType, JSONObject jsonResponse) {
        try {
            logger.info("Starting updating data after getting resonse for request id: " + requestId);
            List<String> requestedFor = redisDataHandler.getRequestedForList(requestId);
            PublicApiWorkerTaskType responseType = PublicApiWorkerTaskType.valueOf(responseDataType);

            // Check if user requested for the response data explicitly,
            // otherwise if the response is local esaf whereas the user requested for esaf,
            // also if the response is either esaf or local esaf and the user requested for
            // local_esaf_then_esaf.
            if (requestedFor.contains(responseDataType) || (requestedFor
                    .contains(ApiExposureConstants.REQUEST_TYPE_ESAF)
                    && (Objects.equals(responseDataType, ApiExposureConstants.REQUEST_TYPE_ESAF)
                    || Objects.equals(responseDataType, ApiExposureConstants.REQUEST_TYPE_LOCAL_ESAF)))) {

                JSONObject dataJsonByRequestId = redisDataHandler.getDataJson(requestId);
                JSONObject dataObject = dataJsonByRequestId.has(JSON_KEY_DATA)
                        ? dataJsonByRequestId.getJSONObject(JSON_KEY_DATA)
                        : new JSONObject();

                if (responseDataType.equals(ApiExposureConstants.REQUEST_TYPE_LOCAL_ESAF)) {
                    responseType = PublicApiWorkerTaskType.valueOf(PublicApiWorkerTaskType.ESAF.getValue());
                }

                if (!dataObject.has(responseType.getKey())) {
                    dataObject.put(responseType.getKey(), JSONUtil.getInitialDataJson());
                }

                JSONObject fetchedDataForType = dataObject.getJSONObject(responseType.getKey());
                JSONArray responseRecords = fetchedDataForType.getJSONArray(JSON_KEY_RESPONSE_RECORDS);

                if (!jsonResponse.isNull(JSON_KEY_DATA)) {
                    JSONObject responseData = jsonResponse.getJSONObject(JSON_KEY_DATA);
                    // Insert json response to array with type.
                    responseData = preProcessResponse(responseType, responseData);
                    responseRecords.put(responseData);
                } else {
                    if (responseRecords.length() == 0) {
                        dataObject.put(responseType.getKey(), errorResponseGenerator.generateErrorResponse(jsonResponse));
                    }
                }

                if (responseRecords.length() > 0 && !Objects.equals(HttpStatus.OK.value(), fetchedDataForType.getInt(JSON_KEY_STATUS_CODE))) {
                    fetchedDataForType.put(JSON_KEY_STATUS_CODE, HttpStatus.OK.value());
                    fetchedDataForType.put(JSON_KEY_STATUS_MESSAGE, JSON_KEY_SUCCESS);
                }

                // Save to redis-cache.
                logger.info("Updating data in redis after getting resonse for request id: " + requestId);
                redisDataHandler.updateDataList(requestId, dataObject);
            }
        } catch (Exception e) {
            logger.error("Error while update data from redis data updater for channel: " + requestId, e);
        }
    }

    private JSONObject preProcessResponse(PublicApiWorkerTaskType taskType, JSONObject responseData) {
        ResponseProcessor responseProcessor = responseProcessorServiceFactory.getResponseProcessor(taskType);
        return responseProcessor.processResponse(responseData);
    }

	@Override
	public void updateRequestInfo(String key, JSONObject requestInfo) {
	}
}
