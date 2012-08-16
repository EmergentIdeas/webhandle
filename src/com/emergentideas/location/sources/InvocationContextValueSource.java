package com.emergentideas.location.sources;

import com.emergentideas.location.InvocationContext;
import com.emergentideas.location.ValueSource;

public class InvocationContextValueSource implements ValueSource<Object> {

	public <T> Object get(String name, Class<T> type, InvocationContext context) {
		return context.getFoundParameter(name, type);
	}

	public <T> boolean canGet(String name, Class<T> type, InvocationContext context) {
		return context.getFoundParameter(name, type) != null;
	}
	
	
	
	

}
