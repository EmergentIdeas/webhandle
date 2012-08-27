package com.emergentideas.webhandle;

import java.lang.reflect.Method;

import com.emergentideas.webhandle.output.Respondent;

/**
 * Determines what the response will be to a call.  There will be only one of these in the life cycle responsible
 * for determining what the output will be.  It may delegate, but there was no easy way to set up a system that 
 * would allow multiple investigators to cooperate.
 * @author kolz
 *
 */
public interface OutputResponseInvestigator {

	/**
	 * Returns a {@link Respondent} that should be applied to the output of a call.  
	 * @param context An invocation context from which it's possible to get info about the call
	 * @param focus The object which was called to handle the request
	 * @param method The method which was called to handle the request
	 * @return An object which can format the response from the handler
	 */
	public Respondent determineTransformers(InvocationContext context, Object focus, Method method, Object response);

}
