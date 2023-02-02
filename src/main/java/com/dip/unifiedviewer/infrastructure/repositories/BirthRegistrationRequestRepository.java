package com.dip.unifiedviewer.infrastructure.repositories;

import com.dip.unifiedviewer.domain.model.requests.BirthRegistrationRequestBodyModel;
import com.dip.unifiedviewer.infrastructure.request_handlers.RequestHandler;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class BirthRegistrationRequestRepository {

  private final RequestHandler requestHandler;

  @Value("${dip.publicapi.rule.baseUrl}")
  private String baseUrl;

  @Value("${dip.publicapi.rule.serviceUrl.birthRegistration}")
  private String serviceUrl;

  private static final String birthRegistrationRequestBodyFormat =
          ""
                  + "{\"requestId\":\"%s\","
                  + "\"birthRegNo\":\"%s\","
                  + "\"birthDate\": \"%s\","
                  + "\"userId\": \"%s\","
                  + "\"agencyId\": \"%s\","
                  + "\"caseId\": \"%s\","
                  + "\"searchMode\": \"%s\","
                  + "\"channels\": [\"%s\"],"
                  + "\"apiType\": \"%s\","
                  + "\"queryTree\": %s"
                  + "}";

  public BirthRegistrationRequestRepository(RequestHandler requestHandler) {
    this.requestHandler = requestHandler;
  }

  public void executeRequest(BirthRegistrationRequestBodyModel bodyModel, JSONArray queryJson) {
    String url = baseUrl + serviceUrl;
    String birthRegistrationRequestBody =
            String.format(
                    birthRegistrationRequestBodyFormat,
                    bodyModel.getRequestId(),
                    bodyModel.getRequestKey(),
                    bodyModel.getDateOfBirth(),
                    bodyModel.getUserId(),
                    bodyModel.getAgencyId(),
                    bodyModel.getCaseId(),
                    bodyModel.getSearchMode(),
                    bodyModel.getRequestId(),
                    bodyModel.getApiType(),
                    queryJson);
    requestHandler.executeRequest(url, birthRegistrationRequestBody);
  }
}
