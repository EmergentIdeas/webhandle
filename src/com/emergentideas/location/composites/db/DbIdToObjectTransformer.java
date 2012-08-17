package com.emergentideas.location.composites.db;

import java.util.Map;

import com.emergentideas.location.InvocationContext;
import com.emergentideas.location.ValueTransformer;
import com.emergentideas.location.exceptions.TransformationException;

public class DbIdToObjectTransformer implements ValueTransformer<String, String, Object> {

	public Object transform(InvocationContext context,
			Map<String, String> transformationProperties, String parameterName,
			String... source) throws TransformationException {
		if(source == null || source.length == 0) {
			return null;
		}
		
		return loadObject(source[0]);
	}

	protected Object loadObject(String id) {
		return null;
	}
	
}
