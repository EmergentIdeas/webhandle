package com.emergentideas.webhandle.templates;

import static junit.framework.Assert.*;

import java.io.File;

import org.junit.Test;

import com.emergentideas.webhandle.AppLocation;
import com.emergentideas.webhandle.Location;
import com.emergentideas.webhandle.WebAppLocation;
import com.emergentideas.webhandle.output.HTML5SegmentedOutput;
import com.emergentideas.webhandle.output.SegmentedOutput;

public class HintsTest {

	@Test
	public void testHints() throws Exception {
		TripartateFileTemplateSource ts = new TripartateFileTemplateSource(new File("WebContent/WEB-INF/testTemplates"));
		
		Location loc = new AppLocation();
		WebAppLocation web = new WebAppLocation(loc);
		web.setTemplateSource(ts);
		
		TemplateInstance ti = ts.get("override1");
		SegmentedOutput out = prepOutput();
		
		ti.render(out, loc, null, null);

		assertTrue(out.getList("cssIncludes").contains("default.css"));
		assertTrue(out.getList("cssIncludes").contains("one.css"));
		assertTrue(out.getList("cssIncludes").contains("two.css"));
		assertTrue(out.getList("cssIncludes").contains("three.css"));
		
		assertTrue(out.getStream("body").toString().startsWith("start:"));
		assertTrue(out.getStream("body").toString().endsWith("This is some body text"));
		
		assertEquals("the new location", out.getPropertySet("httpHeader").get("Location"));
		assertEquals("the default arbitrary value", out.getPropertySet("httpHeader").get("Arbitrary"));
		
		assertEquals("<!DOCTYPE mydoc>", out.getStream("docType").toString());
		
		// now try basically the same thing except with a template that has hints which are 
		// reversed from the defaults.
		ti = ts.get("override2");
		out = prepOutput();
		ti.render(out, loc, null, null);
		
		assertFalse(out.getList("cssIncludes").contains("default.css"));
		assertTrue(out.getList("cssIncludes").contains("one.css"));
		assertTrue(out.getList("cssIncludes").contains("two.css"));
		assertTrue(out.getList("cssIncludes").contains("three.css"));
		
		assertFalse(out.getStream("body").toString().startsWith("start:"));
		assertTrue(out.getStream("body").toString().endsWith("This is some body text"));
		
		assertEquals("the default locationthe new location", out.getPropertySet("httpHeader").get("Location"));
		assertEquals("the default arbitrary value", out.getPropertySet("httpHeader").get("Arbitrary"));
		
		assertEquals("<!DOCTYPE html><!DOCTYPE mydoc>", out.getStream("docType").toString());
		

	}
	
	protected SegmentedOutput prepOutput() {
		SegmentedOutput out = new HTML5SegmentedOutput();
		out.getList("cssIncludes").add("default.css");
		out.getStream("body").append("start:");
		out.getPropertySet("httpHeader").put("Location", "the default location");
		out.getPropertySet("httpHeader").put("Arbitrary", "the default arbitrary value");
		return out;
	}
}
