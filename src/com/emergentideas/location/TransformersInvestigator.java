package com.emergentideas.location;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Determines which transformers should be applied to the source value before it is passed as an argument to a method.  Although it's 
 * up to the caller, the expectation would be that all of the transformers will get applied from all Transformer Investigators.
 * @author kolz
 *
 */
public interface TransformersInvestigator {
	
	/**
	 * Determines which transformers should be applied to the source value before it is passed as an argument to a method
	 * @param focus The object instance that method and argument belong to
	 * @param method The method that the argument belongs to
	 * @param parameterClass The class of the argument
	 * @param parameterName The name of the argument if known
	 * @param parameterAnnotations Any annotations for the argument
	 * @param context
	 * @return
	 */
	public <T> String[] determineTransformers(Object focus, Method method, Class<T> parameterClass, String parameterName, Annotation[] parameterAnnotations, InvocationContext context);

}
