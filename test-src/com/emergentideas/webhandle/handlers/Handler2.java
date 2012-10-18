package com.emergentideas.webhandle.handlers;

@Handle(value = { "/1", "/2"})
public class Handler2 {
	
	@Handle(value = {"/one/{name}", "/one" }, method = HttpMethod.GET)
	public void three(String name) {
		
	}
	
	@Handle(value = "/one", method = {HttpMethod.POST, HttpMethod.GET})
	public void four(String id) {
		
	}


	@Handle(value = "/two", method = {HttpMethod.POST, HttpMethod.GET})
	public void five(String id) {
		
	}
}
