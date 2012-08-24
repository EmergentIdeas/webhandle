package com.emergentideas.webhandle.exceptions;

public class TransformationException extends RuntimeException {

	public TransformationException() {
		super();
	}
	
	public TransformationException(Throwable e) {
		super(e);
	}
	
	public TransformationException(String msg, Throwable e) {
		super(msg, e);
	}
}
