package com.emergentideas.webhandle.handlers;

@Handle(path = { "/1", "/2"})
public class Handler2 {
	
	@Handle(path = {"/one/{name}", "/one" }, method = HttpMethod.GET)
	public void three(String name) {
		
	}
	
	@Handle(path = "/one", method = {HttpMethod.POST, HttpMethod.GET})
	public void four(String id) {
		
	}


}
