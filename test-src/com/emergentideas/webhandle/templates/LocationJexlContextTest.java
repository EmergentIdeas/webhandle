package com.emergentideas.webhandle.templates;

import static junit.framework.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.emergentideas.utils.LocationJexlContext;
import com.emergentideas.webhandle.AppLocation;

public class LocationJexlContextTest {

	@Test
	public void testReplacements() throws Exception {
		AppLocation location = new AppLocation();
		Map<String,String> replacements = new HashMap<String, String>();
		
		LocationJexlContext context = new LocationJexlContext(location, replacements);
		
		location.put("one", "theone");
		
		assertEquals("theone", context.get("one"));
		assertNull(context.get("$two/three"));
		
		replacements.put("two/three", "one");
		assertEquals("theone", context.get("one"));
		assertEquals("theone", context.get("two/three"));
		
	}
}
