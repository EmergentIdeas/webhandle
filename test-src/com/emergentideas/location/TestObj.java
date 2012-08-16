package com.emergentideas.location;

import java.util.List;

public class TestObj {
	
	protected String a;
	protected String b;
	
	protected TestObj child1;
	protected TestObj child2;
	protected List<TestObj> manyChildren;
	
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
	public TestObj getChild1() {
		return child1;
	}
	public void setChild1(TestObj child1) {
		this.child1 = child1;
	}
	public TestObj getChild2() {
		return child2;
	}
	public void setChild2(TestObj child2) {
		this.child2 = child2;
	}
	public List<TestObj> getManyChildren() {
		return manyChildren;
	}
	public void setManyChildren(List<TestObj> manyChildren) {
		this.manyChildren = manyChildren;
	}
	
	public void test1(@Name("one") String one, @Transformers({"one", "two"}) @Source("request") Integer[] two) {
		
	}

	
	
}