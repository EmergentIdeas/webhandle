package com.emergentideas.webhandle.json;

import static org.junit.Assert.*;

import org.junit.Test;

import com.emergentideas.webhandle.output.SegmentedOutput;

public class ArraySerializerTest {

	@Test
	public void testSerializePrimitiveArrays() throws Exception {
		ArraySerializer as = new ArraySerializer();
		AnnotationDrivenJSONSerializer ser = new AnnotationDrivenJSONSerializer();
		ser.add(new NumberSerializer());
		ser.add(as);
		SegmentedOutput output = new SegmentedOutput();
		as.serialize(ser, output, new int[] { 1, 2, 3}, "default");
		assertEquals("[1, 2, 3]", output.getStream("body").toString());
	}
}
