package com.emergentideas.webhandle.templates;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Arrays;

import org.junit.Test;

import com.emergentideas.webhandle.AppLocation;
import com.emergentideas.webhandle.Location;
import com.emergentideas.webhandle.WebAppLocation;
import com.emergentideas.webhandle.output.SegmentedOutput;

public class TripartateListHandlingTest {
	
	@Test
	public void testListHandling() throws Exception {
		
		TripartateFileTemplateSource ts = new TripartateFileTemplateSource(new File("WebContent/WEB-INF/testTemplates"));
		ts.setExpressionFactory(new JexlExpressionFactory());
		ts.init();
		TripartateTemplate tt = (TripartateTemplate)ts.get("list-with-comma");
		Location loc = new AppLocation();
		WebAppLocation web = new WebAppLocation(loc);
		web.init();
		web.setTemplateSource(ts);
		
		
		
		loc.put("theList", Arrays.asList(new String[] { "hello", "there", "to", "you", "world" }));
		
		SegmentedOutput out = new SegmentedOutput();
		tt.render(out, loc, null, null);
		
		
		
		String body = out.getStream("body").toString();
		assertEquals("hello, there, to, you, world", body);
	}
}
