package com.dip.unifiedviewer.configuration;

import org.redisson.api.RedissonClient;
import org.redisson.spring.data.connection.RedissonConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.dip.unifiedviewer.storage.redis.service.RedisMessageSubscriber;

import lombok.extern.slf4j.Slf4j;

/**
 * Redis configuration for this service.
 * @author Md Sadman Sakib
 */

@Configuration
@Slf4j
public class RedissonSpringDataConfig {
	
	@Autowired
	private RedisMessageSubscriber redisMessageSubscriber;
	
	@Autowired
	private RedissonClient redissonClient;
	
	@Bean
	public RedisConnectionFactory redisConnectionFactory() {
		return new RedissonConnectionFactory(redissonClient);
	}

	@Bean
	public RedisTemplate<String, Object> redisTemplate() {
		RedisTemplate<String, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(redisConnectionFactory());
		template.setValueSerializer(new GenericToStringSerializer<Object>(Object.class));
		template.setDefaultSerializer(new GenericJackson2JsonRedisSerializer());
		template.setKeySerializer(new StringRedisSerializer());
		template.setHashKeySerializer(new GenericJackson2JsonRedisSerializer());
		template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
		return template;
	}
	
	@Bean
    public MessageListenerAdapter messageListenerAdapter() {
        return new MessageListenerAdapter(redisMessageSubscriber, "onMessage");
    }
	
	@Bean
    public RedisMessageListenerContainer container(){
           RedisMessageListenerContainer redisMessageListenerContainer = new RedisMessageListenerContainer();
           redisMessageListenerContainer.setConnectionFactory(redisConnectionFactory());
           redisMessageListenerContainer.setErrorHandler(e -> {
        	   log.error("Error in message listener.", e);
           });
           return redisMessageListenerContainer;
    }
}
