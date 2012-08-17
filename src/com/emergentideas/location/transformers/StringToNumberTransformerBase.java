package com.emergentideas.location.transformers;

import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

import com.emergentideas.location.InvocationContext;
import com.emergentideas.location.ValueTransformer;

public abstract class StringToNumberTransformerBase<T> implements ValueTransformer<String, String, T[]> {

	public T[] transform(InvocationContext context, Map<String, String> transformationProperties, String parameterName, String... source) {
		
		T[] result = createArray(source.length);
		
		for(int i = 0; i < source.length; i++) {
			result[i] = convert(source[i]);
		}
		
		return result;
	}
	
	protected T[] createArray(int length) {
		Type type = ((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		return (T[])Array.newInstance((Class)type, length);
	}
	
	protected abstract T convert(String number);
}
