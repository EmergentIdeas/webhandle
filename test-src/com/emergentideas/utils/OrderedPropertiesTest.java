package com.emergentideas.utils;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Test;

public class OrderedPropertiesTest {

	@Test
	public void testOrdered() throws Exception {
		Random rand = new Random();
		List<String> l = new ArrayList<String>();
		StringBuilder sb = new StringBuilder();
		
		int i = 20;
		while(i > 0) {
			int randValue = rand.nextInt(1000);
			if(l.contains(randValue)) {
				continue;
			}
			String value = randValue + ""; 
			l.add(value);
			sb.append(value + "=" + value + "\n");
			i--;
		}
		
		ByteArrayInputStream inst = new ByteArrayInputStream(sb.toString().getBytes());
		
		OrderedProperties prop = new OrderedProperties();
		prop.load(inst);
		
		for(Object value : prop.keySet()) {
			assertEquals(l.get(0), (String)value);
			l.remove(0);
		}
		
		assertTrue(l.isEmpty());
	}
}
