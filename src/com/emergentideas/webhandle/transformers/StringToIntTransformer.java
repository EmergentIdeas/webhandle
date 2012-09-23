package com.emergentideas.webhandle.transformers;

import java.util.Map;

import com.emergentideas.webhandle.InvocationContext;
import com.emergentideas.webhandle.ValueTransformer;

public class StringToIntTransformer implements
		ValueTransformer<String, String, int[]> {

	public int[] transform(InvocationContext context,
			Map<String, String> transformationProperties,
			Class finalParameterClass, String parameterName, String... source) {

		int[] result = new int[source.length];

		for (int i = 0; i < source.length; i++) {
			result[i] = convert(source[i]);
		}

		return result;
	}

	protected int convert(String number) {
		try {
			return Integer.parseInt(number);
		} catch (Exception e) {
			// couldn't convert.
		}
		try {
			// let's try it as a double
			return (int) Double.parseDouble(number);
		} catch (Exception e) {
			// couldn't convert.
		}

		return 0;
	}

}
