package com.emergentideas.webhandle.templates;

import static junit.framework.Assert.*;

import org.junit.Test;

import com.emergentideas.webhandle.output.SegmentedOutput;
	
public class SegmentedOutputTemplateTest {

	@Test
	public void testAddedSections() throws Exception {
		
		SegmentedOutput output = new SegmentedOutput();
		output.getStream("one").append("hello");
		output.getList("two").add("there");
		output.getList("two").add("world");
		output.getPropertySet("three").put("punctuation", "!");
		output.getPropertySet("one").put("punctuation", "!");
		
		SegmentedOutputTemplate template = new SegmentedOutputTemplate(output, null, null);
		assertEquals("hello", template.sections.get("one"));
		assertEquals("there\nworld", template.sections.get("two"));
		assertEquals("punctuation=!", template.sections.get("three"));
	}
}
