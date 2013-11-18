package com.emergentideas.webhandle.json;

import static org.junit.Assert.*;

import org.junit.Test;

public class AnnotationDrivenJSONSerializerTest {
	
	@Test
	public void testFindsProfiles() throws Exception {
		AnnotationDrivenJSONSerializer s = new AnnotationDrivenJSONSerializer();
		
		assertArrayEquals(new String[] {"default", "one"}, s.findProfiles(new Serializer1()));
	}
	
	@Test
	public void testFindsType() throws Exception {
		AnnotationDrivenJSONSerializer s = new AnnotationDrivenJSONSerializer();
		assertEquals(String.class, s.findTypeSerialized(new Serializer1()));
		assertEquals(Number.class, s.findTypeSerialized(new Serializer2()));
	}
	
	@Test
	public void testBestSerializer() throws Exception {
		AnnotationDrivenJSONSerializer s = new AnnotationDrivenJSONSerializer();
		s.add(new Serializer1());
		s.add(new Serializer2());
		s.add(new Serializer3());
		
		assertEquals(Serializer2.class, s.determineBestSerializer(Integer.class, "default").getClass());
		assertEquals(Serializer1.class, s.determineBestSerializer(String.class, "default").getClass());
		assertEquals(Serializer3.class, s.determineBestSerializer(Integer.class, "two").getClass());
		assertNull(s.determineBestSerializer(Integer.class, "not-a-real-profile"));
	}
	
	@Test
	public void testByteArraySerializer() throws Exception {
		AnnotationDrivenJSONSerializer s = new AnnotationDrivenJSONSerializer();
		s.add(new Serializer1());
		s.add(new Serializer2());
		s.add(new Serializer3());
		s.add(new Serializer6());
		
		
		assertEquals(Serializer6.class, s.determineBestSerializer(Object.class, "default").getClass());
		assertNull(s.determineBestSerializer(byte[].class, "default"));
		s.add(new Serializer5());
		assertEquals(Serializer5.class, s.determineBestSerializer(byte[].class, "default").getClass());
	}
	
	@Test
	public void testFindSerializer() throws Exception {
		AnnotationDrivenJSONSerializer s = new AnnotationDrivenJSONSerializer();
		s.add(new Serializer1());
		s.add(new Serializer2());
		s.add(new Serializer3());
		
		assertEquals(Serializer2.class, s.determineSerializer(Integer.class, "default").getClass());
		assertEquals(Serializer1.class, s.determineSerializer(String.class, "default").getClass());
		assertEquals(Serializer3.class, s.determineSerializer(Integer.class, "two").getClass());
		assertNull(s.determineBestSerializer(Integer.class, "not-a-real-profile"));
		
		// Test that multiple profiles work
		assertEquals(Serializer3.class, s.determineSerializer(Integer.class, "two", "default").getClass());
		assertEquals(Serializer2.class, s.determineSerializer(Integer.class, "not-a-real-profile", "default").getClass());
		
		
		// Test that the new serializer is used by preference
		s.add(new Serializer4());
		assertEquals(Serializer4.class, s.determineSerializer(Integer.class, "two", "default").getClass());
		
	}
	
	@Test
	public void testCacheUsed() throws Exception {
		AnnotationDrivenJSONSerializer s = new AnnotationDrivenJSONSerializer();
		s.add(new Serializer1());
		s.add(new Serializer2());
		s.add(new Serializer3());
		
		assertEquals(Serializer2.class, s.determineSerializer(Integer.class, "default").getClass());
		assertEquals(Serializer1.class, s.determineSerializer(String.class, "default").getClass());
		assertEquals(Serializer3.class, s.determineSerializer(Integer.class, "two").getClass());
		assertNull(s.determineBestSerializer(Integer.class, "not-a-real-profile"));
		
		// sneakily clear the list of all the profiles so that only the cached versions will be available
		s.allSerializers.clear();
		
		assertEquals(Serializer2.class, s.determineSerializer(Integer.class, "default").getClass());
		assertEquals(Serializer1.class, s.determineSerializer(String.class, "default").getClass());
		assertEquals(Serializer3.class, s.determineSerializer(Integer.class, "two").getClass());
		assertNull(s.determineBestSerializer(Integer.class, "not-a-real-profile"));
	}


}
