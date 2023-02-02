package com.dip.unifiedviewer.infrastructure.repositories;

import com.dip.unifiedviewer.domain.model.requests.PstnMsisdnRequestBodyModel;
import com.dip.unifiedviewer.infrastructure.request_handlers.RequestHandler;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PstnMsisdnRequestRepository {
    RequestHandler requestHandler;

    @Value("${dip.publicapi.rule.baseUrl}")
    private String baseUrl;

    @Value("${dip.publicapi.rule.serviceUrl.pstnMsisdn}")
    private String serviceUrl;

    public static final String pstnMsisdnRequestBodyFormat =
            ""
                    + "{\"requestId\":\"%s\","
                    + "\"requestType\": \"%s\","
                    + "\"searchValue\": \"%s\","
                    + "\"userId\": \"%s\","
                    + "\"agencyId\": \"%s\","
                    + "\"channels\": [\"%s\"],"
                    + "\"apiType\": \"%s\","
                    + "\"queryTree\": %s"
                    + "}";

    public PstnMsisdnRequestRepository(RequestHandler requestHandler) {
        this.requestHandler = requestHandler;
    }

    public void executeRequest(PstnMsisdnRequestBodyModel bodyModel, JSONArray queryJson) {
        String url = baseUrl + serviceUrl;
        String requestBody = String.format(
                pstnMsisdnRequestBodyFormat,
                bodyModel.getRequestId(),
                "pstn-msisdn",
                bodyModel.getRequestKey(),
                bodyModel.getUserId(),
                bodyModel.getAgencyId(),
                bodyModel.getRequestId(),
                bodyModel.getApiType(),
                queryJson
        );

        requestHandler.executeRequest(url, requestBody);
    }
}
