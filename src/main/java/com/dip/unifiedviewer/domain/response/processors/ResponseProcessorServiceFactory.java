package com.dip.unifiedviewer.domain.response.processors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dip.unifiedviewer.constansts.PublicApiWorkerTaskType;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class ResponseProcessorServiceFactory {
    private final Logger logger = LoggerFactory.getLogger(ResponseProcessorServiceFactory.class);
    private final Map<PublicApiWorkerTaskType, ResponseProcessor> map;

    @Autowired
    ResponseProcessorServiceFactory(Set<ResponseProcessor> responseProcessorSet) {
        map = new HashMap<>();
        responseProcessorSet.forEach(responseProcessor -> map.put(responseProcessor.getWorkerTaskType(), responseProcessor));
    }

    public ResponseProcessor getResponseProcessor(PublicApiWorkerTaskType workerTaskType) {
        logger.info("Preprocessing response for worker task type -> " + workerTaskType.getValue());
        ResponseProcessor responseProcessor = map.get(workerTaskType);
        if (responseProcessor == null) return map.get(PublicApiWorkerTaskType.DEFAULT);
        return responseProcessor;
    }
}
