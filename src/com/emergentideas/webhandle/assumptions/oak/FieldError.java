package com.emergentideas.webhandle.assumptions.oak;

public class FieldError {

	protected String fieldName;
	protected String errorMessage;
	
	public FieldError() {}
	
	public FieldError(String fieldName, String errorMessage) {
		setFieldName(fieldName);
		setErrorMessage(errorMessage);
	}
	
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	
}
