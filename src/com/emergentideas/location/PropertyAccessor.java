package com.emergentideas.location;

public interface PropertyAccessor {

	public Object get(Object focus, String propertyName);
	
	public boolean canAccess(Object focus, String propertyName);
}
