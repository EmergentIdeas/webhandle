package com.emergentideas.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class StringUtils {

	/**
	 * Reads all of the bytes from an input stream and returns an UTF-8 string.
	 * @param input
	 * @return
	 * @throws IOException
	 */
	public static String readStream(InputStream input) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		byte[] temp = new byte[1000];
		int i;
		while((i = input.read(temp)) > 0) {
			baos.write(temp, 0, i);
		}
		
		return new String(baos.toByteArray());
	}
	
	/**
	 * Splits a string into lines by using \n or \r\n as a line
	 * terminator
	 * @param source
	 * @return
	 */
	public static String[] splitIntoLines(String source) {
		source = source.replaceAll("\r", "");
		return(source.split("\n"));
	}
	

	/**
	 * Gets the contents of a resource in the class path.  Resource name is in the format of
	 * com/emergentideas/webhandle/bootstrap/config1.conf
	 * @param resourceName
	 * @return
	 */
	public static String getStringFromClassPathLocation(String resourceName) throws IOException {
		return readStream(getStreamFromClassPathLocation(resourceName));
	}

	/**
	 * Gets the contents of a resource in the class path.  Resource name is in the format of
	 * com/emergentideas/webhandle/bootstrap/config1.conf
	 * @param resourceName
	 * @return
	 */
	public static InputStream getStreamFromClassPathLocation(String resourceName) throws IOException {
		// Using the thread's class loader so that any classes/files loaded by subsequent class loaders
		// will also be found as long as a request sets the app's class loader when the thread is processed.
		InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourceName);
		return input;
	}

}
