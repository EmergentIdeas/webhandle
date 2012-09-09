package com.emergentideas.webhandle.handlers;

import com.emergentideas.webhandle.CallSpec;
import com.emergentideas.webhandle.exceptions.CouldNotHandleException;

/**
 * Finds methods to handle incoming requests.
 * @author kolz
 *
 */
public interface HandlerInvestigator {

	/**
	 * Causes the investigator to look at an object to determine if it many have handlers that could be invoked.
	 * @param handler
	 */
	public void analyzeObject(Object handler);
	
	
	
	/**
	 * Finds an ordered set of call specs which are the handlers that could handle a request
	 * for the specified url.  There are certain circumstances, like a handler throwing a
	 * {@link CouldNotHandleException} where we would want to try subsequent handlers instead
	 * of going to an error handler.
	 * @param requestURL The url requested by the user
	 * @param method The http method of the request.
	 * @return Will always return a list of handlers although that list may be empty.
	 */
	public CallSpec[] determineHandlers(String requestURL, HttpMethod method);
}
