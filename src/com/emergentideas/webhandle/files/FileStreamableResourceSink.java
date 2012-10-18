package com.emergentideas.webhandle.files;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.hibernate.engine.jdbc.StreamUtils;

public class FileStreamableResourceSink extends FileStreamableResourceSource
		implements StreamableResourceSink {
	
	public FileStreamableResourceSink(File root) {
		super(root);
	}


	public void write(String location, InputStream data) throws IOException {
		if(isPathAcceptable(location) == false) {
			return;
		}

		File resource = new File(root, location);
		if(resource.isDirectory()) {
			return;
		}
		
		if(resource.exists() == false) {
			File parent = resource.getParentFile();
			parent.mkdirs();
		}
		
		OutputStream os = new FileOutputStream(resource);
		StreamUtils.copy(data, os);
	}

	public void write(String location, byte[] data) throws IOException {
		write(location, new ByteArrayInputStream(data));
	}


	public void delete(String location) throws IOException {
		if(isPathAcceptable(location) == false) {
			return;
		}
		
		File resource = new File(root, location);
		if(resource.isDirectory()) {
			return;
		}
		
		if(resource.exists()) {
			resource.delete();
		}
	}
	
	

}
