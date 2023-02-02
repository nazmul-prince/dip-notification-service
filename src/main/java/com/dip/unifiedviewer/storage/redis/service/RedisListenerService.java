package com.dip.unifiedviewer.storage.redis.service;

import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class RedisListenerService {
	private final Logger logger = LoggerFactory.getLogger(RedisListenerService.class);
	
	@Async("threadPoolTaskExecutor")
	public CompletableFuture<Boolean> addListenerByChannelName(RedisMessageListenerContainer redisMessageListenerContainer,
												MessageListenerAdapter messageListenerAdapter,
												String channelName) {
		logger.info("Subscribing to channel " + channelName + " in thread " + Thread.currentThread().getName());
		redisMessageListenerContainer.addMessageListener(messageListenerAdapter, new ChannelTopic(channelName));
		return CompletableFuture.completedFuture(true);
	}
	
	@Async("threadPoolTaskExecutor")
	public CompletableFuture<Void> removeListenerByChannelName(RedisMessageListenerContainer redisMessageListenerContainer,
												MessageListenerAdapter messageListenerAdapter,
												String channelName) {
		logger.info("Unsubscribing channel " + channelName + " in thread " + Thread.currentThread().getName());
		redisMessageListenerContainer.removeMessageListener(messageListenerAdapter, new ChannelTopic(channelName));
		return CompletableFuture.completedFuture(null);
	}
}
