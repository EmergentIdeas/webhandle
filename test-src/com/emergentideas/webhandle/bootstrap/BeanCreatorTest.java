package com.emergentideas.webhandle.bootstrap;

import static junit.framework.Assert.*;

import org.junit.Test;

import com.emergentideas.webhandle.TestObj2;

public class BeanCreatorTest {

	@Test
	public void testSetConfigProperties() throws Exception {
		BeanCreator creator = new BeanCreator();
		
		FocusAndPropertiesConfigurationAtom atom = new FocusAndPropertiesConfigurationAtom();
		
		atom.setFocus("com.emergentideas.webhandle.TestObj2");
		atom.getProperties().put("a", "hello");
		atom.getProperties().put("b", "world");
		atom.getProperties().put("something", "there");
		
		
		TestObj2 o = (TestObj2)creator.create(null, null, atom);
		
		assertNotNull(o);
		assertEquals("hello", o.getA());
		assertEquals("world", o.getB());
		
	}
}