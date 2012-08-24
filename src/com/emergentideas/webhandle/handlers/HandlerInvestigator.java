package com.emergentideas.webhandle.handlers;

import com.emergentideas.webhandle.CallSpec;

/**
 * Finds methods to handle incoming requests.
 * @author kolz
 *
 */
public interface HandlerInvestigator {

	public CallSpec determineHandler(String requestURL, HttpMethod method);
}
