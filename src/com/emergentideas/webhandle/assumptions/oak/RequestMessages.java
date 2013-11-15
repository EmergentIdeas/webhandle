package com.emergentideas.webhandle.assumptions.oak;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.emergentideas.webhandle.Location;

/**
 * Holds information about the request that the handler wants to convey to the user.
 * @author kolz
 *
 */
public class RequestMessages {

	public static final String SESSION_LOCATION_KEY = "previousUserMessages";
	
	protected Map<String, FieldError> fieldErrors = new HashMap<String, FieldError>();
	
	protected List<String> errorMessages = new ArrayList<String>();
	protected List<String> warningMessages = new ArrayList<String>();
	protected List<String> successMessages = new ArrayList<String>();
	protected List<String> infoMessages = new ArrayList<String>();
	
	protected Location location;
	
	public RequestMessages() {}
	
	public RequestMessages(Location location) {
		this.location = location;
	}
	
	/**
	 * Adds these messages to the session s
	 */
	public void persistMessages() {
		if(location != null) {
			location.put(SESSION_LOCATION_KEY, this);
		}
	}
	
	public void addFieldError(String fieldName, String errorMessage) {
		fieldErrors.put(fieldName, new FieldError(fieldName, errorMessage));
	}
	
	public Map<String, FieldError> getFieldErrors() {
		return fieldErrors;
	}

	public List<String> getErrorMessages() {
		return errorMessages;
	}

	public List<String> getWarningMessages() {
		return warningMessages;
	}

	public List<String> getSuccessMessages() {
		return successMessages;
	}

	public List<String> getInfoMessages() {
		return infoMessages;
	}
	
	
	
}
