package com.emergentideas.location.transformers;

import com.emergentideas.location.InvocationContext;
import com.emergentideas.location.ValueTransformer;

public class ArrayToObjectTransformer implements ValueTransformer {

	public Object transform(InvocationContext context, String parameterName, Object... source) {
		if(source != null && source.length != 0) {
			return source[0];
		}
		
		return null;
	}

	
}
