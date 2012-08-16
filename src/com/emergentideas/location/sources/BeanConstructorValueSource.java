package com.emergentideas.location.sources;

import com.emergentideas.location.InvocationContext;
import com.emergentideas.location.ValueSource;

public class BeanConstructorValueSource implements ValueSource<Object> {

	public <T> Object get(String name, Class<T> type, InvocationContext context) {
		try {
			T t = type.newInstance();
			return t;
		}
		catch(Exception e) {
			
		}
		return null;
	}

	public <T> boolean canGet(String name, Class<T> type,
			InvocationContext context) {
		return get(name, type, context) != null;
	}
	
	
	
	

}
