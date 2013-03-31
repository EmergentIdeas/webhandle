package com.emergentideas.webhandle.files;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DirectoryResource implements Directory, NamedResource {

	protected File directory;
	protected StreamableResourceSource source;
	
	public DirectoryResource(File directory, StreamableResourceSource source) {
		super();
		this.directory = directory;
		this.source = source;
	}

	public List<Resource> getEntries() {
		
		String rootPath = directory.getAbsolutePath();
		if(rootPath.endsWith("/") == false) {
			rootPath += "/";
		}
		
		List<Resource> entries = new ArrayList<Resource>();
		
		for(String child : directory.list()) {
			Resource r = source.get(rootPath + child);
			if(r != null) {
				entries.add(r);
			}
		}
		
		return entries;
	}

	public String getName() {
		return directory.getName();
	}
}
