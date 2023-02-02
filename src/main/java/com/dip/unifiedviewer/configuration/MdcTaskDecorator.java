package com.dip.unifiedviewer.configuration;

import lombok.extern.log4j.Log4j2;
import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;
import org.springframework.stereotype.Component;

import java.util.Map;

@Log4j2
@Component
public class MdcTaskDecorator implements TaskDecorator {

    @Override
    public Runnable decorate(Runnable runnable) {
        Map<String, String> contextMap = MDC.getCopyOfContextMap();
        if (contextMap == null) return runnable;
        log.debug("Saving previous MDC Context for async thread...");
        return () -> {
            try {
                MDC.setContextMap(contextMap);
                log.debug("Restoring MDC Context for async thread...");
                runnable.run();
            } catch (Throwable e) {
                log.error("Error in async task", e);
            } finally {
                MDC.clear();
            }
        };
    }
}
