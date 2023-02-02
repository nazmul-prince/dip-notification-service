package com.dip.unifiedviewer.domain.response.processors.impls;

import com.dip.unifiedviewer.constansts.PublicApiWorkerTaskType;
import com.dip.unifiedviewer.domain.response.processors.ResponseProcessor;

import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public class DefaultResponseProcessorImpl implements ResponseProcessor {
    private final PublicApiWorkerTaskType workerTaskType = PublicApiWorkerTaskType.DEFAULT;

    @Override
    public JSONObject processResponse(JSONObject jsonObject) {
        return jsonObject;
    }

    @Override
    public PublicApiWorkerTaskType getWorkerTaskType() {
        return workerTaskType;
    }
}
