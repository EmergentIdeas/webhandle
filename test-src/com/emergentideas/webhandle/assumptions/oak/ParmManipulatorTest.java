package com.emergentideas.webhandle.assumptions.oak;

import static junit.framework.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.emergentideas.webhandle.ParameterMarshal;
import com.emergentideas.webhandle.TestObj;
import com.emergentideas.webhandle.configurations.WebParameterMarsahalConfiguration;
import com.emergentideas.webhandle.sources.MapValueSource;

public class ParmManipulatorTest {

	@Test
	public void testInject() {
		TestObj obj = new TestObj();
		
		ParameterMarshal marshal = new ParameterMarshal(new WebParameterMarsahalConfiguration());
		
		ParmManipulator manip = new ParmManipulator(marshal.getContext());
		
		Map<String, String> properties = new HashMap<String, String>();
		properties.put("a", "hello");
		properties.put("b", "there");
		properties.put("z", "world");
		
		marshal.addSource("values", new MapValueSource(properties));
		
		manip.inject(obj);
		
		assertEquals("hello", obj.getA());
		assertNull(obj.getB());
		
		obj = new TestObj();
		manip.inject(obj, "b");
		assertNull(obj.getA());
		assertNull(obj.getB());
		
	}
	
	@Test
	public void testCreateRealm() {
		
	}
}
