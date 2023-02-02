package com.dip.unifiedviewer.infrastructure.repositories;

import com.dip.unifiedviewer.domain.model.requests.MsisdnRequestBodyModel;
import com.dip.unifiedviewer.infrastructure.request_handlers.RequestHandler;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MsisdnRequestRepository {
    private final RequestHandler requestHandler;

    @Value("${dip.publicapi.rule.baseUrl}")
    private String baseUrl;

    @Value("${dip.publicapi.rule.serviceUrl.msisdn}")
    private String serviceUrl;

    private static final String msisdnRequestBodyFormat =
            ""
                    + "{\"requestId\":\"%s\","
                    + "\"requestType\": \"%s\","
                    + "\"searchValue\": \"%s\","
                    + "\"startDate\": \"%s\","
                    + "\"endDate\": \"%s\","
                    + "\"userId\": \"%s\","
                    + "\"agencyId\": \"%s\","
                    + "\"caseId\": \"%s\","
                    + "\"searchMode\": \"%s\","
                    + "\"channels\": [\"%s\"],"
                    + "\"apiType\": \"%s\","
                    + "\"queryTree\": %s"
                    + "}";

    public MsisdnRequestRepository(RequestHandler requestHandler) {
        this.requestHandler = requestHandler;
    }

    public void executeRequest(MsisdnRequestBodyModel bodyModel, JSONArray queryJson) {
        String url = baseUrl + serviceUrl;
        String requestBody =
                String.format(
                        msisdnRequestBodyFormat,
                        bodyModel.getRequestId(),
                        "cdr",
                        bodyModel.getRequestKey(),
                        bodyModel.getStartDate(),
                        bodyModel.getEndDate(),
                        bodyModel.getUserId(),
                        bodyModel.getAgencyId(),
                        bodyModel.getCaseId(),
                        bodyModel.getSearchMode(),
                        bodyModel.getRequestId(),
                        bodyModel.getApiType(),
                        queryJson);
        requestHandler.executeRequest(url, requestBody);
    }
}
