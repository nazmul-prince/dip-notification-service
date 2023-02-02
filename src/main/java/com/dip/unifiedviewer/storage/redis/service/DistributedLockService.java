package com.dip.unifiedviewer.storage.redis.service;

public interface DistributedLockService {
	void putData(String key, String value);
	String getData(String key);
	boolean containsKey(String key);
}