package com.emergentideas.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class StringUtils {

	/**
	 * Reads all of the bytes from an input stream and returns an UTF-8 string.
	 * @param input
	 * @return
	 * @throws IOException
	 */
	public static String readStream(InputStream input) throws IOException {
		
		return new String(readStreamBytes(input));
	}
	
	/**
	 * Reads all of the bytes from an input stream and returns them as an array
	 * @param input
	 * @return
	 * @throws IOException
	 */
	public static byte[] readStreamBytes(InputStream input) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		byte[] temp = new byte[1000];
		int i;
		while((i = input.read(temp)) > 0) {
			baos.write(temp, 0, i);
		}
		
		return baos.toByteArray();
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
	
	/**
	 * Replaces text in source using the arguments passed where the arguments after 
	 * source are always in the order target, replacement
	 * @param source
	 * @param replacements
	 */
	public static void replaceString(StringBuilder source, String ... replacements ) {
		for(int i = 0; i + 1 < replacements.length; i += 2)
		{
			String target = replacements[i];
			String replacement = replacements[i + 1];
			replaceString(source, target, replacement);
		}
	}
	
	public static String replaceString(String source, Map<String, String> replacements) {
		StringBuilder sb = new StringBuilder(source);
		for(Map.Entry<String, String> entry : replacements.entrySet()) {
			replaceString(sb, entry.getKey(), entry.getValue());
		}
		return sb.toString();
	}
	
	/**
	 * Replaces text in source using the arguments passed where the arguments after 
	 * source are always in the order target, replacement
	 * @param source
	 * @param replacements
	 */
	public static String replaceString(String source, String ... replacements ) {
		StringBuilder sb = new StringBuilder(source);
		replaceString(sb, replacements);
		return sb.toString();
	}	
	
	/**
	 * Replaces the target text in the source string with the replacement string
	 * @param source
	 * @param target
	 * @param replacement
	 * @return
	 */
	public static void replaceString(StringBuilder source, String target, String replacement) {
		if(target == null)
		{
			return;
		}
		if(replacement == null)
		{
			replacement = "";
		}
		
		int iTargetLength = target.length();
		int index;
		int starting = 0;
		while((index = source.indexOf(target, starting)) > -1)
		{
			source.replace(index, index + iTargetLength, replacement);
			starting = index + replacement.length();
		}
	}
	
	public static String replaceString(String source, String target, String replacement) {
		if(target == null)
		{
			return source;
		}
		if(replacement == null)
		{
			replacement = "";
		}
		
		StringBuilder sb = new StringBuilder(source);
		replaceString(sb, target, replacement);
		return sb.toString();
	}
}
