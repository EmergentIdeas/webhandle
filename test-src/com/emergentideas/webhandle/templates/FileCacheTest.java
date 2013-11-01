package com.emergentideas.webhandle.templates;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileOutputStream;

import org.junit.Test;

public class FileCacheTest {

	@Test
	public void testCache() throws Exception {
		File f1 = File.createTempFile("one", "two");
		File f2 = File.createTempFile("one", "two");
		
		FileCache fc = new FileCache();
		write(f1, "one");
		write(f2, "two");
		
		assertEquals("one", fc.get(f1));
		assertEquals("two", fc.get(f2));
		
		// because the file system won't track mod times to the millisecond
		Thread.sleep(2000);
		
		write(f2, "three");
		
		assertEquals("three", fc.get(f2));
		
	}
	
	protected void write(File f, String data) throws Exception {
		FileOutputStream fos = new FileOutputStream(f);
		fos.write(data.getBytes());
		fos.flush();
		fos.close();
		
	}
}
