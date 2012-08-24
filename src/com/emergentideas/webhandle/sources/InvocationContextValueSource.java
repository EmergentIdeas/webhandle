package com.emergentideas.webhandle.sources;

import com.emergentideas.webhandle.InvocationContext;
import com.emergentideas.webhandle.ValueSource;

/**
 * Returns an object stored in the invocation context either by name and type or by
 * type alone.
 * @author kolz
 *
 */
public class InvocationContextValueSource implements ValueSource<Object> {

	public <T> Object get(String name, Class<T> type, InvocationContext context) {
		return context.getFoundParameter(name, type);
	}

	public <T> boolean canGet(String name, Class<T> type, InvocationContext context) {
		return context.getFoundParameter(name, type) != null;
	}

	public boolean isCachable() {
		return true;
	}
	
	
	
	

}
