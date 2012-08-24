package com.emergentideas.webhandle.transformers;

import java.util.Map;

import com.emergentideas.webhandle.InvocationContext;
import com.emergentideas.webhandle.ValueTransformer;

public class StringArrayToStringTransformer implements ValueTransformer<String, String, String> {

	
	public String transform(InvocationContext context, Map<String, String> transformationProperties, 
			Class finalParameterClass, String parameterName, String... source) {
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
