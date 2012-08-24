package com.emergentideas.webhandle;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Determines which transformers should be called to transform the output value.  Generally,
 * instances will be called once per annotation in order that the annotations appear in the
 * source code.
 * @author kolz
 *
 */
public interface OutputTransformersInvestigator {

	/**
	 * Returns an array of CallSpec objects that should be applied to the output of a call.  They will be
	 * applied 1) if the applicable, 2) in the array order
	 * @param method The method which was called
	 * @param annotation The annotation currently in focus
	 * @param context An invocation context from which it's possible to get info about the call
	 * @return An array of transformer specifications to be applied to the output or null if this instance can suggest no transformers
	 */
	public CallSpec[] determineTransformers(Object focus, Method method, Annotation annotation, InvocationContext context);

}
