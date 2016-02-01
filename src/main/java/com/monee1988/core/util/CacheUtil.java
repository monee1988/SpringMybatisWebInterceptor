package com.monee1988.core.util;

import java.io.InputStream;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

public class CacheUtil {

	private static CacheManager CACHE_MANAGER;
	private static CacheUtil cacheUtil = new CacheUtil();

	public static CacheUtil getInstance(String file) {
		
		InputStream inputStream = CacheUtil.class.getResourceAsStream(file);

		if (inputStream != null) {
			try {
				CacheManager cacheManager = CacheManager.create(inputStream);
				CACHE_MANAGER = cacheManager;
				return cacheUtil;
			} catch (Throwable t) {
				return getInstance();
			}
		}
		return getInstance();
	}

	public static CacheUtil getInstance() {
		try {
			CacheManager cacheManager = CacheManager.create();
			CACHE_MANAGER = cacheManager;
		} catch (Throwable t) {
			CacheManager cacheManager = CacheManager.create();
			CACHE_MANAGER = cacheManager;
		}

		return cacheUtil;
	}

	public void put(String cacheName, String key, Object value) {
		Cache cache = CACHE_MANAGER.getCache(cacheName);
		if (value == null)
			cache.remove(key);
		else
			cache.put(new Element(key, value));
	}

	public static Object get(String cacheName, String key) {
		Cache cache = CACHE_MANAGER.getCache(cacheName);
		Element element = cache.get(key);
		if (element == null) {
			return null;
		}
		return element.getObjectValue();
	}

	public void flush(String cacheName, String key) {
		Cache cache = CACHE_MANAGER.getCache(cacheName);
		cache.remove(key);
	}
}