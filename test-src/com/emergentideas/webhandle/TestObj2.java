package com.emergentideas.webhandle;

import javax.persistence.Id;

@Wire
public class TestObj2 {
	
	protected String a;
	protected String b;
	protected String c;
	protected TestObj inner;
	
	@Id
	protected int id;
	
	public String getA() {
		return a;
	}
	public void setA(String a) {
		this.a = a;
	}
	public String getB() {
		return b;
	}
	public void setB(String b) {
		this.b = b;
	}
	public String getC() {
		return c;
	}
	public void setC(String c) {
		this.c = c;
	}

	public void causeHorribleException() {
		throw new RuntimeException();
	}
	public TestObj getInner() {
		return inner;
	}
	public void setInner(TestObj inner) {
		this.inner = inner;
	}
	
	
}
