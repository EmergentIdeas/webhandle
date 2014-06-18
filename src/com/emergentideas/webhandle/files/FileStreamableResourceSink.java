package com.emergentideas.webhandle.files;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;


public class FileStreamableResourceSink extends FileStreamableResourceSource
		implements StreamableResourceSink, DirectoryManipulator {
	
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
		IOUtils.copy(data, os);
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
			removeDirectory(location, true);
			return;
		}
		
		if(resource.exists()) {
			resource.delete();
		}
	}


	@Override
	public void makeDirectory(String path) {
		if(isPathAcceptable(path) == false) {
			return;
		}
		
		if(StringUtils.isBlank(path)) {
			return;
		}
		
		if(path.endsWith("/")) {
			path = path.substring(0, path.length() - 1);
		}
		
		if(StringUtils.isBlank(path)) {
			return;
		}
		
		File resource = new File(root, path);
		if(resource.exists()) {
			return;
		}
		
		resource.mkdirs();
	}


	@Override
	public void removeDirectory(String path, boolean failIfNotEmpty) {
		if(isPathAcceptable(path) == false) {
			return;
		}
		
		if(StringUtils.isBlank(path)) {
			return;
		}
		
		if(path.endsWith("/")) {
			path = path.substring(0, path.length() - 1);
		}
		
		if(StringUtils.isBlank(path)) {
			return;
		}
		
		File resource = new File(root, path);
		if(resource.exists() == false) {
			return;
		}

		if(failIfNotEmpty) {
			resource.delete();
		}
		else {
			try {
				FileUtils.deleteDirectory(resource);
			}
			catch(IOException e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	

}
