package com.emergentideas.location;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.emergentideas.location.composites.db.DbInvestigator;
import com.emergentideas.location.configurations.WebParameterMarsahalConfiguration;
import com.emergentideas.location.investigators.NameAnnotationPropertyNameInvestigator;
import com.emergentideas.location.investigators.SourceAnnotationSourceSetInvestigator;
import com.emergentideas.location.investigators.TransformersAnnotationTransformersInvestigator;
import com.emergentideas.location.sources.MapValueSource;
import com.emergentideas.location.transformers.NumberToStringTransformer;
import com.emergentideas.location.transformers.StringArrayToStringTransformer;
import com.emergentideas.location.transformers.StringToDoubleTransformer;
import com.emergentideas.location.transformers.StringToIntegerTransformer;
import com.emergentideas.location.transformers.TransformerSpecification;

import static org.junit.Assert.*;

public class ParameterMarshalTest {

	@Test
	public void testGetInitialObject() {
		ParameterMarshal marshal = new ParameterMarshal();
		
	}
	
	@Test
	public void testReflectionMethods() {
		ParameterMarshal marshal = new ParameterMarshal();
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
		
		marshal.getConfiguration().getTypeTransformers().add(new StringToIntegerTransformer());
		marshal.getConfiguration().getTypeTransformers().add(new StringToDoubleTransformer());
		
		Integer[] ints = (Integer[])marshal.convert(new String[] {"12", "15", "16.6"}, Integer[].class);;
		
		assertEquals(3, ints.length);
		
		assertEquals((Integer)12, ints[0]);
		assertEquals((Integer)15, ints[1]);
		assertEquals((Integer)16, ints[2]);

		Integer i = (Integer)marshal.getTypedParameter(null, Integer.class, null, new String[] {"12", "15", "16.6"}, null);
		assertEquals((Integer)12, i);
		
		marshal.getConfiguration().getTypeTransformers().add(new StringArrayToStringTransformer());
		String s = (String)marshal.getTypedParameter(null, String.class, null, new String[] {"12", "15", "16.6"}, null);
		assertEquals("12,15,16.6", s);
		
		List<String> l = (List<String>)marshal.getTypedParameter(null, List.class, null, new String[] {"12", "15", "16.6"}, null);
		assertNotNull(l);
		assertEquals(3, l.size());
		assertEquals("12", l.get(0));
		assertEquals("15", l.get(1));
		assertEquals("16.6", l.get(2));
		
		Set<String> set = (Set<String>)marshal.getTypedParameter(null, Set.class, null, new String[] {"12", "15", "16.6"}, null);
		assertNotNull(set);
		assertEquals(3, set.size());
		assertTrue(set.contains("12"));
		assertTrue(set.contains("15"));
		assertTrue(set.contains("16.6"));
		
	}
	
	@Test
	public void testGetMethodParameters() throws Exception {
		ParameterMarshal marshal = new ParameterMarshal(new WebParameterMarsahalConfiguration());
		
		Method m = getTestObjMethodTest1();
		TestObj obj = new TestObj();
		
		Integer[] intArgs1 = new Integer[] {2, 3}; 
		
		Map<String, Object> values = new HashMap<String, Object>();
		values.put("one", "1");
		values.put("two", intArgs1);
		
		
		MapValueSource source = new MapValueSource(values);
		marshal.getSources().put("request", source);
		
		Object parameter = marshal.getTypedParameter(obj, m, 0);
		assertEquals("1", parameter);
		parameter = marshal.getTypedParameter(obj, m, 1);
		assertArrayEquals(intArgs1, (Object[])parameter);
		
		
		values.put("two", new String[] {"2", "3"});
		parameter = marshal.getTypedParameter(obj, m, 1);
		assertArrayEquals(intArgs1, (Object[])parameter);
		
		List l = (List)marshal.getTypedParameter(obj, m, 2);
		assertEquals(2.0, l.get(0));
		assertEquals(3.0, l.get(1));
		
	}
	
	@Test
	public void testCall() throws Exception {
		ParameterMarshal marshal = new ParameterMarshal(new WebParameterMarsahalConfiguration());
		
		Method m = getTestObjMethodTest1();
		TestObj obj = new TestObj();
		
		Integer[] intArgs1 = new Integer[] {2, 3}; 
		
		Map<String, Object> values = new HashMap<String, Object>();
		values.put("one", "1");
		values.put("two", new String[] {"2", "3"});
		
		
		MapValueSource source = new MapValueSource(values);
		marshal.getSources().put("request", source);
		
		assertEquals("1-5-5.0", marshal.call(obj, m));
		
		// Test to make sure the invocation context was populated
		InvocationContext context = marshal.getContext();
		assertEquals("1", context.getFoundParameter("one", String.class));
		assertNull(context.getFoundParameter(String.class));
		
		// Add to the context a value which could not come from the sources to be sure it is
		// used preferentially.
		context.setFoundParameter("two", Integer[].class, new Integer[] { 3, 5 });
		assertEquals("1-8-5.0", marshal.call(obj, m));
		
		
	}

	
	@Test
	public void testTransformersRun() {
		ParameterMarshal marshal = new ParameterMarshal();
		marshal.getConfiguration().getTypeTransformers().add(new StringToIntegerTransformer());
		marshal.getConfiguration().getTypeTransformers().add(new StringToDoubleTransformer());
		
		Integer i = (Integer)marshal.getTypedParameter(null, Integer.class, null, new String[] {"12", "15", "16.6"}, null);
		assertEquals((Integer)12, i);
		
		marshal.getConfiguration().getTransformers().put("append", new ValueTransformer<String, String, String[]>() {

			public String[] transform(InvocationContext context, Map<String, String> transformationProperties, String parameterName, String... source) {
				String[] result = new String[source.length];
				for(int i = 0; i < source.length; i++) {
					result[i] = source[i] + "2";
				}
				return result;
			}
		});
		
		i = (Integer)marshal.getTypedParameter(null, Integer.class, null, new String[] {"12", "15", "16.6"}, null);
		assertEquals((Integer)12, i);
		
		i = (Integer)marshal.getTypedParameter(null, Integer.class, null, new String[] {"12", "15", "16.6"}, new TransformerSpecification("append"));
		assertEquals((Integer)122, i);
		
		marshal.getConfiguration().getTransformers().put("add10", new ValueTransformer<String, Integer, Integer[]>() {

			public Integer[] transform(InvocationContext context, Map<String, String> transformationProperties, String parameterName, Integer... source) {
				Integer[] result = new Integer[source.length];
				for(int i = 0; i < source.length; i++) {
					result[i] = source[i] + 10;
				}
				return result;
			}
		});
		
		i = (Integer)marshal.getTypedParameter(null, Integer.class, null, new String[] {"12", "15", "16.6"}, new TransformerSpecification("append"));
		assertEquals((Integer)122, i);
		
		i = (Integer)marshal.getTypedParameter(null, Integer.class, null, new String[] {"12", "15", "16.6"}, new TransformerSpecification("append"), new TransformerSpecification("add10"));
		assertEquals((Integer)132, i);
		
		marshal.getConfiguration().getTransformers().put("addArbitrary", new ValueTransformer<String, Integer, Integer[]>() {

			public Integer[] transform(InvocationContext context, Map<String, String> transformationProperties, String parameterName, Integer... source) {
				int arbitrary = Integer.parseInt(transformationProperties.get("arbitrary"));
				
				Integer[] result = new Integer[source.length];
				for(int i = 0; i < source.length; i++) {
					result[i] = source[i] + arbitrary;
				}
				return result;
			}
		});
		
		Map<String, String> props = new HashMap<String, String>();
		props.put("arbitrary", "11");
		i = (Integer)marshal.getTypedParameter(null, Integer.class, null, new String[] {"12", "15", "16.6"}, new TransformerSpecification("append"), new TransformerSpecification("addArbitrary", props));
		assertEquals((Integer)133, i);
	}
	
	@Test
	public void testDeterminePropertyName() throws Exception {
		ParameterMarshal marshal = new ParameterMarshal();
		
		
		Method mTest1 = getTestObjMethodTest1();
		
		String name = marshal.determineParameterName(null, mTest1, 
				String.class, mTest1.getParameterAnnotations()[0]);
		assertNull( name);

		// once we add the investigator we should get the property name
		marshal.getConfiguration().getParameterNameInvestigators().add(new NameAnnotationPropertyNameInvestigator());
		
		name = marshal.determineParameterName(null, mTest1, 
				String.class, mTest1.getParameterAnnotations()[0]);
		assertEquals("one", name);

		Method mTest2 = getTestObjMethodSetChild2();
		name = marshal.determineParameterName(null, mTest2, 
				String.class, mTest2.getParameterAnnotations()[0]);
		assertNull(name);
		
	}
	
	@Test
	public void testDetermineParameterClass() throws Exception {
		ParameterMarshal marshal = new ParameterMarshal();
		Method mTest1 = getTestObjMethodTest1();
		
		assertEquals(String.class, marshal.determineParameterClass(new TestObj(), mTest1, 0));
		assertEquals(Integer[].class, marshal.determineParameterClass(new TestObj(), mTest1, 1));
	}
	
	@Test
	public void testSourceSets() throws Exception {
		ParameterMarshal marshal = new ParameterMarshal();
		Method mTest1 = getTestObjMethodTest1();
		
		marshal.getConfiguration().getSourceSetInvestigators().add(new SourceAnnotationSourceSetInvestigator());
		
		assertNull(marshal.determineAllowedSourceSets(new TestObj(), mTest1, String.class, null, mTest1.getParameterAnnotations()[0]));
		assertArrayEquals(new String[] { "request" }, marshal.determineAllowedSourceSets(new TestObj(), mTest1, Integer[].class, null, mTest1.getParameterAnnotations()[1]));
	}
	
	@Test
	public void testTransformers() throws Exception {
		ParameterMarshal marshal = new ParameterMarshal();
		Method mTest1 = getTestObjMethodTest1();
		
		marshal.getConfiguration().getTransformersInvestigators().add(new TransformersAnnotationTransformersInvestigator());
		
		assertArrayEquals(new String[] { }, marshal.determineTransformers(new TestObj(), mTest1, String.class, null, mTest1.getParameterAnnotations()[0]));
		
		TransformerSpecification[] specs = marshal.determineTransformers(new TestObj(), mTest1, Integer[].class, null, mTest1.getParameterAnnotations()[1]);
		assertEquals(2, specs.length);
		assertArrayEquals(new String[] { "one", "two" }, new String[] { specs[0].getTransformerName(), specs[1].getTransformerName()});
		assertEquals(specs[0].getTransformerProperties().get("name1"), "value1");
		assertEquals(specs[0].getTransformerProperties().get("name2"), "value2");
		assertEquals(specs[1].getTransformerProperties().get("name1"), "value1");
		assertEquals(specs[1].getTransformerProperties().get("name2"), "value2");
		
		marshal.getConfiguration().getTransformersInvestigators().add(new TransformersAnnotationTransformersInvestigator());
		
		// Confirming that we're using all the investigators and concatenating their output
		specs = marshal.determineTransformers(new TestObj(), mTest1, Integer[].class, null, mTest1.getParameterAnnotations()[1]);
		assertEquals(4, specs.length);
		assertArrayEquals(new String[] { "one", "two", "one", "two" }, new String[] { specs[0].getTransformerName(), specs[1].getTransformerName(), specs[2].getTransformerName(), specs[3].getTransformerName()});
	}
	
	@Test
	public void testCompositeAnotations() throws Exception {
		ParameterMarshal marshal = new ParameterMarshal();
		Method setChild1 = getTestObjMethodSetChild1();
		
		String propertyName = marshal.determineParameterName(null, setChild1, TestObj.class, setChild1.getParameterAnnotations()[0]);
		assertNull(propertyName);
		
		TransformerSpecification[] specs = marshal.determineTransformers(null, setChild1, TestObj.class, propertyName, setChild1.getParameterAnnotations()[0]);
		assertEquals(0, specs.length);
		
		DbInvestigator investigator = new DbInvestigator();
		marshal.getConfiguration().getParameterNameInvestigators().add(investigator);
		marshal.getConfiguration().getTransformersInvestigators().add(investigator);
		
		// now there should be a property name and a named transformer
		propertyName = marshal.determineParameterName(null, setChild1, TestObj.class, setChild1.getParameterAnnotations()[0]);
		assertEquals("id", propertyName);
		
		specs = marshal.determineTransformers(null, setChild1, TestObj.class, propertyName, setChild1.getParameterAnnotations()[0]);
		assertEquals(1, specs.length);
		assertEquals(Constants.DB_TO_OBJECT_TRANSFORMER_NAME_DEFAULT, specs[0].getTransformerName());

		
	}
	
	protected Method getTestObjMethodTest1() throws Exception {
		Method mTest1 = TestObj.class.getMethod("test1", String.class, Integer[].class, List.class);
		return mTest1;
	}
	
	protected Method getTestObjMethodSetChild1() throws Exception {
		Method m = TestObj.class.getMethod("setChild1", TestObj.class);
		return m;
	}
	
	protected Method getTestObjMethodSetChild2() throws Exception {
		Method m = TestObj.class.getMethod("setChild2", TestObj.class);
		return m;
	}
	
	@Test
	public void testIsAllowed() {
		ParameterMarshal marshal = new ParameterMarshal();
		assertTrue(marshal.isSourceAllowed("hello", null));
		assertTrue(marshal.isSourceAllowed("hello", new String[0]));
		assertTrue(marshal.isSourceAllowed("hello", new String[] { "hello", "goodbye"}));
		assertFalse(marshal.isSourceAllowed("hello", new String[] {"goodbye"}));

	}
}
