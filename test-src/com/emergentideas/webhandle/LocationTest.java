package com.emergentideas.webhandle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Stack;

import org.junit.Test;

import com.emergentideas.webhandle.AppLocation;
import com.emergentideas.webhandle.Location;

import static org.junit.Assert.*;

public class LocationTest {

	@Test
	public void testBasics() {
		Location loc = new AppLocation();
		
		loc.put("one", "two");
		
		TestObj o1 = new TestObj();
		loc.add(o1);
		o1.setA("thea");
		
		TestObj o2 = new TestObj();
		o2.setA("somedifferenta");
		o2.setB("thefirstb");
		
		assertEquals("two", loc.get("one"));
		assertEquals("thea", loc.get("a"));
		assertNull(loc.get("b"));
		
		loc.put("a", "thenameda");
		assertEquals("thenameda", loc.get("a"));
		
		loc.add(o2);
		assertEquals("thefirstb", loc.get("b"));
	}
	
	@Test
	public void testParentBehavior() {
		Location parent = new AppLocation();
		Location loc = new AppLocation(parent);
		
		loc.put("one", "two");
		
		TestObj o1 = new TestObj();
		loc.add(o1);
		o1.setA("thea");
		
		TestObj o2 = new TestObj();
		parent.put("two", o2);
		o2.setA("somedifferenta");
		o2.setB("thefirstb");
		
		assertEquals(o2, loc.get("two"));
		
		loc.put("two", o1);
		assertEquals(o1, loc.get("two"));
		
	}
	
	@Test
	public void testPaths() {
		Location loc = new AppLocation();
		
		TestObj o1 = new TestObj();
		loc.add(o1);
		loc.put("o1", o1);
		o1.setA("thea");
		
		TestObj o2 = new TestObj();
		o1.setChild1(o2);
		o2.setA("somedifferenta");
		o2.setB("thefirstb");
		
		assertEquals("thea", loc.get("a"));
		assertEquals("somedifferenta", loc.get("child1/a"));
		assertEquals("somedifferenta", loc.get("o1/child1/a"));
		
		List<TestObj> manyChildren = new ArrayList<TestObj>();
		TestObj o3 = new TestObj();
		manyChildren.add(o3);
		manyChildren.add(new TestObj());
		o2.setManyChildren(manyChildren);
		
		assertEquals(o3, loc.get("child1/manyChildren"));
		assertEquals(2, loc.all("child1/manyChildren").size());
		assertEquals(manyChildren, loc.get("child1/@manyChildren"));
		assertTrue(loc.all("child1/@manyChildren").contains(manyChildren));
		
	}
	
	@Test
	public void testThis() {
		Location loc = new AppLocation();
		
		
		TestObj o1 = new TestObj();
		loc.add(o1);
		o1.setA("thea");
		
		TestObj o2 = new TestObj();
		loc.add(o2);
		o2.setA("somedifferenta");
		o2.setB("thefirstb");
		
		assertEquals(o1, loc.get("$this"));
	}
	
	
	@Test
	public void testPathOperations() {
		AppLocation loc = new AppLocation();
		
		Stack<String> stack = loc.pathToStack("one/two/three");
		assertEquals("one", stack.pop());
		assertEquals("two", stack.pop());
		assertEquals("three", stack.pop());
		
		assertEquals("one/two/three", loc.stackToPath(loc.pathToStack("one/two/three")));
	}
	
	@Test
	public void testCollectionsMethods() {
		AppLocation loc = new AppLocation();
		List<Object> l = new ArrayList<Object>();
		
		l.add("one");
		l.add(null);
		l.add("two");
		
		Collection<Object> result = loc.getNonNulls(l);
		
		assertEquals(2, result.size());
		assertTrue(result.contains("one"));
		assertTrue(result.contains("two"));
		
		List<Object> l2 = new ArrayList<Object>();
		l.add(l2);
		l2.add("three");
		l2.add(null);
		l2.add("four");
		
		
		loc.expandCollections(l);
		result = l;
		
		assertEquals(4, result.size());
		assertTrue(result.contains("one"));
		assertTrue(result.contains("two"));
		assertTrue(result.contains("three"));
		assertTrue(result.contains("four"));
	}
	
}


