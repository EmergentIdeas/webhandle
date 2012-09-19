package com.emergentideas.webhandle.random;

import static junit.framework.Assert.*;

import java.util.Properties;

import org.junit.Test;

import com.emergentideas.utils.StringUtils;

public class PropertiesTest {

	@Test
	public void testPropBehavior() throws Exception {
		Properties props = new Properties();
		props.load(StringUtils.getStreamFromClassPathLocation("com/emergentideas/webhandle/random/test1.properties"));
		
		assertTrue(props.keySet().contains("css/with/no/value.css"));
		assertTrue(props.keySet().contains("css/with/a/media/query.css"));
		
		assertEquals("@print", props.get("css/with/a/media/query.css"));
	}
	
}
