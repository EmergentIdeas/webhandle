package com.emergentideas.webhandle.output;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Causes the user's browser to load the url specified as a new request.
 * @author kolz
 *
 */
public class Show implements Respondent {

	protected String url;
	
	public Show() {
		
	}
	
	public Show(String url) {
		this();
		setUrl(url);
	}
	
	public void respond(ServletContext servletContext,
			HttpServletRequest request, HttpServletResponse response) {
		
		String direct = url;
		if(url.startsWith("/") && url.startsWith(request.getContextPath()) == false) {
			direct = request.getContextPath() + url;
		}
		response.setStatus(HttpServletResponse.SC_SEE_OTHER);
		response.addHeader("Location", direct);
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	
}
