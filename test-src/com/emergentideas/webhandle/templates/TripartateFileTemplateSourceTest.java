package com.emergentideas.webhandle.templates;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.emergentideas.utils.FileUtils;
import com.emergentideas.webhandle.AppLocation;
import com.emergentideas.webhandle.Location;
import com.emergentideas.webhandle.TestObj;
import com.emergentideas.webhandle.WebAppLocation;
import com.emergentideas.webhandle.output.SegmentedOutput;

public class TripartateFileTemplateSourceTest {

	@Test
	public void testLoadAllParts() throws Exception {
		TripartateFileTemplateSource ts = new TripartateFileTemplateSource(new File("WebContent/WEB-INF/testTemplates"));
		
		TripartateTemplate tt = (TripartateTemplate)ts.get("one");
		
		assertEquals("hello there", tt.sections.get("body"));
		assertEquals("/css/sheet.css", tt.sections.get("cssInclude"));
		assertEquals(FileUtils.getFileAsString(new File("WebContent/WEB-INF/testTemplates/one.header")), tt.sections.get("header"));
		
	}
	
	@Test
	public void testLoadSub() throws Exception {
		TripartateFileTemplateSource ts = new TripartateFileTemplateSource(new File("WebContent/WEB-INF/testTemplates"));
		TripartateTemplate tt = (TripartateTemplate)ts.get("sub/five");
		assertNotNull(tt);
	}
	
	@Test
	public void testRender() throws Exception {
		TripartateFileTemplateSource ts = new TripartateFileTemplateSource(new File("WebContent/WEB-INF/testTemplates"));
		ts.setExpressionFactory(new JexlExpressionFactory());
		ts.init();
		TripartateTemplate tt = (TripartateTemplate)ts.get("two");
		Location loc = new AppLocation();
		WebAppLocation web = new WebAppLocation(loc);
		web.init();
		web.setTemplateSource(ts);
		
		TestObj obj = new TestObj("AA", "BB");
		obj.setId("myid");
		
		TestObj obj2 = new TestObj("A2", "B2");
		obj.setChild2(obj2);
		
		TestObj obj3 = new TestObj("A3", "B3");
		
		List<TestObj> manyChildren = new ArrayList<TestObj>();
		manyChildren.add(obj2);
		manyChildren.add(obj3);
		obj.setManyChildren(manyChildren);

		
		
		loc.add(obj);
		
		SegmentedOutput out = new SegmentedOutput();
		tt.render(out, loc, "body", null);
		
		assertEquals("hello there myid", out.getStream("body").toString());
		
		tt = (TripartateTemplate)ts.get("three");
		out = new SegmentedOutput();
		tt.render(out, loc, null, null);
		
		String body = out.getStream("body").toString();
		assertTrue(body.contains("is:AA"));
		assertTrue(body.contains("child2.b is:B2"));
		assertTrue(body.contains("manyChildren is:B2B3"));
		
	}
	
	@Test
	public void testVariableTemplateName() throws Exception {
		TripartateFileTemplateSource ts = new TripartateFileTemplateSource(new File("WebContent/WEB-INF/testTemplates"));
		ts.setExpressionFactory(new JexlExpressionFactory());
		ts.init();
		TripartateTemplate tt = (TripartateTemplate)ts.get("variableTemplateName");
		Location loc = new AppLocation();
		WebAppLocation web = new WebAppLocation(loc);
		web.init();
		web.setTemplateSource(ts);
		
		loc.put("theTemplateName", "one");
		
		SegmentedOutput out = new SegmentedOutput();
		tt.render(out, loc, null, null);
		
		
		
		String body = out.getStream("body").toString();
		assertEquals("hello there", body);
	}
	
	@Test
	public void testIteratorCount() throws Exception {
		TripartateFileTemplateSource ts = new TripartateFileTemplateSource(new File("WebContent/WEB-INF/testTemplates"));
		ts.setExpressionFactory(new JexlExpressionFactory());
		ts.init();
		TripartateTemplate tt = (TripartateTemplate)ts.get("outerIterator");
		Location loc = new AppLocation();
		WebAppLocation web = new WebAppLocation(loc);
		web.init();
		web.setTemplateSource(ts);
		
		
		
		loc.put("listOf5", Arrays.asList(new String[] { "hello", "there", "to", "you", "world" }));
		
		SegmentedOutput out = new SegmentedOutput();
		tt.render(out, loc, null, null);
		
		
		
		String body = out.getStream("body").toString();
		assertEquals("01234", body);
	}

	
}
