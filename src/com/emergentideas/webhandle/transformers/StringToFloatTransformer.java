package com.emergentideas.webhandle.transformers;

import java.util.Map;

import com.emergentideas.webhandle.InvocationContext;

public class StringToFloatTransformer extends StringToNumberTransformerBase<Float> {

	
	@Override
	public Float[] transform(InvocationContext context,
			Map<String, String> transformationProperties,
			Class finalParameterClass, String parameterName, String... source) {
		return super.transform(context, transformationProperties, finalParameterClass,
				parameterName, source);
	}

	@Override
	protected Float convert(String number) {
		try {
			// let's try it as a float
			return Float.parseFloat(number);
		}
		catch(Exception e) {
			// couldn't convert.
		}
		
		return null;
	}
	
	

}
