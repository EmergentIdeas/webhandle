package com.emergentideas.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileUtils {
	public static String getFileAsString(File file) throws FileNotFoundException, IOException
	{
		long length = file.length();
		byte[] b = new byte[(int)length];
		FileInputStream is = new FileInputStream(file);
		is.read(b);
		return(new String(b));
	}
	
	public static String[] getFileAsStrings(File file) throws FileNotFoundException, IOException
	{
		long length = file.length();
		byte[] b = new byte[(int)length];
		FileInputStream is = new FileInputStream(file);
		is.read(b);
		String s = new String(b);
		
		s = s.replaceAll("\r", "");
		
		
		
		return(s.split("\n"));
	}

}
