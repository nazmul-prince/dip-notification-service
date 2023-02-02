package com.dip.unifiedviewer.domain.model.responses;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class InitiateRequestResponseBodyModel {
    private static final String MESSAGE_FORMAT = "Request for %s has been initiated";

    private String message;

    @JsonIgnore
    private String accessToken;
    private String requestId;
    private int statusCode;

    public InitiateRequestResponseBodyModel(String rule, String accessToken, String requestId) {
        this.message = String.format(MESSAGE_FORMAT, rule);
        this.statusCode = HttpStatus.OK.value();
        this.accessToken = "Bearer " + accessToken;
        this.requestId = requestId;
    }
}
