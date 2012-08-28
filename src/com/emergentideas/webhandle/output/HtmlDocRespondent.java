package com.emergentideas.webhandle.output;

import java.io.IOException;

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

		try {
			response.getOutputStream().write(output.getStream("body").toString().getBytes("UTF-8"));
			
		}
		catch(IOException e) {
			
		}
	}

}
