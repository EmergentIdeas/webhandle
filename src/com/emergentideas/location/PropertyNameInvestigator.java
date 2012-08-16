package com.emergentideas.location;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Determines the parameter name for argument to a method
 * @author kolz
 *
 */
public interface PropertyNameInvestigator {

	public <T> String determinePropertyName(Object focus, Method method, Class<T> parameterClass, Annotation[] parameterAnnotations, InvocationContext context);
	
}
