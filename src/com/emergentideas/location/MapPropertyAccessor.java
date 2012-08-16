package com.emergentideas.location;

import java.util.Map;

public class MapPropertyAccessor implements PropertyAccessor {

	/**
	 * Returns the map value with the key the same as <code>propertyName</code>.
	 */
	public Object get(Object focus, String propertyName) {
		if(canAccess(focus, propertyName)) {
			return ((Map)focus).get(propertyName);
		}
		return null;
	}

	/**
	 * Returns true if the focus is a Map and the Map has a key with the property name 
	 * (even if the value is null). 
	 */
	public boolean canAccess(Object focus, String propertyName) {
		if(focus == null) {
			return false;
		}
		
		if(focus instanceof Map) {
			return ((Map)focus).containsKey(propertyName);
		}
		return false;
	}

}
