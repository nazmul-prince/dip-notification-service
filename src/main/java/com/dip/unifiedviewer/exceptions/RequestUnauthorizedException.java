package com.dip.unifiedviewer.exceptions;

public class RequestUnauthorizedException extends RuntimeException {

    public RequestUnauthorizedException(String message) {
        super(message);
    }
}
