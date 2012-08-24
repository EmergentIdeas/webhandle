package com.emergentideas.webhandle;

/**
 * An interface that allows objects to be created that can access properties in different ways
 * for different object types.
 * @author kolz
 *
 */
public interface PropertyAccessor {

	public Object get(Object focus, String propertyName);
	
	public boolean canAccess(Object focus, String propertyName);
}
