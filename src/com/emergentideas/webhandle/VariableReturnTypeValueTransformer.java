package com.emergentideas.webhandle;

import java.util.Map;

import com.emergentideas.webhandle.exceptions.TransformationException;

/**
 * Includes an additional interface that may be used to see if the
 * return type is compatible since the actual type returned may
 * be a sub class of the declared type.
 * @author kolz
 *
 */
public interface VariableReturnTypeValueTransformer<R, S, T> extends
		ValueTransformer<R, S, T> {
	
	public boolean isReturnTypeCompatible(InvocationContext context, Map<String, R> transformationProperties, Class finalParameterClass, String parameterName, S ... source) throws TransformationException;


}
