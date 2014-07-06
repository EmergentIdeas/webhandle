package com.emergentideas.webhandle.handlers;

import javax.ws.rs.Path;

public class EnumBasedHandler {

	public enum TestEnum { ONE, TWO }
	
	protected TestEnum theValue;
	protected boolean called = false;
	
	@Path("/1")
	public Object handle1(TestEnum val) {
		theValue = val;
		called = true;
		return "";
	}

	public TestEnum getTheValue() {
		return theValue;
	}

	public void setTheValue(TestEnum theValue) {
		this.theValue = theValue;
	}

	public boolean isCalled() {
		return called;
	}

	public void setCalled(boolean called) {
		this.called = called;
	}
	
	
}
