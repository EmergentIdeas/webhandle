package com.emergentideas.webhandle.output;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HtmlDocRespondent implements Respondent {

	protected SegmentedOutput output;
	
	public HtmlDocRespondent(SegmentedOutput output) {
		this.output = output;
	}
	
	public void respond(ServletContext servletContext,
			HttpServletRequest request, HttpServletResponse response) {

	}

}
