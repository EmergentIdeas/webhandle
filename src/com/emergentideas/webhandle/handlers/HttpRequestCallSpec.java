package com.emergentideas.webhandle.handlers;

import com.emergentideas.webhandle.CallSpec;

public class HttpRequestCallSpec extends CallSpec {
	
	protected HttpMethod[] allowedMethods;

	public HttpMethod[] getAllowedMethods() {
		return allowedMethods;
	}

	public void setAllowedMethods(HttpMethod[] allowedMethods) {
		this.allowedMethods = allowedMethods;
	}
	
	

}
