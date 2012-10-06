package com.emergentideas.webhandle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import com.emergentideas.logging.Logger;
import com.emergentideas.logging.SystemOutLogger;

public class AppLocation implements Location {
	
	// Storage for the "current" objects.
	protected List<Object> currentObjects;
	
	// Storage for objects accessible by key
	protected Map<String, Object> keyedObjects;
	
	protected Location parent;
	
	List<PropertyAccessor> accessors = null;
	
	protected Logger log;
	
	public AppLocation() {
		log = SystemOutLogger.get(this.getClass());
	}
	
	public AppLocation(Location parent) {
		this();
		this.parent = parent;
	}
	
	
	
	public Object get(String path) {
		return get(path, new InvocationContext(this));
	}

	public List<Object> all(String path) {
		return all(path, new InvocationContext(this));
	}

	public Object get(String path, InvocationContext context) {
		List<Object> all = all(path, context);
		if(all.size() > 0) {
			return all.get(0);
		}
		return null;
	}

	public List<Object> all(String path, InvocationContext context) {
		if("$this".equals(path) || "this".equals(path)) {
			if(currentObjects == null) {
				if(parent != null) {
					return parent.all(path, context);
				}
				return new ArrayList<Object>();
			}
			else if(currentObjects.size() > 0) {
				List<Object> data = new ArrayList<Object>();
				data.add(currentObjects.get(0));
				return(data);
			}
		}
		
		List<Object> results = new ArrayList<Object>();
		Collection<Object> candidates = getRootObjectsToConsider();
		
		boolean useOnlyOneCandidate = true;
		
		Stack<String> pathParts = pathToStack(path);
		while(pathParts.isEmpty() == false) {
			String qualifiedPart = pathParts.pop();
			String propertyName = getPropertyNameFromPathSegment(qualifiedPart);
			boolean keepCollections = isKeepCollections(qualifiedPart);
			
			candidates = extractNextStageObjects(candidates, propertyName, useOnlyOneCandidate);
			if(keepCollections == false) {
				expandCollections(candidates);
			}
			
			if(pathParts.isEmpty() == false) {
				// if there are more parts in the path and any of the candidates are Locations, 
				// process the locations for remaining parts
				String remainingPath = stackToPath(pathParts);
				processLocationsInCandidates(candidates, results, remainingPath, context);
			}
			
			// The first round we only want to use one object instead of all the current and named objects
			// Subsequent rounds we'll want to pick up everything
			useOnlyOneCandidate = false;
			
			if(candidates.isEmpty()) {
				// No need to keep processing parts if their are no more candidates
				break;
			}
		}
		
		results.addAll(candidates);
		
		if(results.isEmpty() == false) {
			// awesome, we've got results so we can return them
			return results;
		}
		
		if(parent != null) {
			return(parent.all(path, context));
		}
		
		return new ArrayList<Object>();
	}
	
	/**
	 * Expands any Collection objects in the candidates so that their non-null members are added
	 * to the candidates collection and removes any nulls.
	 * @param candidates
	 */
	protected void expandCollections(Collection<Object> candidates) {
		List<Object> toAdd = new ArrayList<Object>();
		
		Iterator<Object> it = candidates.iterator();
		while(it.hasNext()) {
			Object o = it.next();
			if(o == null) {
				it.remove();
			}
			else if(o instanceof Collection) {
				toAdd.addAll(getNonNulls((Collection)o));
				it.remove();
			}
		}
		
		candidates.addAll(toAdd);
	}
	
	/**
	 * Returns a new collection with all of the non-null members
	 * @param col
	 * @return
	 */
	protected Collection<Object> getNonNulls(Collection<Object> col) {
		List<Object> nonNull = new ArrayList<Object>();
		for(Object o : col) {
			if(o != null) {
				nonNull.add(o);
			}
		}
		
		return nonNull;
	}
	
	/**
	 * Returns true if the path segment indicates we should keep collections as the values
	 * instead of adding all of the collection members to the candidates for the next round
	 * @param pathSegment
	 * @return
	 */
	protected boolean isKeepCollections(String pathSegment) {
		return pathSegment.startsWith("@");
	}
	
	/**
	 * Sometimes a path segment can be prefixed by an @ meaning we should use
	 * the array instead of the values of the array.  This method returns only
	 * the property name portion of the path part.
	 * @param pathSegment
	 * @return
	 */
	protected String getPropertyNameFromPathSegment(String pathSegment) {
		if(isKeepCollections(pathSegment)) {
			return pathSegment.substring(1);
		}
		return pathSegment;
	}
	
	/**
	 * If there are more parts in the path and any of the candidates are Locations, 
	 * process the locations for remaining parts
	 * @param candidates
	 * @param results
	 * @param path
	 * @param context
	 */
	protected void processLocationsInCandidates(Collection<Object> candidates, List<Object> results, String path, InvocationContext context) {
		Iterator<Object> itCandidates = candidates.iterator();
		while(itCandidates.hasNext()) {
			Object o = itCandidates.next();
			if(o instanceof Location) {
				// We don't need to process it in the next round if it's a Location
				itCandidates.remove();
				
				Location loc = (Location)o;
				results.addAll(loc.all(path, context));
			}
		}
	}
	
	/**
	 * Returns the root set of objects to consider.  This will be the hash of named objects and all
	 * of the current objects.
	 * @return
	 */
	protected Collection<Object> getRootObjectsToConsider() {
		List<Object> result = new ArrayList<Object>();
		if(keyedObjects != null) {
			result.add(keyedObjects);
		}
		
		if(currentObjects != null) {
			result.addAll(currentObjects);
		}
		
		return result;
	}
	
	/**
	 * Given a collection of candidate objects, extract and return the set of any objects
	 * that can be found at <code>key</code>.  Key may have a different meaning for each type
	 * of candidate object.  It may always mean a bean property or method but may also mean
	 * a value if the candidate is a map, etc.  If a candidate is a Location it will NOT
	 * be processed because the remaining key should be given to the Location for processing.
	 * @param candidates The candidates to search for next state objects
	 * @param key The property name to search for
	 * @param useOnlyOneCandidate If true, the first candidate to have something found will be the only
	 * candidate used.  This allows us to search named objects and current objects but stop after finding
	 * one with a matching property.
	 * @return
	 */
	protected List<Object> extractNextStageObjects(Collection<Object> candidates, String key, boolean useOnlyOneCandidate) {
		return extractNextStageObjects(candidates, key, useOnlyOneCandidate, getPropertyAccessorList());
	}
	
	/**
	 * Given a collection of candidate objects, extract and return the set of any objects
	 * that can be found at <code>key</code>.  Key may have a different meaning for each type
	 * of candidate object.  It may always mean a bean property or method but may also mean
	 * a value if the candidate is a map, etc.  If a candidate is a Location it will NOT
	 * be processed because the remaining key should be given to the Location for processing.
	 * @param candidates
	 * @param key
	 * @param useOnlyOneCandidate If true, the first candidate to have something found will be the only
	 * candidate used.  This allows us to search named objects and current objects but stop after finding
	 * one with a matching property.
	 * @param accessors The list of accessors that will be used to find the next stage objects
	 * @return
	 */
	protected List<Object> extractNextStageObjects(Collection<Object> candidates, String key, boolean useOnlyOneCandidate, List<PropertyAccessor> accessors) {
		List<Object> results = new ArrayList<Object>();
		
		candidates: for(Object candidate : candidates) {
			accessors: for(PropertyAccessor accessor : accessors) {
				if(accessor.canAccess(candidate, key)) {
					try {
						Object obj = accessor.get(candidate, key);
						if(obj != null) {
							results.add(obj);
							if(useOnlyOneCandidate) {
								// we only want to use one candidate so we'll stop searching the others
								break candidates;
							}
							else {
								// If we've found a property with one accessor, stop looking
								continue candidates;
							}
						}
					}
					catch(Throwable t) {
						log.error("Exception when fetching property for key: " + key, t);
					}
				}
			}
		}
		
		return results;
	}
	
	protected List<PropertyAccessor> getPropertyAccessorList() {
		if(accessors == null) {
			accessors = Collections.synchronizedList(new ArrayList<PropertyAccessor>());
			accessors.add(new MapPropertyAccessor());
			accessors.add(new JavaBeanPropertyAccessor());
		}
		return accessors;
	}
	
	/**
	 * Converts a path into a stack of parts.  A path like <code>one/two/three</code> 
	 * would be transformed into a stack like:
	 * <pre>
	 * one
	 * two
	 * three
	 * </pre>
	 * where the first pop operation would return "one".
	 * @param path The front slash separated path to convert to a stack
	 * @return
	 */
	protected Stack<String> pathToStack(String path) {
		Stack<String> result = new Stack<String>();
		String[] parts = splitPathToParts(path);
		
		for(int i = 0; i < parts.length; i++) {
			result.add(0, parts[i]);
		}
		
		return result;
	}
	
	/**
	 * Creates a path from a stack.  The inverse of <code>pathToStack</code>.
	 * @param parts
	 * @return
	 */
	protected String stackToPath(Stack<String> parts) {
		String path = "";
		if(parts.isEmpty()) {
			return path;
		}
		
		for(String part : parts) {
			path = "/" + part + path;
		}
		
		return path.substring(1);
	}
	
	/**
	 * Splits a front slash separated path into an array of Strings.
	 * @param path
	 * @return
	 */
	protected String[] splitPathToParts(String path) {
		return path.split("/");
	}

	public void put(String key, Object value) {
		getKeyedObjects().put(key, value);
	}

	public void setParent(Location parent) {
		this.parent = parent;
	}

	public void add(Object current) {
		getCurrentObjects().add(current);
	}

	protected List<Object> getCurrentObjects() {
		if(currentObjects == null) {
			currentObjects = Collections.synchronizedList(new ArrayList<Object>());
		}
		return currentObjects;
	}
	
	protected Map<String, Object> getKeyedObjects() {
		if(keyedObjects == null) {
			keyedObjects = Collections.synchronizedMap(new HashMap<String, Object>());
		}
		return keyedObjects;
	}
}
