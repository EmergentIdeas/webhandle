package com.emergentideas.webhandle.files;

import java.io.File;

public class FileStreamableResourceSource implements StreamableResourceSource {

	protected File root;
	
	public FileStreamableResourceSource(File root) {
		this.root = root;
	}
	
	public Resource get(String location) {
		if(isPathAcceptable(location) == false) {
			return null;
		}
		
		File resource;
		if(location.startsWith("/") == false) {
			resource = new File(root, location);
		}
		else {
			// looks like this is an absolute path
			// if this is a child of the root, we'll retrieve it, otherwise not
			if(location.startsWith(root.getAbsolutePath() + "/")) {
				resource = new File(location);
			}
			else {
				return null;
			}
		}
		
		if(resource.exists()) {
			if(resource.isFile()) {
				return new FileStreamableResource(resource);
			}
			else if(resource.isDirectory()) {
				return new DirectoryResource(resource, this);
			}
		}
		return null;
	}
	
	protected boolean isPathAcceptable(String path) {
		return ! (path.contains("..") || path.contains("~"));
	}

}
