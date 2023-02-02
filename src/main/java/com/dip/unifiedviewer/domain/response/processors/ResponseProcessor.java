package com.dip.unifiedviewer.domain.response.processors;

import org.json.JSONObject;

import com.dip.unifiedviewer.constansts.PublicApiWorkerTaskType;

public interface ResponseProcessor {
    JSONObject processResponse(JSONObject jsonObject);

    PublicApiWorkerTaskType getWorkerTaskType();
}