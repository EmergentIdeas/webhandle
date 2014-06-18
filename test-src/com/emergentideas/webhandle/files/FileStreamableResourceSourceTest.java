package com.emergentideas.webhandle.files;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.*;

public class FileStreamableResourceSourceTest {

	protected String basePath = "/tmp/FileStreamableResourceSourceTest";
	
	protected File base;
	protected File d1;
	protected File d2;
	protected File d3;
	protected File f1;
	protected File f2;
	
	protected FileStreamableResourceSink source;
	
	@Before
	public void setup() throws Exception {
		base = new File(basePath);
		if(base.exists()) {
			FileUtils.deleteDirectory(base);
		}
		
		d1 = new File(base, "d1");
		d1.mkdirs();
		
		d3 = new File(d1, "d3");
		d3.mkdirs();
		
		f2 = new File(d3, "f2");
		f2.createNewFile();
		
		d2 = new File(base, "d2");
		d2.mkdirs();
		
		f1 = new File(d1, "f1");
		f1.createNewFile();
		
		source = new FileStreamableResourceSink(d1);
	}
	
	
	@Test
	public void testDirectoriesReturned() throws Exception {
		
		// Make sure all of the files and directories that exist are returned by 
		// both relative and absolute path
		assertNotNull(source.get("d3"));
		assertNotNull(source.get("d3/"));
		assertNotNull(source.get("f1"));
		assertNotNull(source.get("d3/f2"));
		assertNotNull(source.get(basePath + "/d1/d3"));
		assertNotNull(source.get(basePath + "/d1/d3/"));
		assertNotNull(source.get(basePath + "/d1/d3/f2"));
		assertNotNull(source.get(basePath + "/d1/f1"));
		
		// make sure that all of the things outside of the source are not returned either by 
		// relative or absolute path
		assertNull(source.get("d2"));
		assertNull(source.get("d2/"));
		assertNull(source.get(basePath + "/d2"));
		assertNull(source.get(basePath + "/d2/"));
	}
	
	@Test
	public void testDirectoryManipulatorInterfaces() throws Exception {
		assertNull(source.get("d4"));
		source.makeDirectory("d4");
		assertNotNull(source.get("d4"));
		source.write("d4/test1", "hello".getBytes());
		assertEquals("hello", IOUtils.toString(((FileStreamableResource)source.get("d4/test1")).getContent()));
		source.removeDirectory("d4", true);
		assertEquals("hello", IOUtils.toString(((FileStreamableResource)source.get("d4/test1")).getContent()));
		source.delete("d4");
		assertEquals("hello", IOUtils.toString(((FileStreamableResource)source.get("d4/test1")).getContent()));
		source.removeDirectory("d4", false);
		assertNull(source.get("d4/test1"));
		
	}
	
	@After
	public void after() throws Exception {
		FileUtils.deleteDirectory(base);
	}
}
