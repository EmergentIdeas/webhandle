package com.emergentideas.webhandle.templates;

import static junit.framework.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.jexl2.JexlEngine;
import org.junit.Test;

import com.emergentideas.webhandle.AppLocation;

public class EscapedExpressionTest {

	@Test
	public void testEscapeExpression() {
		EscapedExpression esc = new EscapedExpression();
		
		String escaped = esc.escape("if(something/here) { 'something/else' } if(something.here) { \"some/thing/else\" } " +
				"if(something1/here) { 'some\"th\\'ing/else' } if(something.here) { \"some/t'hi\\\"ng/else\" }");
		
		assertFalse(escaped.contains("if(something/here)"));
		assertTrue(escaped.contains("'something/else'"));
		assertTrue(escaped.contains("\"some/thing/else\""));
		assertTrue(escaped.contains("'some\"th\\'ing/else'"));
		assertTrue(escaped.contains("\"some/t'hi\\\"ng/else\""));
		assertTrue(escaped.contains("locationVariable0"));
		assertTrue(escaped.contains("locationVariable1"));
		assertFalse(escaped.contains("something1/here"));
		assertFalse(escaped.contains("locationVariable2"));
		
		esc = new EscapedExpression();
		escaped = esc.escape("hello/there/somebody");
		assertTrue(escaped.contains("locationVariable0"));
		assertFalse(escaped.contains("hello"));
		assertFalse(escaped.contains("there"));
		assertFalse(escaped.contains("somebody"));
	}
	
	@Test
	public void testEvaluation() {
		JexlEngine jexl = new JexlEngine();
		jexl.setSilent(false);

		AppLocation location = new AppLocation();
		Expression exp = new EscapedExpression(jexl, "if(name == 'hello') { 'hi to you' } else { 'good morning' }");
		
		assertEquals("good morning", exp.evaluate(location));
		
		location.put("name", "hello");
		assertEquals("hi to you", exp.evaluate(location));
		
		exp = new EscapedExpression(jexl, "if(values/name == 'hello') { 'hi to you' } else { 'good morning' }");
		location = new AppLocation();
		assertEquals("good morning", exp.evaluate(location));
		
		Map<String, String> values = new HashMap<String, String>();
		location.put("values", values);
		values.put("name", "hello");
		
		assertEquals("hi to you", exp.evaluate(location));
	}
}
