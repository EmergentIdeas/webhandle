package com.emergentideas.webhandle.handlers;

import com.emergentideas.webhandle.Location;
import com.emergentideas.webhandle.output.Template;

public class Handler1 {

	@Handle(path = {"/one/{name}", "/one" }, method = HttpMethod.GET)
	public void one(String name) {
		
	}
	
	@Handle(path = "/one", method = {HttpMethod.POST, HttpMethod.GET})
	@Template
	public String two(String id, Location location) {
		return "mytemplate.template";
	}
}
