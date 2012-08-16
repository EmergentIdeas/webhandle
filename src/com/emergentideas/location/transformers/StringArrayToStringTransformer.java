package com.emergentideas.location.transformers;

import com.emergentideas.location.InvocationContext;
import com.emergentideas.location.ValueTransformer;

public class StringArrayToStringTransformer implements ValueTransformer<String, String> {

	
	public String transform(InvocationContext context, String parameterName, String... source) {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < source.length; i++) {
			sb.append(source[i]);
			if(i < source.length - 1) {
				sb.append(',');
			}
		}
		
		return sb.toString();
	}

	

}
