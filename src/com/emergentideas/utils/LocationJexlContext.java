package com.emergentideas.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.jexl2.JexlContext;

import com.emergentideas.webhandle.Location;

public class LocationJexlContext implements JexlContext {
	
	protected Location inner;
	protected Map<String,String> variableReplacements;
	
	public LocationJexlContext(Location inner) {
		this(inner, null);
	}

	public LocationJexlContext(Location inner, Map<String,String> variableReplacements) {
		this.inner = inner;
		this.variableReplacements = variableReplacements;
	}

	public Object get(String name) {
		List<Object> result;
		if(name.startsWith("@")) {
			result = inner.all(getVariableName(name.substring(1)));
		}
		else if(name.startsWith("$")) {
			return inner.get(getVariableName(name.substring(1)));
		}
		else {
			result = inner.all(getVariableName(name));
		}
		if(result.size() == 1) {
			return result.get(0);
		}
		return result;
	}
	
	protected String getVariableName(String startingVariableName) {
		if(variableReplacements == null) {
			return startingVariableName;
		}
		if(variableReplacements.containsKey(startingVariableName)) {
			return variableReplacements.get(startingVariableName);
		}
		return startingVariableName;
	}

	public void set(String name, Object value) {
		inner.put(name, value);
	}

	public boolean has(String name) {
		return get(name) != null;
	}

}
