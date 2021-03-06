package com.emergentideas.webhandle.transformers;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.emergentideas.webhandle.InvocationContext;
import com.emergentideas.webhandle.ValueTransformer;

public class StringToBooleanTransformer implements ValueTransformer<String, String, Boolean[]> {

	
	public Boolean[] transform(InvocationContext context, Map<String, String> transformationProperties,
			Class finalParameterClass, String parameterName, String... source) {
		Boolean[] result = new Boolean[source.length];
		
		for(int i = 0; i < source.length; i++) {
			if(StringUtils.isBlank(source[i])) {
				result[i] = Boolean.FALSE;
			}
			result[i] = convert(source[i]);
		}
		
		return result;
	}
	
	protected Boolean convert(String d) {
		d = d.toLowerCase();
		if("0".equals(d) || "false".equals(d) || "no".equals(d)) {
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}

	
}
