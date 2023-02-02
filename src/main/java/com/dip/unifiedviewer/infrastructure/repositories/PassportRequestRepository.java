package com.dip.unifiedviewer.infrastructure.repositories;

import com.dip.unifiedviewer.domain.model.requests.PassportRequestBodyModel;
import com.dip.unifiedviewer.infrastructure.request_handlers.RequestHandler;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PassportRequestRepository {
  private static final String PARAMETER_TYPE_NATIONAL_ID = "NationalID";
  private static final String PARAMETER_TYPE_PASSPORT_NO = "PassportNo";

  private final RequestHandler requestHandler;

  @Value("${dip.publicapi.rule.baseUrl}")
  private String baseUrl;

  @Value("${dip.publicapi.rule.serviceUrl.passport}")
  private String serviceUrl;

  private static final String passportRequestBodyFormat =
      ""
          + "{\"requestId\":\"%s\","
          + "\"parameterType\":\"%s\","
          + "\"parameterValue\": \"%s\","
          + "\"userId\": \"%s\","
          + "\"agencyId\": \"%s\","
          + "\"caseId\": \"%s\","
          + "\"searchMode\": \"%s\","
          + "\"channels\": [\"%s\"],"
          + "\"apiType\": \"%s\","
          + "\"queryTree\": %s"
          + "}";

  public PassportRequestRepository(RequestHandler requestHandler) {
    this.requestHandler = requestHandler;
  }

  public void executeRequest(PassportRequestBodyModel bodyModel, JSONArray queryJson) {
    String parameterType =
        (bodyModel.getRequestKey().matches("[0-9]+"))
            ? PARAMETER_TYPE_NATIONAL_ID
            : PARAMETER_TYPE_PASSPORT_NO;
    String requestBody =
        String.format(
            passportRequestBodyFormat,
            bodyModel.getRequestId(),
            parameterType,
            bodyModel.getRequestKey(),
            bodyModel.getUserId(),
            bodyModel.getAgencyId(),
            bodyModel.getCaseId(),
            bodyModel.getSearchMode(),
            bodyModel.getRequestId(),
            bodyModel.getApiType(),
            queryJson);

    String url = baseUrl + serviceUrl;
    requestHandler.executeRequest(url, requestBody);
  }
}
