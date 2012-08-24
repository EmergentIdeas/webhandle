package com.emergentideas.webhandle;

import java.lang.reflect.Method;
import java.util.Map;

public class CallSpec {

	protected Object focus;
	protected Method method;
	protected boolean failOnMissingParameter;
	protected Map<String, Object> callSpecificProperties;
	
	public CallSpec() {
		
	}
	
	public CallSpec(Object focus, Method method, boolean failOnMissingParameter) {
		this.focus = focus;
		this.method = method;
		this.failOnMissingParameter = failOnMissingParameter;
	}
	
	public Object getFocus() {
		return focus;
	}
	public void setFocus(Object focus) {
		this.focus = focus;
	}
	public Method getMethod() {
		return method;
	}
	public void setMethod(Method method) {
		this.method = method;
	}
	public boolean isFailOnMissingParameter() {
		return failOnMissingParameter;
	}
	public void setFailOnMissingParameter(boolean failOnMissingParameter) {
		this.failOnMissingParameter = failOnMissingParameter;
	}

	public Map<String, Object> getCallSpecificProperties() {
		return callSpecificProperties;
	}

	public void setCallSpecificProperties(Map<String, Object> callSpecificProperties) {
		this.callSpecificProperties = callSpecificProperties;
	}
	
	
}
