package com.dip.unifiedviewer.domain.model;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiExposureError {

    private String timestamp;
    private int statusCode;
    private String error;
    private String message;

    public ApiExposureError(HttpStatus status, String message) {
        this.timestamp = LocalDateTime.now().toString();
        this.statusCode = status.value();
        this.error = status.name();
        this.message = message;
    }
}
