package com.emergentideas.utils;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.util.Random;

import org.junit.Test;

public class StringUtilsTest {

	@Test
	public void testReadFromStream() throws Exception {
		String source = generateString(2501);
		String read = StringUtils.readStream(new ByteArrayInputStream(source.getBytes()));
		assertEquals(source, read);
	}
	
	public String generateString(int length) {
		char base = 'A';
		Random rand = new Random(System.currentTimeMillis());
		
		StringBuilder sb = new StringBuilder();
		
		for(int i = 0; i < length; i++) {
			sb.append((base + rand.nextInt(26)));
		}
		
		return sb.toString();
	}
}
