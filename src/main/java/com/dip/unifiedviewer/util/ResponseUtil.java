package com.dip.unifiedviewer.util;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseUtil {

    private ResponseUtil() {}

    public static <T> ResponseEntity<T> getResponseEntity(HttpStatus status, HttpHeaders headers, T body) {
        return ResponseEntity.status(status).headers(headers).body(body);
    }
}
