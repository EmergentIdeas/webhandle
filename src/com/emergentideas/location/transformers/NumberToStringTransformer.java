package com.emergentideas.location.transformers;

import com.emergentideas.location.InvocationContext;
import com.emergentideas.location.ValueTransformer;

public class NumberToStringTransformer implements ValueTransformer<Number, String[]> {

	public String[] transform(InvocationContext context, String parameterName, Number... source) {
		String[] result = new String[source.length];
		
		for(int i = 0; i < source.length; i++) {
			result[i] = source[i].toString();
		}
		
		return result;
	}
}
