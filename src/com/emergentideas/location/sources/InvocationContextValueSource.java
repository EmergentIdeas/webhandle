package com.emergentideas.location.sources;

import com.emergentideas.location.InvocationContext;
import com.emergentideas.location.ValueSource;

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
	
	
	
	

}
