package com.emergentideas.location;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Determines the parameter name for argument to a method
 * @author kolz
 *
 */
public interface ParameterNameInvestigator {

	public <T> String determineParameterName(Object focus, Method method, Class<T> parameterClass, Annotation[] parameterAnnotations, InvocationContext context);
	
}
