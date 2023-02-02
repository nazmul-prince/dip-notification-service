package com.dip.unifiedviewer.exceptions;

public class RequestForbiddenException extends RuntimeException {

    public RequestForbiddenException(String message) {
        super(message);
    }
}
