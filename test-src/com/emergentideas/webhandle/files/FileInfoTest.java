package com.emergentideas.webhandle.files;

import static org.junit.Assert.*;

import java.io.File;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import com.emergentideas.webhandle.files.FileInfo.FileType;

public class FileInfoTest {

	@Test
	public void testJava6And7() throws Exception {
		
		String firstTime;
		
		FileInfo.isJava7();
		FileInfo.initReflectionObjects();
		FileInfo fi = FileInfo.getInfo(new File("").getAbsoluteFile());
		firstTime = fi.getLastModified();
		assertTrue(fi.getType() == FileType.DIRECTORY);
		assertTrue(StringUtils.isNotBlank(fi.getLastModified()));
		
		
		fi = FileInfo.getInfo(new File("").getAbsoluteFile());
		
		FileInfo.java7 = false;
		fi = FileInfo.getInfo(new File("").getAbsoluteFile());
		assertTrue(fi.getType() == FileType.DIRECTORY);
		assertTrue(StringUtils.isNotBlank(fi.getLastModified()));
		assertEquals(firstTime, fi.getLastModified());
		
		fi = FileInfo.getInfo(new File("").getAbsoluteFile());
	}
	
}
