package com.emergentideas.webhandle.handlers;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import com.emergentideas.webhandle.json.JSON;

public class JsonTestHandler {

	@Path("/1")
	@GET
	@JSON
	public Object getSomeJSON() {
		return new String[] { "hello", "there" };
	}
}
