package com.emergentideas.webhandle.handlers;

public class Handler3 extends Handler2 {

	@Handle(value = "/two", method = {HttpMethod.POST, HttpMethod.GET})
	public void six(String id) {
		
	}

}
