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
	
	@Test
	public void testCreateMultipleObjects() throws Exception {
		BeanCreator creator = new BeanCreator();
		
		FocusAndPropertiesConfigurationAtom atom = new FocusAndPropertiesConfigurationAtom();
		
		atom.setFocus("com.emergentideas.webhandle.TestObj.*");
		
		AtomAndObject[] atomsAndObjects = (AtomAndObject[])creator.create(null, null, atom);
		assertEquals(10, atomsAndObjects.length);
	}
	
	@Test
	public void testObjectWithoutConstructor() throws Exception {
		BeanCreator creator = new BeanCreator();
		assertNull(creator.createObjectFromClass(NoNoargConstructor.class.getName(), null));
		assertNotNull(creator.createObjectFromClass(NoargConstructor.class.getName(), null));
		assertNull(creator.createObjectFromClass(AbstractClass.class.getName(), null));
	}
}
