package com.emergentideas.utils;

import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;

public class OrderedProperties extends Properties {

	protected Vector names;

	public OrderedProperties() {
		super();

		names = new Vector();
	}

	public Enumeration<Object> propertyNames() {
		return names.elements();
	}
	
	

	@Override
	public Set<Object> keySet() {
		LinkedHashSet<Object> result = new LinkedHashSet<Object>();
		result.addAll(names);
		return result;
	}

	public Object put(Object key, Object value) {
		if (names.contains(key)) {
			names.remove(key);
		}

		names.add(key);

		return super.put(key, value);
	}

	public Object remove(Object key) {
		names.remove(key);

		return super.remove(key);
	}

}
