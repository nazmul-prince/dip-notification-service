package com.dip.unifiedviewer.infrastructure.repositories;

import com.dip.unifiedviewer.domain.model.requests.NbrRequestBodyModel;
import com.dip.unifiedviewer.infrastructure.request_handlers.RequestHandler;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class NbrRequestRepository {
    RequestHandler requestHandler;
    @Value("${dip.publicapi.rule.baseUrl}")
    private String baseUrl;
    @Value("${dip.publicapi.rule.serviceUrl.nbr}")
    private String serviceUrl;
    public static final String nbrRequestBodyFormat =
            ""
                    + "{\"requestId\":\"%s\","
                    + "\"requestKey\": \"%s\","
                    + "\"requestValue\": \"%s\","
                    + "\"userId\": \"%s\","
                    + "\"agencyId\": \"%s\","
                    + "\"channels\": [\"%s\"],"
                    + "\"apiType\": \"%s\","
                    + "\"queryTree\": %s"
                    + "}";

    public NbrRequestRepository(RequestHandler requestHandler) {
        this.requestHandler = requestHandler;
    }

    public void executeRequest(NbrRequestBodyModel bodyModel, JSONArray queryJson) {
        String url = baseUrl + serviceUrl;
        String requestBody = String.format(nbrRequestBodyFormat,
                bodyModel.getRequestId(),
                bodyModel.getRequestKey(),
                bodyModel.getRequestValue(),
                bodyModel.getUserId(),
                bodyModel.getAgencyId(),
                bodyModel.getRequestId(),
                bodyModel.getApiType(),
                queryJson
        );

        requestHandler.executeRequest(url, requestBody);
    }
}
