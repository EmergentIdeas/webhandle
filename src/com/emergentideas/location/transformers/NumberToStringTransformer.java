package com.emergentideas.location.transformers;

import java.util.Map;

import com.emergentideas.location.InvocationContext;
import com.emergentideas.location.ValueTransformer;

public class NumberToStringTransformer implements ValueTransformer<String, Number, String[]> {

	public String[] transform(InvocationContext context, Map<String, String> transformationProperties, String parameterName, Number... source) {
		String[] result = new String[source.length];
		
		for(int i = 0; i < source.length; i++) {
			result[i] = source[i].toString();
		}
		
		return result;
	}
}
