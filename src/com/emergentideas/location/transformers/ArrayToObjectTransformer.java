package com.emergentideas.location.transformers;

import java.util.Map;

import com.emergentideas.location.InvocationContext;
import com.emergentideas.location.ValueTransformer;
import com.emergentideas.location.exceptions.TransformationException;

public class ArrayToObjectTransformer implements ValueTransformer {

	public Object transform(InvocationContext context,	Map transformationProperties, String parameterName,	Object... source) throws TransformationException {
		if(source != null && source.length != 0) {
			return source[0];
		}
		
		return null;
	}
}
