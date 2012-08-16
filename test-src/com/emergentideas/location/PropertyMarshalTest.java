package com.emergentideas.location;

import java.lang.reflect.Method;

import org.junit.Test;

import com.emergentideas.location.investigators.NameAnnotationPropertyNameInvestigator;
import com.emergentideas.location.investigators.SourceAnnotationSourceSetInvestigator;
import com.emergentideas.location.investigators.TransformersAnnotationTransformersInvestigator;
import com.emergentideas.location.transformers.NumberToStringTransformer;
import com.emergentideas.location.transformers.StringArrayToStringTransformer;
import com.emergentideas.location.transformers.StringToDoubleTransformer;
import com.emergentideas.location.transformers.StringToIntegerTransformer;

import static org.junit.Assert.*;

public class PropertyMarshalTest {

	@Test
	public void testGetInitialObject() {
		PropertyMarshal marshal = new PropertyMarshal();
		
	}
	
	@Test
	public void testReflectionMethods() {
		PropertyMarshal marshal = new PropertyMarshal();
		assertFalse(marshal.isArrayType("hello"));
		assertTrue(marshal.isArrayType(new String[0]));
		
		assertTrue(marshal.makeArrayFromObject("hello") instanceof String[]);
		
		assertTrue(marshal.getArrayStyle(String.class) == String[].class);
		assertTrue(marshal.getArrayStyle(String[].class) == String[].class);
		
		ValueTransformer transformer = new NumberToStringTransformer();
		
		assertTrue(marshal.canRun(new Integer[0], transformer));
		assertTrue(marshal.canRun(new Double[0], transformer));
		assertFalse(marshal.canRun(new String[0], transformer));

		assertTrue(marshal.isReturnTypeCompatible(String[].class, transformer));
		assertTrue(marshal.isReturnTypeCompatible(CharSequence[].class, transformer));
		assertFalse(marshal.isReturnTypeCompatible(Integer[].class, transformer));
		
		marshal.getTypeTransformers().add(new StringToIntegerTransformer());
		marshal.getTypeTransformers().add(new StringToDoubleTransformer());
		
		Integer[] ints = (Integer[])marshal.convert(new String[] {"12", "15", "16.6"}, Integer[].class);;
		
		assertEquals(3, ints.length);
		
		assertEquals((Integer)12, ints[0]);
		assertEquals((Integer)15, ints[1]);
		assertEquals((Integer)16, ints[2]);

		Integer i = (Integer)marshal.getTypedParameter(null, Integer.class, new String[] {"12", "15", "16.6"}, null);
		assertEquals((Integer)12, i);
		
		marshal.getTypeTransformers().add(new StringArrayToStringTransformer());
		String s = (String)marshal.getTypedParameter(null, String.class, new String[] {"12", "15", "16.6"}, null);
		assertEquals("12,15,16.6", s);
	}
	
	@Test
	public void testTransformersRun() {
		PropertyMarshal marshal = new PropertyMarshal();
		marshal.getTypeTransformers().add(new StringToIntegerTransformer());
		marshal.getTypeTransformers().add(new StringToDoubleTransformer());
		
		Integer i = (Integer)marshal.getTypedParameter(null, Integer.class, new String[] {"12", "15", "16.6"}, null);
		assertEquals((Integer)12, i);
		
		marshal.getTransformers().put("append", new ValueTransformer<String, String[]>() {

			public String[] transform(InvocationContext context, String parameterName, String... source) {
				String[] result = new String[source.length];
				for(int i = 0; i < source.length; i++) {
					result[i] = source[i] + "2";
				}
				return result;
			}
		});
		
		i = (Integer)marshal.getTypedParameter(null, Integer.class, new String[] {"12", "15", "16.6"}, null);
		assertEquals((Integer)12, i);
		
		i = (Integer)marshal.getTypedParameter(null, Integer.class, new String[] {"12", "15", "16.6"}, "append");
		assertEquals((Integer)122, i);
		
		marshal.getTransformers().put("add10", new ValueTransformer<Integer, Integer[]>() {

			public Integer[] transform(InvocationContext context, String parameterName, Integer... source) {
				Integer[] result = new Integer[source.length];
				for(int i = 0; i < source.length; i++) {
					result[i] = source[i] + 10;
				}
				return result;
			}
		});
		
		i = (Integer)marshal.getTypedParameter(null, Integer.class, new String[] {"12", "15", "16.6"}, "append");
		assertEquals((Integer)122, i);
		
		i = (Integer)marshal.getTypedParameter(null, Integer.class, new String[] {"12", "15", "16.6"}, "append", "add10");
		assertEquals((Integer)132, i);
	}
	
	@Test
	public void testDeterminePropertyName() throws Exception {
		PropertyMarshal marshal = new PropertyMarshal();
		
		
		Method mTest1 = getTestObjMethodTest1();
		
		String name = marshal.determinePropertyName(null, mTest1, 
				String.class, mTest1.getParameterAnnotations()[0]);
		assertNull( name);

		// once we add the investigator we should get the property name
		marshal.getPropertyNameInvestigators().add(new NameAnnotationPropertyNameInvestigator());
		
		name = marshal.determinePropertyName(null, mTest1, 
				String.class, mTest1.getParameterAnnotations()[0]);
		assertEquals("one", name);

		name = marshal.determinePropertyName(null, mTest1, 
				String.class, mTest1.getParameterAnnotations()[1]);
		assertNull(name);
		
	}
	
	@Test
	public void testDetermineParameterClass() throws Exception {
		PropertyMarshal marshal = new PropertyMarshal();
		Method mTest1 = getTestObjMethodTest1();
		
		assertEquals(String.class, marshal.determineParameterClass(new TestObj(), mTest1, 0));
		assertEquals(Integer[].class, marshal.determineParameterClass(new TestObj(), mTest1, 1));
	}
	
	@Test
	public void testSourceSets() throws Exception {
		PropertyMarshal marshal = new PropertyMarshal();
		Method mTest1 = getTestObjMethodTest1();
		
		marshal.getSourceSetInvestigators().add(new SourceAnnotationSourceSetInvestigator());
		
		assertNull(marshal.determineAllowedSourceSets(new TestObj(), mTest1, String.class, null, mTest1.getParameterAnnotations()[0]));
		assertArrayEquals(new String[] { "request" }, marshal.determineAllowedSourceSets(new TestObj(), mTest1, Integer[].class, null, mTest1.getParameterAnnotations()[1]));
	}
	
	@Test
	public void testTransformers() throws Exception {
		PropertyMarshal marshal = new PropertyMarshal();
		Method mTest1 = getTestObjMethodTest1();
		
		marshal.getTransformersInvestigators().add(new TransformersAnnotationTransformersInvestigator());
		
		assertArrayEquals(new String[] { }, marshal.determineTransformers(new TestObj(), mTest1, String.class, null, mTest1.getParameterAnnotations()[0]));
		assertArrayEquals(new String[] { "one", "two" }, marshal.determineTransformers(new TestObj(), mTest1, Integer[].class, null, mTest1.getParameterAnnotations()[1]));
		
		
		marshal.getTransformersInvestigators().add(new TransformersAnnotationTransformersInvestigator());
		
		// Confirming that we're using all the investigators and concatenating their output
		assertArrayEquals(new String[] { "one", "two", "one", "two" }, marshal.determineTransformers(new TestObj(), mTest1, Integer[].class, null, mTest1.getParameterAnnotations()[1]));
	}
	
	protected Method getTestObjMethodTest1() throws Exception {
		Method mTest1 = TestObj.class.getMethod("test1", String.class, Integer[].class);
		return mTest1;
	}
	
	@Test
	public void testIsAllowed() {
		PropertyMarshal marshal = new PropertyMarshal();
		assertTrue(marshal.isSourceAllowed("hello", null));
		assertTrue(marshal.isSourceAllowed("hello", new String[0]));
		assertTrue(marshal.isSourceAllowed("hello", new String[] { "hello", "goodbye"}));
		assertFalse(marshal.isSourceAllowed("hello", new String[] {"goodbye"}));

	}
}
