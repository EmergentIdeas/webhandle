package com.emergentideas.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileUtils {
	
	public static String getFileAsString(File file) throws FileNotFoundException, IOException {
		byte[] b = new byte[(int)file.length()];
		FileInputStream is = new FileInputStream(file);
		is.read(b);
		return new String(b);
	}
	
	public static String[] getFileAsStrings(File file) throws FileNotFoundException, IOException {
		return StringUtils.splitIntoLines(getFileAsString(file));
	}
	


}
