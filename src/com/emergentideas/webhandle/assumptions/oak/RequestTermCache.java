package com.emergentideas.webhandle.assumptions.oak;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

/**
 * A cache using {@link ThreadLocal} to provide a way for components to cache information
 * for the length of a request. It is the promise made by this structure that somebody 
 * other than the callign code will be setting it up and cleaning it up on every request.
 * @author kolz
 *
 */
@Resource
public class RequestTermCache {
	
	protected ThreadLocal<Map<String, Object>> threadCache = new ThreadLocal<Map<String, Object>>();
	
	public void clear() {
		threadCache.remove();
	}
	
	public void put(String key, Object value) {
		Map<String, Object> cache = threadCache.get();
		if(cache == null) {
			cache = new HashMap<String, Object>();
			threadCache.set(cache);
		}
		cache.put(key, value);
	}
	
	public Object get(String key) {
		Map<String, Object> cache = threadCache.get();
		
		if(cache == null) {
			return null;
		}
		
		return cache.get(key);
	}
	
	public boolean containsKey(String key) {
		Map<String, Object> cache = threadCache.get();
		
		if(cache == null) {
			return false;
		}
		
		return cache.containsKey(key);
	}

}
