package com.emergentideas.location;

import java.util.List;

import com.emergentideas.location.composites.db.Db;

public class TestObj {
	
	protected String a;
	protected String b;
	
	protected TestObj child1;
	protected TestObj child2;
	protected List<TestObj> manyChildren;
	
	public TestObj() {
		
	}
	
	public TestObj(String a, String b) {
		this.a = a;
		this.b = b;
	}
	
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
	public void setChild1(@Db("id") TestObj child1) {
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
	
	/**
	 * Returns the <code>one</code>-[sum of two]-[sum of three]
	 * @param one
	 * @param two
	 * @param three
	 */
	public String test1(@Name("one") String one, 
			@Name("two") @Transformers(value = {"one", "two"}, properties = "name1=value1&name2=value2") @Source("request") Integer[] two,
			@Name("two") List<Double> three) {
		int intSum = 0;
		for(int i : two) {
			intSum += i;
		}
		
		double dSum = 0;
		for(Double d : three) {
			dSum += d;
		}
		
		return one + "-" + intSum + "-" + dSum;
	}

	
	
}