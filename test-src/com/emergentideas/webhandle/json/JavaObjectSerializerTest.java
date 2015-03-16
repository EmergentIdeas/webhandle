package com.emergentideas.webhandle.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;


import com.emergentideas.webhandle.output.SegmentedOutput;

import static org.junit.Assert.*;

public class JavaObjectSerializerTest<T extends Object> {

	@Test
	public void testCollection() throws Exception {
		List<String> l = new ArrayList<String>();
		JavaObjectSerializer ser = new JavaObjectSerializer();
		assertTrue(ser.isSimpleEnoughType(l, l.getClass(), l.getClass().getTypeParameters()[0]));
		
		l.add("hello");
		assertTrue(ser.isSimpleEnoughType(l, l.getClass(), l.getClass().getTypeParameters()[0]));
		
		((List)l).add(15);
		assertTrue(ser.isSimpleEnoughType(l, l.getClass(), l.getClass().getTypeParameters()[0]));
		
		((List)l).add(new Double(15));
		assertTrue(ser.isSimpleEnoughType(l, l.getClass(), l.getClass().getTypeParameters()[0]));
		
		((List)l).add(new Serializer1());
		assertFalse(ser.isSimpleEnoughType(l, l.getClass(), l.getClass().getTypeParameters()[0]));
		
	}
	
	@Test
	public void testMap() throws Exception {
		Map<Object, Object> m = new HashMap<Object, Object>();
		JavaObjectSerializer ser = new JavaObjectSerializer();
		
		m.put("hello", "there");
		assertTrue(ser.isSimpleEnoughType(m, m.getClass(), m.getClass().getTypeParameters()[0]));
		
		m.put("age", 35);
		assertTrue(ser.isSimpleEnoughType(m, m.getClass(), m.getClass().getTypeParameters()[0]));
		
		m.put("serializer", new Serializer1());
		assertFalse(ser.isSimpleEnoughType(m, m.getClass(), m.getClass().getTypeParameters()[0]));
		
		m.remove("serializer");
		assertTrue(ser.isSimpleEnoughType(m, m.getClass(), m.getClass().getTypeParameters()[0]));
		
		m.put(new Serializer1(), "35");
		assertFalse(ser.isSimpleEnoughType(m, m.getClass(), m.getClass().getTypeParameters()[0]));
		
	}
	
	@Test
	public void testSimpleObject() throws Exception {
		AnnotationDrivenJSONSerializer ser = new AnnotationDrivenJSONSerializer();
		JavaObjectSerializer jos = new JavaObjectSerializer();
		ser.add(jos);
		ser.add(new NumberSerializer());
		ser.add(new StringSerializer());
		ser.add(new DateSerializer());
		ser.add(new BooleanSerializer());
		ser.add(new ByteArraySerializer());
		
		SegmentedOutput output = new SegmentedOutput();
		ser.serialize(output, new SimpleData1());
		String result = output.getStream("body").toString();
		System.out.println(result);
		
		assertTrue(result.contains("\"greeting\""));
		assertTrue(result.contains("\"age\""));
		assertTrue(result.contains("\"cakeWeight\""));
		assertTrue(result.contains("\"todaysDate\""));
		assertTrue(result.contains("\"actuallyNull\""));
		assertTrue(result.contains("\"justStrings\""));
		assertTrue(result.contains("\"ints\""));
		assertTrue(result.contains("\"doubles\""));
		assertTrue(result.contains("\"myBytes\": \"d29ybGQ=\""));
		
		
		assertFalse(result.contains("\"lotsOfStuff\""));
		assertFalse(result.contains("\"shouldNotSee\""));
		assertFalse(result.contains("\"serializer\""));
		
		output = new SegmentedOutput();
		ser.serialize(output, new SimpleData1(), "bytes-as-array");
		result = output.getStream("body").toString();
		assertTrue(result.contains("\"myBytes\": [119, 111, 114, 108, 100]"));
		
		output = new SegmentedOutput();
		ser.serialize(output, new SimpleData1(), "default");
		result = output.getStream("body").toString();
		assertFalse(result.contains("new Date("));
		
		output = new SegmentedOutput();
		ser.serialize(output, new SimpleData1(), "date-as-string");
		result = output.getStream("body").toString();
		assertFalse(result.contains("new Date("));

		output = new SegmentedOutput();
		ser.serialize(output, new SimpleData1(), "date-as-millis");
		result = output.getStream("body").toString();
		assertFalse(result.contains("Z"));
	}

	
	
}
