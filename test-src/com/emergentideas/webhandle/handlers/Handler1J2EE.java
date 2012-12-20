package com.emergentideas.webhandle.handlers;

import javax.persistence.EntityManager;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.apache.commons.lang.StringUtils;

import com.emergentideas.webhandle.Command;
import com.emergentideas.webhandle.Location;
import com.emergentideas.webhandle.NotNull;
import com.emergentideas.webhandle.TestObj;
import com.emergentideas.webhandle.Wire;
import com.emergentideas.webhandle.composites.db.Db;
import com.emergentideas.webhandle.exceptions.CouldNotHandleException;
import com.emergentideas.webhandle.exceptions.TransformationException;
import com.emergentideas.webhandle.exceptions.UserRequiredException;
import com.emergentideas.webhandle.output.Template;
import com.emergentideas.webhandle.output.Wrap;

public class Handler1J2EE {

	protected boolean calledOnce = false;
	protected EntityManager entityManager;
	
	@Path("/one/{name}")
	@GET
	public String one(String name) {
		return "really";
	}
	
	@Path("/one")
	@GET
	@POST
	@Template
	@Wrap("app_page")
	public String two(String id, Location location) {
		return "mytemplate";
	}
	
	@Path("/three")
	public String three(String id) {
		if(calledOnce == false) {
			calledOnce = true;
			throw new CouldNotHandleException();
		}
		return "called twice";
	}
	
	@Path("/three")
	public String four(String id) {
		if(calledOnce == false) {
			calledOnce = true;
			throw new CouldNotHandleException();
		}
		return "called twice";
	}
	
	
	@Path("/five")
	public String five(String id) {
		throw new SecurityException();
	}
	
	@Path("/twelve")
	public String twelve(String id) {
		throw new UserRequiredException();
	}
	
	@Path("/six")
	public String six(String id) {
		throw new TransformationException();
	}
	
	@Path("/seven")
	public String seven(String id) {
		return "The number is: " + id;
	}
	
	@Path("/eight/{id}")
	public String eight(Double id) {
		return "The number is: " + id;
	}
	
	@Path("/nine")
	@Template
	public String nine(String id) {
		return "one";
	}
	
	@Path("/ten/{id}")
	@GET
	@Template
	public String tenGet(@Db("id") @NotNull @Command TestObj obj, Location loc) {
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
	
	@Path("/ten")
	@POST
	@Template
	public String ten(@Db("id") @NotNull @Command TestObj obj, Location loc) {
		if(StringUtils.isBlank(obj.getId())) {
			obj.setId("" + System.currentTimeMillis());
		}
		entityManager.persist(obj);
		loc.add(obj);
		return "form1";
	}
	
	@Path("/eleven")
	@GET
	@POST
	@Template
	@Wrap("app_page")
	public String eleven(String id, Location location) {
		return "mytemplate";
	}
	
	@Path("/1/fourteen")
	public String fourteen() {
		return "handler1";
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	@Wire
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public boolean isCalledOnce() {
		return calledOnce;
	}

	public void setCalledOnce(boolean calledOnce) {
		this.calledOnce = calledOnce;
	}

	
	
}
