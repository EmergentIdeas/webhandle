package com.emergentideas.webhandle.handlers;

import com.emergentideas.webhandle.Location;
import com.emergentideas.webhandle.exceptions.CouldNotHandleException;
import com.emergentideas.webhandle.exceptions.TransformationException;
import com.emergentideas.webhandle.output.Template;
import com.emergentideas.webhandle.output.Wrap;

public class Handler1 {

	protected boolean calledOnce = false;
	
	@Handle(path = {"/one/{name}", "/one" }, method = HttpMethod.GET)
	public String one(String name) {
		return "really";
	}
	
	@Handle(path = "/one", method = {HttpMethod.POST, HttpMethod.GET})
	@Template
	@Wrap
	public String two(String id, Location location) {
		return "mytemplate.template";
	}
	
	@Handle(path = "/three")
	public String three(String id) {
		if(calledOnce == false) {
			calledOnce = true;
			throw new CouldNotHandleException();
		}
		return "called twice";
	}
	
	@Handle(path = "/three")
	public String four(String id) {
		if(calledOnce == false) {
			calledOnce = true;
			throw new CouldNotHandleException();
		}
		return "called twice";
	}
	
	@Handle(path = "/five")
	public String five(String id) {
		throw new SecurityException();
	}
	
	@Handle(path = "/six")
	public String six(String id) {
		throw new TransformationException();
	}

}
