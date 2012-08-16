package com.emergentideas.location.transformers;

public class StringToIntegerTransformer extends
		StringToNumberTransformerBase<Integer> {

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
