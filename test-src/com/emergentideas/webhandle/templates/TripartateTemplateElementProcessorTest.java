package com.emergentideas.webhandle.templates;

import static junit.framework.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;

import com.emergentideas.webhandle.AppLocation;

public class TripartateTemplateElementProcessorTest {

	@Test
	public void testDetermineTemplateName() throws Exception {
		
		TripartateTemplateElementProcessor proc = new TripartateTemplateElementProcessor(new JexlExpressionFactory());
		AppLocation location = new AppLocation();
		TripartateElementBasic te = new TripartateElementBasic();
		
		te.setHandlingExpression("temp1");
		assertEquals("temp1", proc.determineTemplateName(location, te));
		
		location.put("temp1", "the/template/name");
		te.setHandlingExpression("$temp1");
		assertEquals("the/template/name", proc.determineTemplateName(location, te));
		
		location.put("testvar", "hi");
		te.setHandlingExpression("$ if(testvar == 'hi') { 'newtemp' } else { temp1 }");
		assertEquals("newtemp", proc.determineTemplateName(location, te));
		
		location.put("testvar", "hello");
		assertEquals("the/template/name", proc.determineTemplateName(location, te));

	}
	
	@Test
	public void testDetermineSelectedObjects() throws Exception {
		
		TripartateTemplateElementProcessor proc = new TripartateTemplateElementProcessor(new JexlExpressionFactory());
		AppLocation location = new AppLocation();
		TripartateElementBasic te = new TripartateElementBasic();
		
		te.setDataSelectorExpression("");
		assertNull(proc.determineSelectedObjects(location, te));
		
		te.setDataSelectorExpression("test1");
		assertEquals(0, ((Collection<?>)proc.determineSelectedObjects(location, te)).size());
		
		location.put("test1", "datavalue");
		assertEquals("datavalue", proc.determineSelectedObjects(location, te));
		
		List<String> vals = new ArrayList<String>();
		vals.add("one");
		vals.add("two");
		
		location.put("test1", vals);
		assertEquals(2, ((Collection<?>)proc.determineSelectedObjects(location, te)).size());
		
		Lister l1 = new Lister(vals);
		location.put("test1", l1);
		te.setDataSelectorExpression("test1/a");
		Object o = proc.determineSelectedObjects(location, te);
		assertEquals(2, ((Collection<?>)proc.determineSelectedObjects(location, te)).size());
		
		List<Lister> listers = new ArrayList<TripartateTemplateElementProcessorTest.Lister>();
		listers.add(l1);
		Lister l2 = new Lister(vals);
		listers.add(l2);
		location.put("test1", listers);
		assertEquals(4, ((Collection<?>)proc.determineSelectedObjects(location, te)).size());

		
		te.setDataSelectorExpression("test1.a");
		location.put("test1", l1);
		assertEquals(2, ((Collection<?>)proc.determineSelectedObjects(location, te)).size());
		
	}
	
	
	@Test
	public void testDetermineShouldBeRun() throws Exception {
		
		TripartateTemplateElementProcessor proc = new TripartateTemplateElementProcessor(new JexlExpressionFactory());
		AppLocation location = new AppLocation();
		TripartateElementBasic te = new TripartateElementBasic();
		
		assertTrue(proc.shouldRunTemplateBasedOnConditional(location, te));
		
		te.setConditionalExpression("");
		assertTrue(proc.shouldRunTemplateBasedOnConditional(location, te));
		
		te.setConditionalExpression("var1");
		assertFalse(proc.shouldRunTemplateBasedOnConditional(location, te));
		
		location.put("var1", true);
		assertTrue(proc.shouldRunTemplateBasedOnConditional(location, te));
		
		location.put("var1", false);
		assertFalse(proc.shouldRunTemplateBasedOnConditional(location, te));
		
		
		location.put("var1", "hello");
		te.setConditionalExpression("var1 == 'hello'");
		assertTrue(proc.shouldRunTemplateBasedOnConditional(location, te));
		
		te.setConditionalExpression("var1 == 'hello!'");
		assertFalse(proc.shouldRunTemplateBasedOnConditional(location, te));
		
		te.setConditionalExpression("if(var1 == 'hello!') { true } else { false }");
		assertFalse(proc.shouldRunTemplateBasedOnConditional(location, te));
		
		te.setConditionalExpression("if(var1 == 'hello') { true } else { false }");
		assertTrue(proc.shouldRunTemplateBasedOnConditional(location, te));
		
		List<String> vals = new ArrayList<String>();
		vals.add("one");
		vals.add("two");
		
		location.put("test1", vals);
		te.setConditionalExpression("test1");
		assertTrue(proc.shouldRunTemplateBasedOnConditional(location, te));
		
		vals.clear();
		assertFalse(proc.shouldRunTemplateBasedOnConditional(location, te));
		
		location.put("test1", "hi");
		assertTrue(proc.shouldRunTemplateBasedOnConditional(location, te));
		
		location.put("test1", null);
		assertFalse(proc.shouldRunTemplateBasedOnConditional(location, te));

	}
	
	
	public class Lister {
		protected List<?> vals;
		
		public Lister(List<?> vals) {
			this.vals = vals;
		}
		
		public List<?> getA() {
			return vals;
		}
	}
}
