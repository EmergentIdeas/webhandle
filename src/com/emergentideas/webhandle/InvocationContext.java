package com.emergentideas.webhandle;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class InvocationContext {

	// The topmost location used for this set of invocations
	protected Location location;
	
	// a map holding the objects that we've found already.
	// the key is in the form of <parameterName>:<className>
	protected Map<String, Object> foundParameterMap;
	
	protected static final String UNNAMED_PARAMETER_NAME = "IAMTHEUNNAMED";
	protected static final String thisInvocationContextName = UNNAMED_PARAMETER_NAME + ":" + InvocationContext.class.getName();
	

	public InvocationContext() {
	}
	
	public InvocationContext(Location location) {
		this();
		this.location = location;
	}
	
	protected void createFoundParameterMap() {
		if(foundParameterMap == null) {
			foundParameterMap = Collections.synchronizedMap(new HashMap<String, Object>());
			setFoundParameter(InvocationContext.class, this);
		}
	}
	
	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}
	
	public <E> E getFoundParameter(String parameterName, Class<E> type) {
		String parameterNameCombined = getFoundParameterMapKey(parameterName, type);
		if(foundParameterMap == null) {
			if(thisInvocationContextName.equals(parameterNameCombined)) {
				return (E)this;
			}
			return null;
		}
		return (E)foundParameterMap.get(parameterNameCombined); 
	}
	
	public <E> E getFoundParameter(Class<E> type) {
		if(foundParameterMap == null) {
			if(type.equals(InvocationContext.class)) {
				return (E)this;
			}
			return null;
		}
		return (E)foundParameterMap.get(getFoundParameterMapKey(UNNAMED_PARAMETER_NAME, type)); 
	}
	
	public <E, F extends E> void setFoundParameter(String parameterName, Class<E> type, F value) {
		createFoundParameterMap();
		foundParameterMap.put(getFoundParameterMapKey(parameterName, type), value);
	}
	
	public <E, F extends E> void setFoundParameter(Class<E> type, F value) {
		createFoundParameterMap();
		foundParameterMap.put(getFoundParameterMapKey(UNNAMED_PARAMETER_NAME, type), value);
	}
	
	protected <T> String getFoundParameterMapKey(String parameterName, Class<T> type) {
		if(parameterName == null) {
			parameterName = UNNAMED_PARAMETER_NAME;
		}
		return parameterName + ":" + type.getName();
	}
}
