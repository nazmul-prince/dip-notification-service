package com.dip.unifiedviewer.storage.redis.service.impls;

import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.redisson.api.map.event.EntryExpiredListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Service;

import com.dip.unifiedviewer.storage.redis.service.DistributedLockService;
import com.dip.unifiedviewer.storage.redis.service.RedisListenerService;

import static com.dip.unifiedviewer.constansts.RedisConstants.*;

import java.util.concurrent.TimeUnit;

@Service
public class RedisDistributedLockServiceImpl implements DistributedLockService {
	private RMapCache<String, String> mapCache;
	private final RedissonClient redissonClient;

	@Value("${dip.publicapi.redis.timeout}")
	private int timeoutInMillis;
	private final RedisMessageListenerContainer redisMessageListenerContainer;
	private final MessageListenerAdapter messageListenerAdapter;
	private final RedisListenerService redisListenerService;

	private final Logger logger = LoggerFactory.getLogger(RedisDistributedLockServiceImpl.class);

	public RedisDistributedLockServiceImpl(RedissonClient redissonClient,
			@Lazy RedisMessageListenerContainer redisMessageListenerContainer,
			@Lazy MessageListenerAdapter messageListenerAdapter, @Lazy RedisListenerService redisListenerService) {
		this.redissonClient = redissonClient;
		this.redisMessageListenerContainer = redisMessageListenerContainer;
		this.messageListenerAdapter = messageListenerAdapter;
		this.redisListenerService = redisListenerService;
		mapCache = this.redissonClient.getMapCache(REDIS_MAP_KEY);
		mapCacheExpiryListener();
	}

	@Override
	public void putData(String key, String value) {
		mapCache.put(key, value, timeoutInMillis, TimeUnit.MILLISECONDS);
	}

	@Override
	public String getData(String key) {
		return mapCache.get(key);
	}

	private String extractChannelId(String key) {
		return key.substring(REDIS_KEY_FETCHABLE_PREFIX.length());
	}

	private void mapCacheExpiryListener() {
		mapCache.addListener((EntryExpiredListener<String, String>) event -> {
			if (event.getKey().startsWith(REDIS_KEY_FETCHABLE_PREFIX)) {
				String channelId = extractChannelId(event.getKey());
				logger.info("On Expired from channel " + channelId + " with key: " + event.getKey() + ", value: "
						+ event.getValue());
				redisListenerService.removeListenerByChannelName(this.redisMessageListenerContainer,
						this.messageListenerAdapter, channelId);
			}
		});
	}

	@Override
	public boolean containsKey(String key) {
		return mapCache.containsKey(key);
	}
}
