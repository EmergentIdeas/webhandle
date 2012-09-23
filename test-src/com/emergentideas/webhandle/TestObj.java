package com.emergentideas.webhandle;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.emergentideas.webhandle.Name;
import com.emergentideas.webhandle.NoInject;
import com.emergentideas.webhandle.Source;
import com.emergentideas.webhandle.Transformers;
import com.emergentideas.webhandle.composites.db.Db;

@Entity
@Name("hello")
@Type({"there", "world"})
public class TestObj {
	
	protected String a;
	protected String b;
	
	@Id
	protected String id;
	protected String[] c;
	
	@Transient
	protected TestObj child1;
	@Transient
	protected TestObj child2;
	@Transient
	protected List<TestObj> manyChildren;
	
	@Transient
	int order = 2;
	
	public TestObj() {
		
	}
	
	public TestObj(String a, String b) {
		this.a = a;
		this.b = b;
	}
	
	
	public String getId() {
		return id;
	}

	
	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getA() {
		return a;
	}
	
	@Wire
	public void setA(String a) {
		this.a = a;
	}
	public String getB() {
		return b;
	}
	
	@NoInject
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
	
	@RolesAllowed("admin")
	public void setSecuredMethod(int[] someInts) {
		
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

	public String[] getC() {
		return c;
	}

	@Wire
	public void setC(String[] c) {
		this.c = c;
	}

	public void primitiveParameters(int[] values) {
		
	}
	
}