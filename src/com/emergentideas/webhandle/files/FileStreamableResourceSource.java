package com.emergentideas.webhandle.files;

import java.io.File;

public class FileStreamableResourceSource implements StreamableResourceSource {

	protected File root;
	
	public FileStreamableResourceSource(File root) {
		this.root = root;
	}
	
	public StreamableResource get(String location) {
		if(location.contains("..") || location.contains("~")) {
			return null;
		}
		
		File resource = new File(root, location);
		if(resource.exists()) {
			return new FileStreamableResource(resource);
		}
		return null;
	}

}
