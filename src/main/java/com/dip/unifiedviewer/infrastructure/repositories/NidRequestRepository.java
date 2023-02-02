package com.dip.unifiedviewer.infrastructure.repositories;

import com.dip.unifiedviewer.domain.model.requests.NidRequestBodyModel;
import com.dip.unifiedviewer.infrastructure.request_handlers.RequestHandler;

import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class NidRequestRepository {
  private static final Logger logger = LoggerFactory.getLogger(NidRequestRepository.class);
  private final RequestHandler requestHandler;

  @Value("${dip.publicapi.rule.baseUrl}")
  private String baseUrl;

  @Value("${dip.publicapi.rule.serviceUrl.nid}")
  private String serviceUrl;

  private static final String nidRequestBodyFormat =
          ""
                  + "{\"requestId\":\"%s\","
                  + "\"nidNumber\":\"%s\","
                  + "\"dateOfBirth\": \"%s\","
                  + "\"userId\": \"%s\","
                  + "\"agencyId\": \"%s\","
                  + "\"caseId\": \"%s\","
                  + "\"searchMode\": \"%s\","
                  + "\"channels\": [\"%s\"],"
                  + "\"apiType\": \"%s\","
                  + "\"queryTree\": %s"
                  + "}";

  public NidRequestRepository(RequestHandler requestHandler) {
    this.requestHandler = requestHandler;
  }

  public void executeRequest(NidRequestBodyModel bodyModel, JSONArray queryJson) {
    String url = baseUrl + serviceUrl;
    String nidRequestBody =
            String.format(
                    nidRequestBodyFormat,
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
    requestHandler.executeRequest(url, nidRequestBody);
  }
}
