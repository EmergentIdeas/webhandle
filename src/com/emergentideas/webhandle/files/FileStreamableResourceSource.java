package com.emergentideas.webhandle.files;

import java.io.File;

public class FileStreamableResourceSource implements StreamableResourceSource {

	protected File root;
	
	public FileStreamableResourceSource(File root) {
		this.root = root;
	}
	
	public StreamableResource get(String location) {
		if(isPathAcceptable(location) == false) {
			return null;
		}
		
		File resource = new File(root, location);
		if(resource.exists() && resource.isFile()) {
			return new FileStreamableResource(resource);
		}
		return null;
	}
	
	protected boolean isPathAcceptable(String path) {
		return ! (path.contains("..") || path.contains("~"));
	}

}
