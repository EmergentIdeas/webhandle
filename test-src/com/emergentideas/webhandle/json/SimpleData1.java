package com.emergentideas.webhandle.json;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SimpleData1 {

	protected String greeting = "Hello";
	protected Integer age = 35;
	protected Double cakeWeight = 4.5;
	protected Date todaysDate = new Date();
	protected ObjectSerializer serializer = new Serializer1();
	protected String actuallyNull = null;
	protected String shouldNotSee = "You shouldn't see this.";
	
	protected List<String> justStrings = new ArrayList<String>();
	protected List<Object> lotsOfStuff = new ArrayList<Object>();
	protected int[] ints = new int[] { 1, 2, 3 };
	protected Double[] doubles = new Double[] { 1.1, 2.2, 3.3 };
	
	
	public SimpleData1() {
		justStrings.add("Hello");
		justStrings.add("World");
		
		lotsOfStuff.add("Hello");
		lotsOfStuff.add(serializer);
	}
	
	public String getGreeting() {
		return greeting;
	}
	public void setGreeting(String greeting) {
		this.greeting = greeting;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	public Double getCakeWeight() {
		return cakeWeight;
	}
	public void setCakeWeight(Double cakeWeight) {
		this.cakeWeight = cakeWeight;
	}
	public Date getTodaysDate() {
		return todaysDate;
	}
	public void setTodaysDate(Date todaysDate) {
		this.todaysDate = todaysDate;
	}
	public ObjectSerializer getSerializer() {
		return serializer;
	}
	public void setSerializer(ObjectSerializer serializer) {
		this.serializer = serializer;
	}
	public String getActuallyNull() {
		return actuallyNull;
	}
	public void setActuallyNull(String actuallyNull) {
		this.actuallyNull = actuallyNull;
	}

	public List<String> getJustStrings() {
		return justStrings;
	}

	public void setJustStrings(List<String> justStrings) {
		this.justStrings = justStrings;
	}

	public List<Object> getLotsOfStuff() {
		return lotsOfStuff;
	}

	public void setLotsOfStuff(List<Object> lotsOfStuff) {
		this.lotsOfStuff = lotsOfStuff;
	}

	public int[] getInts() {
		return ints;
	}

	public void setInts(int[] ints) {
		this.ints = ints;
	}

	public Double[] getDoubles() {
		return doubles;
	}

	public void setDoubles(Double[] doubles) {
		this.doubles = doubles;
	}
	
	
	
}
