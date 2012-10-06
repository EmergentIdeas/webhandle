package com.emergentideas.webhandle.templates;

import static junit.framework.Assert.*;

import org.junit.Test;

public class TripartateClasspathTemplateSourceTest {

	@Test
	public void testLoad() throws Exception {
		TripartateClasspathTemplateSource source = new TripartateClasspathTemplateSource("com.emergentideas.webhandle.templates.testTemplates");
		source.setExpressionFactory(new JexlExpressionFactory());
		source.init();
		TripartateTemplate tt = (TripartateTemplate)source.get("one");
		assertEquals("one body", tt.sections.get("body"));
		assertEquals("stream,replace", tt.hints.getProperty("title"));
		
		tt = (TripartateTemplate)source.get("sub/two");
		assertEquals("two title", tt.sections.get("title"));
		assertEquals("stream,append", tt.hints.getProperty("title"));
	}
}
