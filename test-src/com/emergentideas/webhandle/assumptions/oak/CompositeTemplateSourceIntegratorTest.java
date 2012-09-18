package com.emergentideas.webhandle.assumptions.oak;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

import com.emergentideas.utils.StringUtils;
import com.emergentideas.webhandle.WebAppLocation;
import com.emergentideas.webhandle.output.SegmentedOutput;
import com.emergentideas.webhandle.templates.TemplateInstance;
import com.emergentideas.webhandle.templates.TemplateSource;

public class CompositeTemplateSourceIntegratorTest {
	
	@Test
	public void testIntegration() throws Exception {
		AppLoader loader = new AppLoader();
		loader.load(StringUtils.getStreamFromClassPathLocation("com/emergentideas/webhandle/assumptions/oak/test1.conf"), new File("").getAbsoluteFile());
		
		WebAppLocation webApp = new WebAppLocation(loader.getLocation());
		TemplateSource ts = webApp.getTemplateSource();
		
		assertNotNull(ts);
		assertTrue(ts instanceof CompositeTemplateSource);
		
		CompositeTemplateSource cts = (CompositeTemplateSource)ts;
		assertEquals(2, cts.getSources().size());
		assertTrue(cts.getSources().get(0) instanceof LibraryTemplateSource);
		
		assertNotNull(ts.get("template1"));
		assertNull(ts.get("Template1"));
		
		
		TemplateInstance ti = ts.get("template2");
		assertNotNull(ti);
		
		SegmentedOutput output = new SegmentedOutput();
		ti.render(output, loader.getLocation(), null, null);
		assertEquals("hello world", output.getStream("body").toString());
	}
}
