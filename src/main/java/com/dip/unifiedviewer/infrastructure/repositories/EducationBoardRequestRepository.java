package com.dip.unifiedviewer.infrastructure.repositories;

import com.dip.unifiedviewer.domain.model.requests.EducationBoardRequestBodyModel;
import com.dip.unifiedviewer.infrastructure.request_handlers.RequestHandler;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EducationBoardRequestRepository {
    private final RequestHandler requestHandler;

    @Value("${dip.publicapi.rule.baseUrl}")
    private String baseUrl;

    @Value("${dip.publicapi.rule.serviceUrl.educationBoard}")
    private String serviceUrl;

    private static final String educationBoardRequestBodyFormat =
            ""
                    + "{\"requestId\":\"%s\","
                    + "\"userId\": \"%s\","
                    + "\"agencyId\": \"%s\","
                    + "\"caseId\": \"%s\","
                    + "\"searchMode\": \"%s\","
                    + "\"channels\": [\"%s\"],"
                    + "\"apiType\": \"%s\","
                    + "\"searchParameters\":[%s],"
                    + "\"requestData\":\"%s\","
                    + "\"selectionCriteria\":\"%s\","
                    + "\"page\":\"%s\","
                    + "\"size\":\"%s\","
                    + "\"queryTree\": %s"
                    + "}";

    private static final String educationBoardSearchParameters =
            ""
                    + "{\"searchValue1\":\"%s\","
                    + "\"searchValue2\":\"null\","
                    + "\"searchField\":\"BOARD_NAME\","
                    + "\"queryCondition\":\"EQUAL\""
                    + "},"
                    + "{\"searchValue1\":\"%s\","
                    + "\"searchValue2\":\"null\","
                    + "\"searchField\":\"PASSING_YEAR\","
                    + "\"queryCondition\":\"EQUAL\""
                    + "},"
                    + "{\"searchValue1\":\"%s\","
                    + "\"searchValue2\":\"null\","
                    + "\"searchField\":\"EXAM_NAME\","
                    + "\"queryCondition\":\"EQUAL\""
                    + "},"
                    + "{\"searchValue1\":\"%s\","
                    + "\"searchValue2\":\"null\","
                    + "\"searchField\":\"ROLL_NO\","
                    + "\"queryCondition\":\"EQUAL\""
                    + "},"
                    + "{\"searchValue1\":\"%s\","
                    + "\"searchValue2\":\"null\","
                    + "\"searchField\":\"REGISTRATION_NO\","
                    + "\"queryCondition\":\"EQUAL\""
                    + "}";

    public EducationBoardRequestRepository(RequestHandler ruleEngineRequestHandler) {
        this.requestHandler = ruleEngineRequestHandler;
    }

    public String processEducationBoardRequestList(EducationBoardRequestBodyModel bodyModel){
        return String.format(
                educationBoardSearchParameters,
                bodyModel.getBoardName(),
                bodyModel.getPassingYear(),
                bodyModel.getExamName(),
                bodyModel.getRollNo(),
                bodyModel.getRegistrationNo());
    }

    public void executeRequest(EducationBoardRequestBodyModel bodyModel, JSONArray queryJson) {
        String url = baseUrl + serviceUrl;
        String requestBody =
                String.format(
                        educationBoardRequestBodyFormat,
                        bodyModel.getRequestId(),
                        bodyModel.getUserId(),
                        bodyModel.getAgencyId(),
                        bodyModel.getCaseId(),
                        bodyModel.getSearchMode(),
                        bodyModel.getRequestId(),
                        bodyModel.getApiType(),
                        processEducationBoardRequestList(bodyModel),
                        bodyModel.getRequestData(),
                        bodyModel.getSelectionCriteria(),
                        bodyModel.getPage(),
                        bodyModel.getSize(),
                        queryJson
                );
        requestHandler.executeRequest(url, requestBody);
    }
}
