package com.emergentideas.location;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Determines which sources should be used to draw from for the parameter in question.  Although it is
 * caller determined, the expectation is that the first SourceSetInvestigator to return a non-null
 * will be the only one used.
 * @author kolz
 *
 */
public interface SourceSetInvestigator {
	
	/**
	 * Returns the sources which can be used to draw raw info from.  Returning null would indicate that any 
	 * source could be used.
	 * @param focus The object instance that method and argument belong to
	 * @param method The method that the argument belongs to
	 * @param parameterClass The class of the argument
	 * @param parameterName The name of the argument if known
	 * @param parameterAnnotations Any annotations for the argument
	 * @param context
	 * @return
	 */
	public <T> String[] determineAllowedSourceSets(Object focus, Method method, Class<T> parameterClass, String parameterName, Annotation[] parameterAnnotations, InvocationContext context);
}
