package com.dip.unifiedviewer.redis.publisher.services;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface MessagePublisher {

	/**
	 * Publishes an object to specific channel in redis defined by channelName
	 * 
	 * @param channelName
	 * @param message
	 * @throws JsonProcessingException
	 */
	void publish(String channelName, Object message);

	void publish(List<String> channels, Object message) throws JsonProcessingException;
}