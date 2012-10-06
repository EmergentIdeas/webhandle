package com.emergentideas.webhandle.assumptions.oak;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.emergentideas.webhandle.AppLocation;
import com.emergentideas.webhandle.Location;
import com.emergentideas.webhandle.TestObj;
import com.emergentideas.webhandle.WebAppLocation;
import com.emergentideas.webhandle.assumptions.oak.CompositeTemplateSource;
import com.emergentideas.webhandle.output.SegmentedOutput;
import com.emergentideas.webhandle.templates.JexlExpressionFactory;
import com.emergentideas.webhandle.templates.TemplateInstance;
import com.emergentideas.webhandle.templates.TripartateFileTemplateSource;
import com.emergentideas.webhandle.templates.TripartateTemplate;

public class CompositeTemplateSourceTest {

	
	@Test
	public void testRender() throws Exception {
		TripartateFileTemplateSource ts = new TripartateFileTemplateSource(new File("WebContent/WEB-INF/testTemplates"));
		ts.setExpressionFactory(new JexlExpressionFactory());
		ts.init();
		Location loc = new AppLocation();
		WebAppLocation web = new WebAppLocation(loc);
		web.init();
		
		LibraryTemplateSource lts = new LibraryTemplateSource();
		lts.add("arbitrary", new TemplateInstance() {
			
			public void render(SegmentedOutput output, Location location, String elementSourceName, String... processingHints) {
				output.getStream(elementSourceName).append("this is some arbitrary text");
			}
		});
		
		
		CompositeTemplateSource cts = new CompositeTemplateSource();
		cts.addTemplateSource(ts);
		cts.addTemplateSource(lts);
		
		web.setTemplateSource(cts);
		
		TripartateTemplate tt = (TripartateTemplate)cts.get("two");
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
		tt.render(out, loc, null, null);
		
		assertEquals("hello there myid", out.getStream("body").toString());
		
		tt = (TripartateTemplate)cts.get("three");
		out = new SegmentedOutput();
		tt.render(out, loc, null, null);
		
		String body = out.getStream("body").toString();
		assertTrue(body.contains("is:AA"));
		assertTrue(body.contains("child2.b is:B2"));
		assertTrue(body.contains("manyChildren is:B2B3"));
		
		
		tt = (TripartateTemplate)cts.get("five");
		out = new SegmentedOutput();
		tt.render(out, loc, null, null);
		
		body = out.getStream("body").toString();
		assertTrue(body.contains("this is some arbitrary text"));
		
		
	}

}
