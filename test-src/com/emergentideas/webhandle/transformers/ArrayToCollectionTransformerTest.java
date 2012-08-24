package com.emergentideas.webhandle.transformers;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import com.emergentideas.webhandle.TestObj;
import com.emergentideas.webhandle.transformers.ArrayToCollectionTransformer;

import static org.junit.Assert.*;

public class ArrayToCollectionTransformerTest {

	@Test
	public void testArrayToCollection() {
		ArrayToCollectionTransformer transformer = new ArrayToCollectionTransformer();
		
		List<TestObj> l;
		
		TestObj[] source = new TestObj[] { new TestObj("1", "2"), new TestObj("3", "4") };
		l = transformer.transform(List.class, TestObj.class, source[0], source[1]);
		assertEquals(2, l.size());
		assertEquals("1", l.get(0).getA());
		assertEquals("3", l.get(1).getA());
		assertEquals(source[0].getA(), l.get(0).getA());
		assertEquals(source[1].getA(), l.get(1).getA());
		
		Set<TestObj> s;
		s = transformer.transform(Set.class, TestObj.class, source[0], source[1]);
		assertEquals(2, s.size());
		
		Collection<TestObj> c;
		c = transformer.transform(Collection.class, TestObj.class, source[0], source[1]);
		assertEquals(2, c.size());
		
	}
}
