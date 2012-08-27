package com.emergentideas.webhandle.handlers;

import static org.junit.Assert.*;

import java.lang.reflect.Method;

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
		
		// make sure we examine them in order
		specs = investigator.determineHandlers("/2/one", HttpMethod.GET);
		assertEquals(2, specs.length);
		assertEquals(three, specs[0].getMethod());
		
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
		
		// make sure we examine them in order
		specs = investigator.determineHandlers("/one", HttpMethod.GET);
		assertEquals(2, specs.length);
		assertEquals(one, specs[0].getMethod());
		
		specs = investigator.determineHandlers("/one", HttpMethod.POST);
		assertEquals(1, specs.length);
		assertEquals(two, specs[0].getMethod());
	}
}
