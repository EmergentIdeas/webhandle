package com.emergentideas.webhandle.files;

import java.io.File;

import com.emergentideas.webhandle.files.FileInfo.FileType;

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
		
		FileInfo fi = FileInfo.getInfo(resource);
		if(fi != null) {
			if(fi.getType() == FileType.FILE) {
				return new FileStreamableResource(resource, fi.getLastModified() + "");
			}
			else if(fi.getType() == FileType.DIRECTORY) {
				return new DirectoryResource(resource, this);
			}
		}
		return null;
	}
	
	protected boolean isPathAcceptable(String path) {
		return ! (path.contains("..") || path.contains("~"));
	}

}
