package com.emergentideas.webhandle.handlers;

import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.util.List;

import org.junit.Test;

import com.emergentideas.utils.ReflectionUtils;
import com.emergentideas.webhandle.CallSpec;

public class HandleAnnotationHandlerInvestigatorTest {

	@Test
	public void testFindMethod() {
		
		Method one = ReflectionUtils.getFirstMethod(Handler1.class, "one");
		Method two = ReflectionUtils.getFirstMethod(Handler1.class, "two");
		Method three = ReflectionUtils.getFirstMethod(Handler2.class, "three");
		Method four = ReflectionUtils.getFirstMethod(Handler2.class, "four");
		
		
		HandleAnnotationHandlerInvestigator investigator = new HandleAnnotationHandlerInvestigator();
		investigator.analyzeObject(new Handler1());
		
		handler1Tests(investigator, one, two);
		
		investigator = new HandleAnnotationHandlerInvestigator();
		// add the second handler first.  Since it has a class handler prefix our previous tests should still
		// give the same results
		investigator.analyzeObject(new Handler2());
		investigator.analyzeObject(new Handler1());

		handler1Tests(investigator, one, two);
		
		assertEquals(0, investigator.determineHandlers("/1/hello", HttpMethod.GET).length);
		
		CallSpec[] specs = investigator.determineHandlers("/1/one/12", HttpMethod.GET);
		assertEquals(1, specs.length);
		assertEquals(three, specs[0].getMethod());
		
		// make sure we have two.  Their order is non-deterministic
		specs = investigator.determineHandlers("/2/one", HttpMethod.GET);
		assertEquals(2, specs.length);
		
		specs = investigator.determineHandlers("/2/one", HttpMethod.POST);
		assertEquals(1, specs.length);
		assertEquals(four, specs[0].getMethod());
	}
	
	protected void handler1Tests(HandlerInvestigator investigator, Method one, Method two) {
		
		CallSpec[] specs = investigator.determineHandlers("/hello", HttpMethod.GET); 
		assertEquals(0, specs.length);
		
		specs = investigator.determineHandlers("/one/12", HttpMethod.GET);
		assertEquals(1, specs.length);
		assertEquals(one, specs[0].getMethod());
		
		// There's really only one of these. In the past, I understood that methods were returned in the order
		// they are declared, but that is not reliably so. Now, if you define two handlers for the same url pattern
		// in the same class, it's up to the jvm ordering of the methods returned from getDeclaredMethods as to which
		// you'll get
		specs = investigator.determineHandlers("/one", HttpMethod.GET);
		assertEquals(1, specs.length);
		assertEquals(two, specs[0].getMethod());
		
		specs = investigator.determineHandlers("/one", HttpMethod.POST);
		assertEquals(1, specs.length);
		assertEquals(two, specs[0].getMethod());
	}
	
	@Test
	public void testFindMostRecentMethodForUrl() throws Exception {
		HandleAnnotationHandlerInvestigator investigator = new HandleAnnotationHandlerInvestigator();
		investigator.analyzeObject(new Handler3());
		
		CallSpec[] specs = investigator.determineHandlers("/1/two", HttpMethod.GET); 
		assertEquals(1, specs.length);
		

		
	}
	
	@Test
	public void testMethodOrder() throws Exception {
		HandleAnnotationHandlerInvestigator investigator = new HandleAnnotationHandlerInvestigator();
		List<Method> methods = investigator.getMethodsInReverseInheritenceOrder(Handler3.class);
		
		assertTrue(methods.get(methods.size() - 1).getName().equals("six"));
	}
}
