package com.emergentideas.webhandle.sources;

import java.util.Map;

import com.emergentideas.webhandle.InvocationContext;
import com.emergentideas.webhandle.ValueSource;

public class MapValueSource implements ValueSource<Object> {
	
	protected Map<String, Object> data;
	protected boolean cachable = true;
	
	public MapValueSource(Map<String, ? extends Object> data) {
		this.data = (Map)data;
	}
	
	public <T> Object get(String name, Class<T> type, InvocationContext context) {
		return data.get(name);
	}

	public <T> boolean canGet(String name, Class<T> type,
			InvocationContext context) {
		return data.containsKey(name);
	}

	public boolean isCachable() {
		return cachable;
	}

	public void setCachable(boolean cachable) {
		this.cachable = cachable;
	}

	
}
