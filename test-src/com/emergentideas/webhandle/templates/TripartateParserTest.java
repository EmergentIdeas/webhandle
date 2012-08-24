package com.emergentideas.webhandle.templates;

import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.List;

import org.junit.Test;

public class TripartateParserTest {

	@Test
	public void testExpressionParser() {
		TripartateParser parser = new TripartateParser();
		
		TripartateElement tp = parser.parseTripartateExpression("hello");
		assertEquals("hello", tp.getDataSelectorExpression());
		assertNull(tp.getConditionalExpression());
		assertNull(tp.getHandlingExpression());
		
		tp = parser.parseTripartateExpression("hello::there");
		assertEquals("hello", tp.getDataSelectorExpression());
		assertNull(tp.getConditionalExpression());
		assertEquals("there", tp.getHandlingExpression());
		
		tp = parser.parseTripartateExpression("why??hello::there");
		assertEquals("hello", tp.getDataSelectorExpression());
		assertEquals("why", tp.getConditionalExpression());
		assertEquals("there", tp.getHandlingExpression());
		
		tp = parser.parseTripartateExpression("why??hello::");
		assertEquals("hello", tp.getDataSelectorExpression());
		assertEquals("why", tp.getConditionalExpression());
		assertNull(tp.getHandlingExpression());
		
		tp = parser.parseTripartateExpression("why??hello");
		assertEquals("hello", tp.getDataSelectorExpression());
		assertEquals("why", tp.getConditionalExpression());
		assertNull(tp.getHandlingExpression());
		
		tp = parser.parseTripartateExpression("::there");
		assertNull(tp.getConditionalExpression());
		assertNull(tp.getDataSelectorExpression());
		assertEquals("there", tp.getHandlingExpression());
	}
	
	
	@Test
	public void testTemplateParse() {
		TripartateParser parser = new TripartateParser();
		
		List<Element> elements = parser.parse(test1);
		Iterator<Element> it = elements.iterator();
		
		assertEquals(test1_1, it.next().toString());
		
		TripartateElement tp = (TripartateElement)it.next();
		assertEquals("$this", tp.getDataSelectorExpression());
		assertNull(tp.getConditionalExpression());
		assertNull(tp.getHandlingExpression());
		
		assertEquals(test1_2, it.next().toString());
		
		tp = (TripartateElement)it.next();
		assertEquals("adsService/allStores", tp.getDataSelectorExpression());
		assertNull(tp.getConditionalExpression());
		assertEquals("storeLink.template", tp.getHandlingExpression());
		
		assertEquals(test1_3, it.next().toString());
		assertFalse(it.hasNext());
		
		elements = parser.parse(test2);
		it = elements.iterator();
		
		tp = (TripartateElement)it.next();
		assertEquals("there", tp.getHandlingExpression());
		assertNull(tp.getDataSelectorExpression());
		assertNull(tp.getConditionalExpression());
		
		assertEquals("world", it.next().toString());
		
		
		elements = parser.parse(test3);
		it = elements.iterator();
		
		assertEquals("oh!", it.next().toString());
		
		tp = (TripartateElement)it.next();
		assertEquals("there", tp.getHandlingExpression());
		assertNull(tp.getDataSelectorExpression());
		assertNull(tp.getConditionalExpression());
		
		
		
		
	}
	
	protected static final String test2 =
			"__::there__world";
	
	protected static final String test3 =
			"oh!__::there__";
	
	protected static final String test1 = 
			"<span id=\"currently-viewing\">\n" + 
			"	Currently Viewing: __$this__ &raquo; \n" + 
			"	<a class=\"change-store-dialog-link\" onclick=\"dijit.byId('switchStoreDialog').show(); void(0);\" href=\"#\">Change Location</a>\n" + 
			"</span>\n" + 
			"\n" + 
			"<div class=\"tundra\" dojoType=\"dijit.Dialog\" id=\"switchStoreDialog\" title=\"Switch Store\" style=\"background-color: white; border: solid black 1px; display: none;\">\n" + 
			"    <div class=\"switch-store-dialog-contents\">\n" + 
			"    	<ul class=\"switch-to-store-list\">\n" + 
			"        __adsService/allStores::storeLink.template__\n" + 
			" 		</ul>\n" + 
			"    </div>\n" + 
			"</div>\n";
	
	protected static final String test1_1 = 
			"<span id=\"currently-viewing\">\n" + 
			"	Currently Viewing: ";
	
	protected static final String test1_2 = 
			" &raquo; \n" + 
			"	<a class=\"change-store-dialog-link\" onclick=\"dijit.byId('switchStoreDialog').show(); void(0);\" href=\"#\">Change Location</a>\n" + 
			"</span>\n" + 
			"\n" + 
			"<div class=\"tundra\" dojoType=\"dijit.Dialog\" id=\"switchStoreDialog\" title=\"Switch Store\" style=\"background-color: white; border: solid black 1px; display: none;\">\n" + 
			"    <div class=\"switch-store-dialog-contents\">\n" + 
			"    	<ul class=\"switch-to-store-list\">\n" + 
			"        ";
	
	protected static final String test1_3 =
			"\n" + 
			" 		</ul>\n" + 
			"    </div>\n" + 
			"</div>\n";
	
	
}
