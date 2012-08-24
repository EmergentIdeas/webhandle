package com.emergentideas.webhandle.transformers;

import java.util.Map;

import com.emergentideas.webhandle.InvocationContext;
import com.emergentideas.webhandle.ValueTransformer;

public class NumberToStringTransformer implements ValueTransformer<String, Number, String[]> {

	public String[] transform(InvocationContext context, Map<String, String> transformationProperties, Class finalParameterClass, String parameterName, Number... source) {
		String[] result = new String[source.length];
		
		for(int i = 0; i < source.length; i++) {
			result[i] = source[i].toString();
		}
		
		return result;
	}
}
