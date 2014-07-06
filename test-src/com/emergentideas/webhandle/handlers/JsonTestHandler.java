package com.emergentideas.webhandle.handlers;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonString;
import javax.json.JsonStructure;
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
	
	@Path("/2")
	public Object parsesSomeJson(JsonStructure theJson) {
		return ((JsonObject)theJson).getString("hello");
	}
	
	@Path("/3")
	public Object parsesSomeJsonToObject(JsonObject theJson) {
		return theJson.getString("hello");
	}

	@Path("/4")
	public Object parsesSomeJsonToArrayChanged(JsonArray theJson) {
		System.out.println(theJson.getValueType());
		return ((JsonString)theJson.get(0)).getString();
	}

}
