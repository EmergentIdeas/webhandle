package com.emergentideas.webhandle;

import javax.annotation.Resource;

@Resource
public class TestObj7 {

	@Resource
	private String private7;
	@Resource
	protected String protected7;
	@Resource
	String package7;
	@Resource
	public String public7;
	
	
	public String getPrivate7() {
		return private7;
	}
	public String getProtected7() {
		return protected7;
	}
	public String getPackage7() {
		return package7;
	}
	public String getPublic7() {
		return public7;
	}
	
	
	
}
