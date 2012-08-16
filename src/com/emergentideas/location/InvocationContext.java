package com.emergentideas.location;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class InvocationContext {

	// The topmost location used for this set of invocations
	protected Location location;
	
	// a map holding the objects that we've found already.
	// the key is in the form of <parameterName>:<className>
	protected Map<String, Object> foundParameterMap = Collections.synchronizedMap(new HashMap<String, Object>());
	
	protected static final String UNNAMED_PARAMETER_NAME = "IAMTHEUNNAMED";

	public InvocationContext() {
		
	}
	
	public InvocationContext(Location location) {
		this.location = location;
	}
	
	
	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}
	
	public <E> E getFoundParameter(String parameterName, Class<E> type) {
		return (E)foundParameterMap.get(getFoundParameterMapKey(parameterName, type)); 
	}
	
	public <E> E getFoundParameter(Class<E> type) {
		return (E)foundParameterMap.get(getFoundParameterMapKey(UNNAMED_PARAMETER_NAME, type)); 
	}
	
	public <E, F extends E> void setFoundParameter(String parameterName, Class<E> type, F value) {
		foundParameterMap.put(getFoundParameterMapKey(parameterName, type), value);
	}
	
	public <E, F extends E> void setFoundParameter(Class<E> type, F value) {
		foundParameterMap.put(getFoundParameterMapKey(UNNAMED_PARAMETER_NAME, type), value);
	}
	
	protected <T> String getFoundParameterMapKey(String parameterName, Class<T> type) {
		if(parameterName == null) {
			parameterName = UNNAMED_PARAMETER_NAME;
		}
		return parameterName + ":" + type.getName();
	}
}
