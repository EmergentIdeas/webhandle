package com.emergentideas.webhandle.transformers;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.emergentideas.utils.ReflectionUtils;
import com.emergentideas.webhandle.InvocationContext;
import com.emergentideas.webhandle.ValueTransformer;
import com.emergentideas.webhandle.VariableReturnTypeValueTransformer;
import com.emergentideas.webhandle.exceptions.TransformationException;

public class StringToEnumTransformer implements VariableReturnTypeValueTransformer<String, String, Enum[]> {

	public Enum[] transform(InvocationContext context,
			Map<String, String> transformationProperties,
			Class finalParameterClass, String parameterName, String... source)
			throws TransformationException {
		List l = new ArrayList();
		
		if(isFinalClassOkay(finalParameterClass)) {
			if(ReflectionUtils.isArrayStyle(finalParameterClass)) {
				finalParameterClass = ReflectionUtils.getNonArrayStyle(finalParameterClass);
			}
			for(String item : source) {
				try {
					Enum e = Enum.valueOf(finalParameterClass, item);
					l.add(e);
				}
				catch(Exception e) {}
			}
		}
		
		if(l.isEmpty()) {
			return null;
		}
		
		Enum[] args = (Enum[])Array.newInstance(finalParameterClass, l.size());
		l.toArray(args);
		return (Enum[])args;
	}

	public boolean isReturnTypeCompatible(InvocationContext context,
			Map<String, String> transformationProperties,
			Class finalParameterClass, String parameterName, String... source)
			throws TransformationException {
		return isFinalClassOkay(finalParameterClass);
	}
	
	protected boolean isFinalClassOkay(Class finalParameterClass) {
		return Enum.class.isAssignableFrom(finalParameterClass) || Enum[].class.isAssignableFrom(finalParameterClass);
	}

	

}
