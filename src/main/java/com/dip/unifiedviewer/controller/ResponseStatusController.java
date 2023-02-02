//package com.dip.unifiedviewer.controller;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.http.MediaType;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.dip.unifiedviewer.domain.model.requests.BaseRequestBodyModel;
//import com.dip.unifiedviewer.domain.model.responses.CheckStatusResponseBodyModel;
//import com.dip.unifiedviewer.domain.services.DomainService;
//
//
//@RestController
//@RequestMapping(value = "/api/v1/private/status")
//public class ResponseStatusController {
//
//    private Logger logger = LoggerFactory.getLogger(ResponseStatusController.class);
//
//    private final DomainService domainService;
//
//    public ResponseStatusController(DomainService domainService) {
//        this.domainService = domainService;
//    }
//
//    @PostMapping(
//            value = "/response-count-status",
//            consumes = MediaType.APPLICATION_JSON_VALUE)
//    public CheckStatusResponseBodyModel countStatus(
//            @RequestBody BaseRequestBodyModel countStatusRequestBodyModel) {
//        return domainService.executeStatusCheckRequest(countStatusRequestBodyModel);
//    }
//
//    @PostMapping(
//            value = "/response-status",
//            consumes = MediaType.APPLICATION_JSON_VALUE)
//    public String responseState(
//            @RequestBody BaseRequestBodyModel responseStateRequestBodyModel) {
//        return domainService.executeDataFetchRequest(responseStateRequestBodyModel).get();
//    }
//}
