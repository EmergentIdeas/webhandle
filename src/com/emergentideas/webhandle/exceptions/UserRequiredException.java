package com.emergentideas.webhandle.exceptions;

/**
 * Thrown when the user has requested access to a resource which requires them to be
 * logged-in in some way but is not currently logged in at all.
 * @author kolz
 *
 */
public class UserRequiredException extends SecurityException {
	
	protected String requestedURL;
	
	public UserRequiredException() {}
	public UserRequiredException(String requestedURL) {
		this.requestedURL = requestedURL;
	}
	public String getRequestedURL() {
		return requestedURL;
	}
	public void setRequestedURL(String requestedURL) {
		this.requestedURL = requestedURL;
	}
	
	

}
