package com.emergentideas.webhandle;

import java.util.List;

public interface Location {
	
	/**
	 * Gets the first object located at <code>path</code>
	 * @param path A forward slash separated path to the data
	 * @return
	 */
	public Object get(String path);
	
	/**
	 * Gets all of the objects located at <code>path</code>
	 * @param path A forward slash separated path to the data
	 * @return
	 */
	public List all(String path);
	
	/**
	 * Gets the first object located at <code>path</code>
	 * @param path A forward slash separated path to the data
	 * @param context A structure that holds all of the information about the current call
	 * including the topmost location
	 * @return
	 */
	public Object get(String path, InvocationContext context);
	
	/**
	 * Gets all of the objects located at <code>path</code>
	 * @param path A forward slash separated path to the data
	 * @param context A structure that holds all of the information about the current call
	 * including the topmost location
	 * @return
	 */
	public List<Object> all(String path, InvocationContext context);
	
	/**
	 * Puts an object (<code>value</code>) at the location specified (<code>key</code>).
	 * @param key The location of the new object
	 * @param value The new object.
	 */
	public void put(String key, Object value);
	
	/**
	 * The parent of the Location to be checked when additional data is needed
	 * or data at a path is not found.
	 * @param parent The parent Location
	 */
	public void setParent(Location parent);
	
	/**
	 * Adds a current object to be examined when there are no key matches.
	 * @param current
	 */
	public void add(Object current);
}
