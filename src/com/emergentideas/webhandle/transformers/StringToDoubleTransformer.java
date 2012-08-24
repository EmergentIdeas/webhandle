package com.emergentideas.webhandle.transformers;

public class StringToDoubleTransformer extends StringToNumberTransformerBase<Double> {

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
