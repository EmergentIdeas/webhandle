package com.emergentideas.location;

import java.util.Map;

import com.emergentideas.location.exceptions.TransformationException;

/**
 * Can transform an object.  Sometimes between one class and another, sometimes just
 * a cleanup or security check.
 * @author kolz
 *
 * @param <R> The class of any transformation properties
 * @param <S> The source class
 * @param <T> The target class
 */
public interface ValueTransformer<R, S, T> {
	
	/**
	 * Transforms the input data <code>source</code> to the return value.
	 * @param context
	 * @param transformationProperties An optional set of properties that tells the transformer how to transform
	 * the source data.  A trivial example would be <code>precision => 3</code> that would tell a double to string
	 * transformer about how many digits to show.
	 * @param parameterName
	 * @param source
	 * @return
	 * @throws TransformationException
	 */
	public T transform(InvocationContext context, Map<String, R> transformationProperties, String parameterName, S ... source) throws TransformationException;
}
