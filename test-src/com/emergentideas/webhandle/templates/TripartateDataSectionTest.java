package com.emergentideas.webhandle.templates;

import static junit.framework.Assert.*;

import org.junit.Test;

import com.emergentideas.webhandle.AppLocation;
import com.emergentideas.webhandle.WebAppLocation;
import com.emergentideas.webhandle.output.SegmentedOutput;

public class TripartateDataSectionTest {

	@Test
	public void testLoad() throws Exception {
		TripartateClasspathTemplateSource source = new TripartateClasspathTemplateSource("com.emergentideas.webhandle.templates.testTemplates");
		source.setExpressionFactory(new JexlExpressionFactory());
		source.init();
		
		assertEquals("hello to my data", getBodyFromTemplate(source, "three"));
		
		assertEquals("four:hello to my data", getBodyFromTemplate(source, "four"));

	}
	
	protected String getBodyFromTemplate(TemplateSource source, String templateName) {
		TripartateTemplate tt = (TripartateTemplate)source.get(templateName);
		
		SegmentedOutput output = new SegmentedOutput();
		
		AppLocation location = new AppLocation();
		WebAppLocation wal = new WebAppLocation(location);
		wal.setTemplateSource(source);
		
		tt.render(output, location, "body", null);
		
		return output.getStream("body").toString();
	}
}
