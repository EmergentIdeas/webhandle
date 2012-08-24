package com.emergentideas.webhandle.output;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Respondent {

	/**
	 * Does the actual response for an Http request usually relying on information already created.
	 * @param servletContext
	 * @param request
	 * @param response
	 */
	public void respond(ServletContext servletContext, HttpServletRequest request, HttpServletResponse response);
}
