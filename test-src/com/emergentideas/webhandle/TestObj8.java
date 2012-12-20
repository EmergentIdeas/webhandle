package com.emergentideas.webhandle;

import javax.annotation.Resource;

@Resource(name = "to8", type = TestObj7.class)
public class TestObj8 extends TestObj7 {

	@Resource
	private String private8;
	@Resource
	protected String protected8;
	@Resource
	String package8;
	@Resource
	public String public8;

	protected String notAResource = "not set";

	@Resource(name = "npv8")
	private String namedPrivate8;
	
	
	@Resource(name = "npr8")
	protected String namedProtected8;
	
	
	@Resource(name = "npa8")
	String namedPackage8;
	
	@Resource(name = "npu8")
	public String namedPublic8;

	public String getPrivate8() {
		return private8;
	}
	public String getProtected8() {
		return protected8;
	}
	public String getPackage8() {
		return package8;
	}
	public String getPublic8() {
		return public8;
	}
	public String getNotAResource() {
		return notAResource;
	}
	public String getNamedPrivate8() {
		return namedPrivate8;
	}
	public String getNamedProtected8() {
		return namedProtected8;
	}
	public String getNamedPackage8() {
		return namedPackage8;
	}
	public String getNamedPublic8() {
		return namedPublic8;
	}
	
	
}
