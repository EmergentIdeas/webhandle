package com.emergentideas.webhandle.handlers;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

import javax.ws.rs.GET;

@Handle(value = { "/1", "/2"})
public class Handler2J2EE {
	
	@Path("/one/{name}")
	@GET
	public void three(String name) {
		
	}
	
	@Path("/one")
	@GET
	@POST
	public void four(String id) {
		
	}

	@Path("/two")
	@POST
	@GET
	public void five(String id) {
		
	}
	
	@Path("/fourteen")
	public String fourteen() {
		return "handler2";
	}

}
