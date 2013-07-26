package com.emergentideas.webhandle.transformers;

import java.util.Map;

import com.emergentideas.webhandle.InvocationContext;

public class StringToIntegerTransformer extends
		StringToNumberTransformerBase<Integer> {

	
	@Override
	public Integer[] transform(InvocationContext context,
			Map<String, String> transformationProperties,
			Class finalParameterClass, String parameterName, String... source) {
		return super.transform(context, transformationProperties, finalParameterClass,
				parameterName, source);
	}

	@Override
	protected Integer convert(String number) {
		try {
			return Integer.parseInt(number);
		}
		catch(Exception e) {
			// couldn't convert.
		}
		try {
			// let's try it as a double
			return (int)Double.parseDouble(number);
		}
		catch(Exception e) {
			// couldn't convert.
		}
		
		return null;
	}
	
	

}
