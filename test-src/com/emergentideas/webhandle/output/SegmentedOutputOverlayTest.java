package com.emergentideas.webhandle.output;

import static junit.framework.Assert.*;

import org.junit.Test;

public class SegmentedOutputOverlayTest {

	@Test
	public void testOverlay() {
		SegmentedOutput output = new SegmentedOutput();
		SegmentedOutputOverlay overlay = new SegmentedOutputOverlay(output, "test");
		
		overlay.getStream("one").append("hello");
		assertEquals("hello", output.getStream("one").toString());
		
		overlay.getStream("test").append("hello");
		assertEquals("", output.getStream("test").toString());
		
		overlay.getList("test").add("world");
		assertEquals("world", output.getList("test").get(0));
	}
}
