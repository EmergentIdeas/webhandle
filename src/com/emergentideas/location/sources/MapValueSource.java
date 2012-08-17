package com.emergentideas.location.sources;

import java.util.Map;

import com.emergentideas.location.InvocationContext;
import com.emergentideas.location.ValueSource;

public class MapValueSource implements ValueSource<Object> {
	
	protected Map<String, Object> data;
	
	public MapValueSource(Map<String, Object> data) {
		this.data = data;
	}
	
	public <T> Object get(String name, Class<T> type, InvocationContext context) {
		return data.get(name);
	}

	public <T> boolean canGet(String name, Class<T> type,
			InvocationContext context) {
		return data.containsKey(name);
	}

	
}
