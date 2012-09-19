package com.emergentideas.webhandle.handlers;

import com.emergentideas.webhandle.Command;
import com.emergentideas.webhandle.Location;
import com.emergentideas.webhandle.NotNull;
import com.emergentideas.webhandle.TestObj;
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
	@Wrap("app_page")
	public String two(String id, Location location) {
		return "mytemplate";
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
	
	@Handle(path = "/seven")
	public String seven(String id) {
		return "The number is: " + id;
	}
	
	@Handle(path = "/eight/{id}")
	public String eight(Double id) {
		return "The number is: " + id;
	}
	
	@Handle(path = "/nine")
	@Template
	public String nine(String id) {
		return "one";
	}
	
	@Handle(path = "/ten")
	@Template
	public String ten(@NotNull @Command TestObj obj, Location loc) {
		if(obj.getA() == null) {
			obj.setA("hello");
		}
		obj.setB("world");
		
		if(obj.getId() == null) {
			obj.setId("3");
		}
		loc.add(obj);
		return "form1";
	}
	
	@Handle(path = "/eleven", method = {HttpMethod.POST, HttpMethod.GET})
	@Template
	@Wrap("app_page")
	public String eleven(String id, Location location) {
		return "mytemplate";
	}


}
