package com.dip.unifiedviewer.domain.model.responses;

import com.dip.unifiedviewer.constansts.Status;

import lombok.Getter;

@Getter
public class CheckStatusResponseBodyModel {
    private String message;
    private String status;

    public CheckStatusResponseBodyModel(Status status) {
        this.message = status.getMessage();
        this.status = status.name();
    }
}
