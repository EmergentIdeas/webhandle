package com.emergentideas.webhandle.files;

import java.io.IOException;
import java.io.InputStream;

/**
 * Provides a way to write streams to a file system or net location using 
 * a simple interface.
 * @author kolz
 *
 */
public interface StreamableResourceSink extends StreamableResourceSource {

	public void write(String location, InputStream data) throws IOException;
	
	public void write(String location, byte[] data) throws IOException;
	
	public void delete(String location) throws IOException;
	
}
