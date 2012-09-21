package com.emergentideas.webhandle.assumptions.oak;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Holds information about the request that the handler wants to convey to the user.
 * @author kolz
 *
 */
public class RequestMessages {

	protected Map<String, FieldError> fieldErrors = new HashMap<String, FieldError>();
	
	protected List<String> errorMessages = new ArrayList<String>();
	protected List<String> warningMessages = new ArrayList<String>();
	protected List<String> successMessages = new ArrayList<String>();
	protected List<String> infoMessages = new ArrayList<String>();
	
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
