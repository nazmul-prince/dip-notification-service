package com.dip.unifiedviewer.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskDecorator;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfigurer {

	@Bean("threadPoolTaskExecutor")
	public TaskExecutor getAsyncExecutor(TaskDecorator taskDecorator) {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(1000);
		executor.setMaxPoolSize(1500);
		executor.setQueueCapacity(1000);
		executor.setThreadNamePrefix("dip-unified-viewer");
		executor.setTaskDecorator(taskDecorator);
		executor.initialize();
		return executor;
	}
}
