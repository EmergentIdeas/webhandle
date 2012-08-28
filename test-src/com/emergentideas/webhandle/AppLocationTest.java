package com.emergentideas.webhandle;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.emergentideas.webhandle.templates.TripartateFileTemplateSource;

public class AppLocationTest {

	@Test
	public void testGet() throws Exception {
		Location loc = new AppLocation();
		TestObj obj = new TestObj("AA", "BB");
		obj.setId("myid");
		loc.add(obj);

		TestObj obj2 = new TestObj("A2", "B2");
		obj.setChild2(obj2);

		TestObj obj3 = new TestObj("A3", "B3");

		List<TestObj> manyChildren = new ArrayList<TestObj>();
		manyChildren.add(obj2);
		manyChildren.add(obj3);
		obj.setManyChildren(manyChildren);

		List<Object> l = loc.all("id");
		assertEquals(1, l.size());
		assertEquals("myid", l.get(0));

		List<TestObj> tol = loc.all("manyChildren");
		assertEquals(2, tol.size());
		assertEquals("B2", tol.get(0).getB());
		assertEquals("B3", tol.get(1).getB());

	}

	@Test
	public void testMultiLevel() throws Exception {
		Location base = new AppLocation();
		WebAppLocation web = new WebAppLocation(base);
		web.init();
		
		web.setParameterMarshalConfiguration(new ParameterMarshalConfiguration());
		web.setTemplateSource(new TripartateFileTemplateSource());

		Location second = new AppLocation(base);
		second.put("servicesByType", new AppLocation());
		
		WebAppLocation web2 = new WebAppLocation(second);
		web2.setTemplateSource(new TripartateFileTemplateSource());
		
		Object o1 = web.getParameterMarshalConfiguration();
		Object o2 = web2.getParameterMarshalConfiguration();
		
		assertNotNull(o1);
		assertNotNull(o2);
		assertTrue(o1 == o2);
		
		o1 = web.getTemplateSource();
		o2 = web2.getTemplateSource();
		assertNotNull(o1);
		assertNotNull(o2);
		assertTrue(o1 != o2);
	}
}
