package com.emergentideas.webhandle.templates.instances;

import org.junit.Test;

import static junit.framework.Assert.*;

public class EscapeHTMLTest {

	@Test
	public void testEscapeHTML() throws Exception {
		EscapeHTML esc = new EscapeHTML();
		String s = esc.escape("hello < > \" there");
		assertFalse(s.contains("<"));
		assertFalse(s.contains(">"));
		assertFalse(s.contains("\""));
		assertTrue(s.contains("&gt;"));
		assertTrue(s.contains("&lt;"));
		assertTrue(s.contains("&quot;"));
	}
}
