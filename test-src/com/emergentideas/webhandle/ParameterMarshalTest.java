package com.emergentideas.webhandle;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.junit.Test;
import org.mockito.Mockito;

import com.emergentideas.utils.ReflectionUtils;
import com.emergentideas.webhandle.Constants;
import com.emergentideas.webhandle.InvocationContext;
import com.emergentideas.webhandle.ParameterMarshal;
import com.emergentideas.webhandle.ValueTransformer;
//import com.emergentideas.webhandle.apps.oak.login.OakUser;
import com.emergentideas.webhandle.assumptions.oak.interfaces.User;
import com.emergentideas.webhandle.composites.db.DbInvestigator;
import com.emergentideas.webhandle.configurations.IntegratorConfiguration;
import com.emergentideas.webhandle.configurations.WebParameterMarsahalConfiguration;
import com.emergentideas.webhandle.exceptions.ParameterNotFoundException;
import com.emergentideas.webhandle.investigators.NameAnnotationPropertyNameInvestigator;
import com.emergentideas.webhandle.investigators.SourceAnnotationSourceSetInvestigator;
import com.emergentideas.webhandle.investigators.TransformersAnnotationTransformersInvestigator;
import com.emergentideas.webhandle.sources.MapValueSource;
import com.emergentideas.webhandle.transformers.NumberToStringTransformer;
import com.emergentideas.webhandle.transformers.StringArrayToStringTransformer;
import com.emergentideas.webhandle.transformers.StringToDoubleTransformer;
import com.emergentideas.webhandle.transformers.StringToIntTransformer;
import com.emergentideas.webhandle.transformers.StringToIntegerTransformer;
import com.emergentideas.webhandle.transformers.TransformerSpecification;

import static org.junit.Assert.*;

public class ParameterMarshalTest {

	@Test
	public void testGetInitialObject() {
		ParameterMarshal marshal = new ParameterMarshal();
		
	}
	
	@Test
	public void testReflectionMethods() {
		ParameterMarshal marshal = new ParameterMarshal();
		
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
		
		Object parameter = marshal.getTypedParameter(obj, m, 0, true);
		assertEquals("1", parameter);
		parameter = marshal.getTypedParameter(obj, m, 1, true);
		assertArrayEquals(intArgs1, (Object[])parameter);
		
		
		values.put("two", new String[] {"2", "3"});
		parameter = marshal.getTypedParameter(obj, m, 1, true);
		assertArrayEquals(intArgs1, (Object[])parameter);
		
		List l = (List)marshal.getTypedParameter(obj, m, 2, true);
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
		values.put("two", new String[] {"2", "3"});
		
		
		MapValueSource source = new MapValueSource(values);
		marshal.getSources().put("request", source);
		
		try {
			values.remove("one");
			assertEquals("null-5-5.0", marshal.call(obj, m));
			assertEquals("something we should never see", marshal.call(obj, m, true));
			fail("There is no parameter one so we should not be here.");
		}
		catch(ParameterNotFoundException e) {
			// we'd expect this since the parameter is not available and we just bad a call
			// that should fail if the parameter is not available.
		}

		// Run once without being able to cache the value
		values.put("one", "3");
		source.setCachable(false);
		assertEquals("3-5-5.0", marshal.call(obj, m));
		
		source.setCachable(true);
		values.put("one", "1");
		// if this test fails it may be because we used a cached value
		assertEquals("1-5-5.0", marshal.call(obj, m));
		
		values.put("one", "2");
		// if this test fails it may be because we did not use a cached value
		assertEquals("1-5-5.0", marshal.call(obj, m));

		
		// Test to make sure the invocation context was populated
		InvocationContext context = marshal.getContext();
		assertEquals("1", context.getFoundParameter("one", String.class));
		assertNull(context.getFoundParameter(String.class));
		
		// Add to the context a value which could not come from the sources to be sure it is
		// used preferentially.
		context.setFoundParameter("two", Integer[].class, new Integer[] { 3, 5 });
		assertEquals("1-8-5.0", marshal.call(obj, m));
		
		// now let's call with call specific properties.  These should be used even in preference 
		// to the cached context parameters.
		Map<String, Object> callProps = new HashMap<String, Object>();
		callProps.put("two", new String[] { "14", "16" });
		assertEquals("1-8-30.0", marshal.call(obj, m, false, "callParms", callProps));
		
		
	}
	
	@Test
	public void testParameterizedCalls() throws Exception {
		ParameterMarshal marshal = new ParameterMarshal(new WebParameterMarsahalConfiguration());
		
		// Test that we can convert a bunch of strings to a list of integers and sum them
		TestObj3 obj = new TestObj3();
		Method m = ReflectionUtils.getFirstMethod(TestObj3.class, "sum");
		
		Map<String, Object> values = new HashMap<String, Object>();
		values.put("values", new String[] {"2", "3", "4"});
		
		MapValueSource source = new MapValueSource(values);
		marshal.getSources().put("request", source);
		
		Integer i = (Integer)marshal.call(obj, m);
		assertEquals((Integer)9, i);
		
		// Test that we can convert to a String to the parameterized argument type
		TestObj5 obj5 = new TestObj5();
		m = ReflectionUtils.getFirstMethod(TestObj5.class, "plus2");
		values.put("num", new String[] {"2", "3", "4"});
		
		String s = (String)marshal.call(obj5, m);
		assertEquals("4", s);
		
	}

	@Test
	public void testSecuredCall() throws Exception {
		ParameterMarshal marshal = new ParameterMarshal(new WebParameterMarsahalConfiguration());
		
		Method m = ReflectionUtils.getFirstMethod(TestObj.class, "setSecuredMethod");
		TestObj obj = new TestObj();
		
		Integer[] intArgs1 = new Integer[] {2, 3}; 
		
		Map<String, Object> values = new HashMap<String, Object>();
		values.put("someInts", new String[] {"2", "3"});
		
		HttpServletRequest mockRequest = Mockito.mock(HttpServletRequest.class);
		Mockito.when(mockRequest.getRequestURL()).thenReturn(new StringBuffer("hello"));
		values.put("request", new HttpServletRequest[] {mockRequest});
		
		MapValueSource source = new MapValueSource(values);
		marshal.getSources().put("request", source);
		
		try {
			marshal.call(obj, m);
			fail("Ooops.  We shouldn't be able to call this.");
		}
		catch(SecurityException e) {
			// Yay! because we don't have any roles for our user
		}
		
		values.put("userRoles", new String[] { "admin", "normal-user" });
		values.put("user", new User() {
			
			public boolean isActive() {
				// TODO Auto-generated method stub
				return false;
			}
			
			public String getProfileName() {
				// TODO Auto-generated method stub
				return null;
			}
			
			public String getId() {
				// TODO Auto-generated method stub
				return null;
			}
			
			public Collection<String> getGroupNames() {
				// TODO Auto-generated method stub
				return null;
			}
			
			public String getFullName() {
				// TODO Auto-generated method stub
				return null;
			}
			
			public String getEmail() {
				// TODO Auto-generated method stub
				return null;
			}
			
			public String getAuthenticationSystem() {
				// TODO Auto-generated method stub
				return null;
			}
		});
		try {
			marshal.call(obj, m);
			fail("Ooops.  We shouldn't be able to call this.");
		}
		catch(SecurityException e) {
			// Yay! because we don't have any roles for our user
		}
		
		// now add our existing source as User Information
		marshal.getSources().put(Constants.USER_INFORMATION_SOURCE_NAME, source);
		marshal.call(obj, m);
		// if we didn't get and exception here, we correctly used the security information
		
	}

	
	@Test
	public void testTransformersRun() {
		ParameterMarshal marshal = new ParameterMarshal();
		marshal.getConfiguration().getTypeTransformers().add(new StringToIntegerTransformer());
		marshal.getConfiguration().getTypeTransformers().add(new StringToDoubleTransformer());
		marshal.getConfiguration().getTypeTransformers().add(new StringToIntTransformer());
		
		Integer i = (Integer)marshal.getTypedParameter(null, Integer.class, null, new String[] {"12", "15", "16.6"}, null);
		assertEquals((Integer)12, i);
		
		i = (Integer)marshal.getTypedParameter(null, int.class, null, new String[] {"12"}, null);
		assertEquals((Integer)12, i);
		
		marshal.getConfiguration().getTransformers().put("append", new ValueTransformer<String, String, String[]>() {

			public String[] transform(InvocationContext context, Map<String, String> transformationProperties, Class finalParameterClass, String parameterName, String... source) {
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

			public Integer[] transform(InvocationContext context, Map<String, String> transformationProperties, Class finalParameterClass, String parameterName, Integer... source) {
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

			public Integer[] transform(InvocationContext context, Map<String, String> transformationProperties, Class finalParameterClass, String parameterName, Integer... source) {
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
				String.class, mTest1.getParameterAnnotations()[0], 0);
		assertNull( name);

		// once we add the investigator we should get the property name
		marshal.getConfiguration().getParameterNameInvestigators().add(new NameAnnotationPropertyNameInvestigator());
		
		name = marshal.determineParameterName(null, mTest1, 
				String.class, mTest1.getParameterAnnotations()[0], 0);
		assertEquals("one", name);

		Method mTest2 = getTestObjMethodSetChild2();
		name = marshal.determineParameterName(null, mTest2, 
				String.class, mTest2.getParameterAnnotations()[0], 0);
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
		
		String propertyName = marshal.determineParameterName(null, setChild1, TestObj.class, setChild1.getParameterAnnotations()[0], 0);
		assertNull(propertyName);
		
		TransformerSpecification[] specs = marshal.determineTransformers(null, setChild1, TestObj.class, propertyName, setChild1.getParameterAnnotations()[0]);
		assertEquals(0, specs.length);
		
		DbInvestigator investigator = new DbInvestigator();
		marshal.getConfiguration().getParameterNameInvestigators().add(investigator);
		marshal.getConfiguration().getTransformersInvestigators().add(investigator);
		
		// now there should be a property name and a named transformer
		propertyName = marshal.determineParameterName(null, setChild1, TestObj.class, setChild1.getParameterAnnotations()[0], 0);
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
	
	
	@Test
	public void testTryConversion() throws Exception {
		TestObj obj = new TestObj();
		Method m = ReflectionUtils.getFirstMethod(obj.getClass(), "primitiveParameters");
		
		m.invoke(obj, new int[] { new Integer(12) });
		assertArrayEquals(new int[] {12}, obj.getPrimitiveParameters());
		
		ArrayTestObj9 o = new ArrayTestObj9();
		assertNull(o.getAssigned());
		
		Map<String, Object> parameters = new HashMap<String, Object>();
		ParameterMarshal parameterMarshal = new ParameterMarshal(new IntegratorConfiguration());
		
		m = ReflectionUtils.getFirstMethod(o.getClass(), "string");
		
		parameters.put("value", "12");
		parameterMarshal.call(o, m, false, Constants.USER_INFORMATION_SOURCE_NAME, parameters);
		assertEquals("12", o.getAssigned());
		o.clear();
		
		parameters.put("value", new String[] {"12"});
		parameterMarshal.call(o, m, false, Constants.USER_INFORMATION_SOURCE_NAME, parameters);
		assertEquals("12", o.getAssigned());
		o.clear();
		
		parameters.put("value", new String[] {"12", "11"});
		parameterMarshal.call(o, m, false, Constants.USER_INFORMATION_SOURCE_NAME, parameters);
		assertEquals("12", o.getAssigned());
		o.clear();
		
		m = ReflectionUtils.getFirstMethod(o.getClass(), "strings");
		
		parameters.put("value", "12");
		parameterMarshal.call(o, m, false, Constants.USER_INFORMATION_SOURCE_NAME, parameters);
		assertTrue(isEqual(new String[] {"12"}, (Object[])o.getAssigned()));
		o.clear();
		
		parameters.put("value", new String[] {"12"});
		parameterMarshal.call(o, m, false, Constants.USER_INFORMATION_SOURCE_NAME, parameters);
		assertTrue(isEqual(new String[] {"12"}, (Object[])o.getAssigned()));
		o.clear();
		
		m = ReflectionUtils.getFirstMethod(o.getClass(), "integers");
		
		parameters.put("value", "12");
		parameterMarshal.call(o, m, false, Constants.USER_INFORMATION_SOURCE_NAME, parameters);
		assertTrue(isEqual(new Integer[] {12}, (Object[])o.getAssigned()));
		o.clear();
		
		parameters.put("value", new String[] {"12", "11"});
		parameterMarshal.call(o, m, false, Constants.USER_INFORMATION_SOURCE_NAME, parameters);
		assertTrue(isEqual(new Integer[] {12, 11}, (Object[])o.getAssigned()));
		o.clear();
		
		parameters.put("value", new Integer[] {12, 11});
		parameterMarshal.call(o, m, false, Constants.USER_INFORMATION_SOURCE_NAME, parameters);
		assertTrue(isEqual(new Integer[] {12, 11}, (Object[])o.getAssigned()));
		o.clear();
		
		m = ReflectionUtils.getFirstMethod(o.getClass(), "intValue");
		
		parameters.put("value", "12");
		parameterMarshal.call(o, m, false, Constants.USER_INFORMATION_SOURCE_NAME, parameters);
		assertEquals(new Integer(12), o.getAssigned());
		o.clear();
		
		parameters.put("value", new String[] {"12"});
		parameterMarshal.call(o, m, false, Constants.USER_INFORMATION_SOURCE_NAME, parameters);
		assertEquals(new Integer(12), o.getAssigned());
		o.clear();

		parameters.put("value", new Integer[] {12});
		parameterMarshal.call(o, m, false, Constants.USER_INFORMATION_SOURCE_NAME, parameters);
		assertEquals(new Integer(12), o.getAssigned());
		o.clear();
		
		m = ReflectionUtils.getFirstMethod(o.getClass(), "integer");
		
		parameters.put("value", "12");
		parameterMarshal.call(o, m, false, Constants.USER_INFORMATION_SOURCE_NAME, parameters);
		assertEquals(new Integer(12), o.getAssigned());
		o.clear();
		
		parameters.put("value", new String[] {"12"});
		parameterMarshal.call(o, m, false, Constants.USER_INFORMATION_SOURCE_NAME, parameters);
		assertEquals(new Integer(12), o.getAssigned());
		o.clear();

		parameters.put("value", new Integer[] {12});
		parameterMarshal.call(o, m, false, Constants.USER_INFORMATION_SOURCE_NAME, parameters);
		assertEquals(new Integer(12), o.getAssigned());
		o.clear();
		
	}
	
	protected boolean isEqual(Object[] a1, Object[] a2) {
		if(a1 == null && a2 == null) {
			return true;
		}
		
		if(a1 == null || a2 == null) {
			return false;
		}
		
		if(a1.length != a2.length) {
			return false;
		}
		
		for(int i = 0; i < a1.length; i++) {
			assertEquals(a1[i], a2[i]);
		}
		
		return true;
	}
}
