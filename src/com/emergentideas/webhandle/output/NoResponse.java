package com.emergentideas.webhandle.output;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class NoResponse implements Respondent {

	public void respond(ServletContext servletContext,
			HttpServletRequest request, HttpServletResponse response) {
		// Left blank because the response has already been made an no further action is needed

	}

}
