package com.emergentideas.location;

import com.emergentideas.location.exceptions.TransformationException;

/**
 * Can transform an object.  Sometimes between one class and another, sometimes just
 * a cleanup or security check.
 * @author kolz
 *
 * @param <S> The source class
 * @param <T> The target class
 */
public interface ValueTransformer<S, T> {
	
	public T transform(InvocationContext context, String parameterName, S ... source) throws TransformationException;
}
