package com.emergentideas.webhandle.sources;

import java.util.Map;
import java.util.Properties;

import com.emergentideas.webhandle.InvocationContext;
import com.emergentideas.webhandle.ValueSource;

public class PropertiesValueSource implements ValueSource<Object> {
	
	protected Properties data;
	protected boolean cachable = true;
	
	public PropertiesValueSource(Properties data) {
		this.data = data;
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
