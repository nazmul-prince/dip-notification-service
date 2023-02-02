package com.dip.unifiedviewer.domain.services.impls;

import com.dip.unifiedviewer.domain.services.ErrorResponseGenerator;
import com.dip.unifiedviewer.util.JSONUtil;

import lombok.extern.slf4j.Slf4j;

import static com.dip.unifiedviewer.constansts.ApiExposureConstants.*;
import static com.dip.unifiedviewer.constansts.JsonConstants.*;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ErrorResponseGeneratorImpl implements ErrorResponseGenerator {
    @Override
    public JSONObject generateErrorResponse(JSONObject jsonResponse) {
        int statusCode;
        String message;

        if (jsonResponse.get(JSON_KEY_ERROR) instanceof JSONObject) {
            JSONObject error = jsonResponse.getJSONObject(JSON_KEY_ERROR);
            log.info("Error response for " + jsonResponse.getString(JSON_KEY_RESPONSE_TYPE) + " : " + error.toString() + ", request id : " + jsonResponse.getString(JSON_KEY_PUBLISHED_CHANNEL));

            switch (error.getString(JSON_KEY_STATUS)) {
                case UNPROCESSABLE_ENTITY:
                    statusCode = HttpStatus.UNPROCESSABLE_ENTITY.value();
                    message = HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase();
                    break;
                case NOT_FOUND:
                    statusCode = HttpStatus.NOT_FOUND.value();
                    message = HttpStatus.NOT_FOUND.getReasonPhrase();
                    break;
                case PARENT_RULE_FAILED:
                case REQUEST_TIMEOUT:
                    statusCode = error.getInt(JSON_KEY_STATUS_CODE);
                    message = error.getString(JSON_KEY_STATUS_MESSAGE);
                    break;
                case INTERNAL_SERVER_ERROR:
                default:
                    statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
                    message = HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase();
            }
        } else {
            statusCode = HttpStatus.NOT_FOUND.value();
            message = HttpStatus.NOT_FOUND.getReasonPhrase();
        }

        JSONObject jsonObject = JSONUtil.getInitialDataJson();
        jsonObject.put(JSON_KEY_STATUS_CODE, statusCode);
        jsonObject.put(JSON_KEY_STATUS_MESSAGE, message);

        return jsonObject;
    }
}
