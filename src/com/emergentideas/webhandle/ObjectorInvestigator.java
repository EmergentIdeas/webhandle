package com.emergentideas.webhandle;

import java.lang.reflect.Method;

/**
 * Returns a {@link CallSpec} which can be called to throw a SecurityException
 * in the case that a method should be run.
 * @author kolz
 *
 */
public interface ObjectorInvestigator {

	public CallSpec determineObjector(Object focus, Method method, InvocationContext context);
}
