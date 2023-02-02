package com.dip.unifiedviewer.constansts;

import lombok.Getter;

@Getter
public enum Status {
    READY("Finished processing data"),
    NOT_READY("Still processing data"),
    NOT_FOUND("Request Id not valid or expired or not found");

    private String message;

    Status(String message) {
        this.message = message;
    }
}
