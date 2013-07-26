package com.emergentideas.webhandle.transformers;

import java.util.Map;

import com.emergentideas.webhandle.InvocationContext;

public class StringToDoubleTransformer extends StringToNumberTransformerBase<Double> {

	
	@Override
	public Double[] transform(InvocationContext context,
			Map<String, String> transformationProperties,
			Class finalParameterClass, String parameterName, String... source) {
		return super.transform(context, transformationProperties, finalParameterClass,
				parameterName, source);
	}

	@Override
	protected Double convert(String number) {
		try {
			// let's try it as a double
			return Double.parseDouble(number);
		}
		catch(Exception e) {
			// couldn't convert.
		}
		
		return null;
	}
	
	

}
