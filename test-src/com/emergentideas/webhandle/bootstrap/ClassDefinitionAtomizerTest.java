package com.emergentideas.webhandle.bootstrap;

import static junit.framework.Assert.*;

import org.junit.Test;

public class ClassDefinitionAtomizerTest {

	@Test
	public void testParameterParsing() throws Exception {
		ClassDefinitionAtomizer atomizer = new ClassDefinitionAtomizer();
		
		FocusAndPropertiesAtom atom = atomizer.atomize("class", "com.emergentideas.webhandle.Class1");
		assertEquals("com.emergentideas.webhandle.Class1", atom.getFocus());
		assertEquals("com.emergentideas.webhandle.Class1", atom.getValue());
		assertEquals("class", atom.getType());
		
		atom = atomizer.atomize("class", "com.emergentideas.webhandle.Class1?");
		assertEquals("com.emergentideas.webhandle.Class1", atom.getFocus());
		assertEquals("com.emergentideas.webhandle.Class1?", atom.getValue());
		
		atom = atomizer.atomize("class", "com.emergentideas.webhandle.Class1?attr1=one");
		assertEquals("com.emergentideas.webhandle.Class1", atom.getFocus());
		assertEquals("com.emergentideas.webhandle.Class1?attr1=one", atom.getValue());
		assertEquals(1, atom.getProperties().size());
		assertEquals("one", atom.getProperties().get("attr1"));
		
		atom = atomizer.atomize("class", "com.emergentideas.webhandle.Class1?attr1=one&attr2=two");
		assertEquals(2, atom.getProperties().size());
		assertEquals("one", atom.getProperties().get("attr1"));
		assertEquals("two", atom.getProperties().get("attr2"));
		
		atom = atomizer.atomize("class", "com.emergentideas.webhandle.Class1?attr1=one%20%26&attr2=two,");
		assertEquals(2, atom.getProperties().size());
		assertEquals("one &", atom.getProperties().get("attr1"));
		assertEquals("two,", atom.getProperties().get("attr2"));
		
	}
}
