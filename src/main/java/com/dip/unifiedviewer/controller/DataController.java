package com.dip.unifiedviewer.controller;

import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dip.unifiedviewer.domain.model.requests.BaseDataSourceRequestModel;
import com.dip.unifiedviewer.domain.model.requests.BaseRequestBodyModel;
import com.dip.unifiedviewer.domain.model.responses.CheckStatusResponseBodyModel;
import com.dip.unifiedviewer.domain.model.responses.InitiateRequestResponseBodyModel;
import com.dip.unifiedviewer.domain.services.DomainService;
import com.dip.unifiedviewer.domain.services.RequestInitiatorTemplate;
import com.dip.unifiedviewer.gateway.services.QueryServiceProGatewayService;
import com.dip.unifiedviewer.util.ResponseUtil;

@RestController
@RequestMapping(value = "unified-viewer-service/api/v1/public")
public class DataController {

	private final Logger logger = LoggerFactory.getLogger(DataController.class);
	
	private RequestInitiatorTemplate requestInitiatorTemplateService;

//  private final DomainService domainService;
//
  public DataController(RequestInitiatorTemplate requestInitiatorTemplateService) {
    this.requestInitiatorTemplateService = requestInitiatorTemplateService;
  }

//	@PostMapping(value = "/initiate-request", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseEntity<InitiateRequestResponseBodyModel> initiateRequest(Map<String, Object> dataSourceRequest) {
//
//    logger.info("Generated request ID: " + requestBodyModel.getRequestId() + requestBodyModel);
//    requestBodyModel.setCaseId(requestBodyModel.getAgencyId());
//    logger.info(requestBodyModel.getRule().getValue() + " rule has been called with request body: " + requestBodyModel);
//    return ResponseUtil.getResponseEntity(
//        HttpStatus.OK, new HttpHeaders(), domainService.executeRequest(requestBodyModel));
//		return null;
//	}

//  @PostMapping(
//      value = "/check-status",
//      consumes = MediaType.APPLICATION_JSON_VALUE,
//      produces = MediaType.APPLICATION_JSON_VALUE)
//  public ResponseEntity<CheckStatusResponseBodyModel> postDataStatusRequest(
//      @RequestBody BaseRequestBodyModel dataFetchRequestBodyModel) {
//    logger.info(
//        "Status request has been initiated with request ID: "
//            + dataFetchRequestBodyModel.getRequestId());
//    return ResponseUtil.getResponseEntity(
//        HttpStatus.OK,
//        new HttpHeaders(),
//        domainService.executeStatusCheckRequest(dataFetchRequestBodyModel));
//  }

//  @PostMapping(
//      value = "/fetch-data",
//      consumes = MediaType.APPLICATION_JSON_VALUE,
//      produces = MediaType.APPLICATION_JSON_VALUE)
//  public ResponseEntity<String> postDataFetchRequest(
//      @RequestBody BaseRequestBodyModel dataFetchRequestBodyModel) {
//    logger.info(
//        "Fetch request has been initiated with request ID: "
//            + dataFetchRequestBodyModel.getRequestId());
//    Optional<String> response = domainService.executeDataFetchRequest(dataFetchRequestBodyModel);
//    return ResponseUtil.getResponseEntity(HttpStatus.OK, new HttpHeaders(), response.get());
//  }

	@PostMapping(value = "/initiate-request", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> postDataSourceRequest(
			@RequestBody BaseDataSourceRequestModel baseDataSourceRequestModel) {
		requestInitiatorTemplateService.initiateUnifiedRequest(baseDataSourceRequestModel);
		logger.info("Returning");
		return (ResponseEntity<String>) ResponseEntity.ok().body("Ok");
	}

	@Autowired
	private QueryServiceProGatewayService gatewayService;

	@PostMapping(value = "/check", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> postDataFetchRequest(@RequestBody Map<String, Object> requestBody) {
		logger.info("checking");
		JSONObject request = new JSONObject(requestBody);
		JSONObject reqObj = request.getJSONObject("requestBody");
		gatewayService.postRequest(reqObj.toString(), "nid");
		return null;
	}

	@GetMapping(value = "healthcheck", produces = MediaType.APPLICATION_JSON_VALUE)
	public String healthCheck() {

		String msg = "{\n" + "  \"status\": 200,\n" + "  \"message\": \"Health is OK!\"\n" + "}";
		return msg;
	}
}
