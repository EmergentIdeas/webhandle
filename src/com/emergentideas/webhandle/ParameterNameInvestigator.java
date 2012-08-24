package com.emergentideas.webhandle;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Determines the parameter name for argument to a method
 * @author kolz
 *
 */
public interface ParameterNameInvestigator {

	/**
	 * Determines the name of the parameter if possible.  Returns null if no determination can
	 * be made
	 * @param focus
	 * @param method
	 * @param parameterClass
	 * @param parameterAnnotations
	 * @param context
	 * @param argumentIndex The index of the argument within the method.  May be null if it can
	 * not be determined.
	 * @return
	 */
	public <T> String determineParameterName(Object focus, Method method, Class<T> parameterClass, 
			Annotation[] parameterAnnotations, InvocationContext context, Integer argumentIndex);
	
}
