package com.emergentideas.webhandle.transformers;

import java.util.Map;

import com.emergentideas.webhandle.InvocationContext;
import com.emergentideas.webhandle.ValueTransformer;
import com.emergentideas.webhandle.exceptions.TransformationException;

public class ArrayToObjectTransformer implements ValueTransformer {

	public Object transform(InvocationContext context,	Map transformationProperties, Class finalParameterClass, 
			String parameterName,	Object... source) throws TransformationException {
		if(source != null && source.length != 0) {
			return source[0];
		}
		
		return null;
	}
}
