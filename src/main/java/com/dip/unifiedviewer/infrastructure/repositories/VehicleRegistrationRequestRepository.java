package com.dip.unifiedviewer.infrastructure.repositories;

import com.dip.unifiedviewer.domain.model.requests.VehicleRegistrationRequestBodyModel;
import com.dip.unifiedviewer.infrastructure.request_handlers.RequestHandler;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class VehicleRegistrationRequestRepository {
    private final RequestHandler requestHandler;

    @Value("${dip.publicapi.rule.baseUrl}")
    private String baseUrl;

    @Value("${dip.publicapi.rule.serviceUrl.vehicleRegistration}")
    private String serviceUrl;

    protected final String vehicleRegistrationRequestBodyFormat =
            ""
                    + "{\"requestId\":\"%s\","
                    + "\"vehicleNumber\": \"%s\","
                    + "\"zone\": \"%s\","
                    + "\"series\": \"%s\","
                    + "\"userId\": \"%s\","
                    + "\"agencyId\": \"%s\","
                    + "\"caseId\": \"%s\","
                    + "\"searchMode\": \"%s\","
                    + "\"channels\": [\"%s\"],"
                    + "\"apiType\": \"%s\","
                    + "\"queryTree\": %s"
                    + "}";

    public VehicleRegistrationRequestRepository(RequestHandler requestHandler) {
        this.requestHandler = requestHandler;
    }

    public void executeRequest(VehicleRegistrationRequestBodyModel bodyModel, JSONArray queryJson) {
        String url = baseUrl + serviceUrl;
        String requestBody =
                String.format(
                        vehicleRegistrationRequestBodyFormat,
                        bodyModel.getRequestId(),
                        bodyModel.getRequestKey(),
                        bodyModel.getZone(),
                        bodyModel.getSeries(),
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
