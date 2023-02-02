package com.dip.unifiedviewer.redis.publisher.services;

import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This class publish messages to redis channel
 * 
 * @author dev
 *
 */
@Component
public class RedisMessagePublisher implements MessagePublisher {
	Logger logger = LoggerFactory.getLogger(RedisMessagePublisher.class);

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	@Autowired
	private ObjectMapper objectMapper;

	public RedisMessagePublisher() {
	}

	/**
	 * Publishes an object to specific channel in redis defined by channelName
	 */
	@Override
	public void publish(String channelName, Object message) {
		channelName = (channelName == null || channelName.isEmpty()) ? "redisChannel" : channelName;
		logger.info("Publishing message in channel: " + channelName);
		
		try {
			this.redisTemplate.convertAndSend(channelName, message);
		} catch (Exception e) {
            logger.error("Couldn't able to publish data in channel: " + channelName, e);
		}
	}

	@Override
	public void publish(List<String> channels, Object message) throws JsonProcessingException {
		
		if (Objects.equals(channels, null) || channels.size() == 0) {
//			publish("", message);
			return;
		}
		for (String channel : channels) {
			publish(channel, message);
		}
		
	}
}