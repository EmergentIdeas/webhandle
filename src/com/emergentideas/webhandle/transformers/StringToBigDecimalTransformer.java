package com.emergentideas.webhandle.transformers;

import java.math.BigDecimal;
import java.util.Map;

import com.emergentideas.webhandle.InvocationContext;

public class StringToBigDecimalTransformer extends StringToNumberTransformerBase<BigDecimal> {

	
	@Override
	public BigDecimal[] transform(InvocationContext context,
			Map<String, String> transformationProperties,
			Class finalParameterClass, String parameterName, String... source) {
		return super.transform(context, transformationProperties, finalParameterClass,
				parameterName, source);
	}

	@Override
	protected BigDecimal convert(String number) {
		try {
			// let's try it as a BigDecimal
			return new BigDecimal(number);
		}
		catch(Exception e) {
			// couldn't convert.
		}
		
		return null;
	}
	
	

}
