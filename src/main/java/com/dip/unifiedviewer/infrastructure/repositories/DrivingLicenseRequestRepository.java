package com.dip.unifiedviewer.infrastructure.repositories;

import com.dip.unifiedviewer.domain.model.requests.DrivingLicenseRequestBodyModel;
import com.dip.unifiedviewer.infrastructure.request_handlers.RequestHandler;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/* repository that builds driving license request body model for rule-engine-pro service*/
@Service
public class DrivingLicenseRequestRepository {
  private final RequestHandler requestHandler;

  @Value("${dip.publicapi.rule.baseUrl}")
  private String baseUrl;

  @Value("${dip.publicapi.rule.serviceUrl.drivingLicense}")
  private String serviceUrl;

  private static final String drivingLicenseRequestBodyFormat =
          ""
                  + "{\"requestId\":\"%s\","
                  + "\"dlNo\":\"%s\","
                  + "\"userId\": \"%s\","
                  + "\"agencyId\": \"%s\","
                  + "\"caseId\": \"%s\","
                  + "\"searchMode\": \"%s\","
                  + "\"channels\": [\"%s\"],"
                  + "\"apiType\": \"%s\","
                  + "\"queryTree\": %s"
                  + "}";

  public DrivingLicenseRequestRepository(RequestHandler ruleEngineRequestHandler) {
    this.requestHandler = ruleEngineRequestHandler;
  }

  /* formats the driving license requested body model and
  * the formatted body model is executed using Request Handler's executeRequest method
  * @param bodyModel, queryJson
  * @return
  * @throws
  */
  public void executeRequest(DrivingLicenseRequestBodyModel bodyModel, JSONArray queryJson) {
    String url = baseUrl + serviceUrl;
    String requestBody =
            String.format(
                    drivingLicenseRequestBodyFormat,
                    bodyModel.getRequestId(),
                    bodyModel.getRequestKey(),
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
