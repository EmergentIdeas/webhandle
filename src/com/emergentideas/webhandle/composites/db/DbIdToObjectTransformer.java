package com.emergentideas.webhandle.composites.db;

import java.util.Map;

import com.emergentideas.webhandle.InvocationContext;
import com.emergentideas.webhandle.ValueTransformer;
import com.emergentideas.webhandle.exceptions.TransformationException;

public class DbIdToObjectTransformer implements ValueTransformer<String, String, Object> {

	public Object transform(InvocationContext context,
			Map<String, String> transformationProperties, Class finalParameterClass, String parameterName,
			String... source) throws TransformationException {
		if(source == null || source.length == 0) {
			return null;
		}
		
		return loadObject(source[0], finalParameterClass);
	}

	protected Object loadObject(String id, Class finalParameterClass) {
		return null;
	}
	
}
