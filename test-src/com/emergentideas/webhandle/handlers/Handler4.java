package com.emergentideas.webhandle.handlers;

import javax.ws.rs.Path;

import com.emergentideas.webhandle.output.ResponsePackage;
import com.emergentideas.webhandle.output.Template;

public class Handler4 {

	@Path("/hello")
	@Template
	@ResponsePackage("body-only")
	public Object hello() {
		return "one";
	}
	
	@Path("/hi")
	@Template
	public Object hi() {
		return "one";
	}

}
