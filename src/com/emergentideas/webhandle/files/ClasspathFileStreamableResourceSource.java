package com.emergentideas.webhandle.files;

import java.util.HashSet;
import java.util.Set;

public class ClasspathFileStreamableResourceSource implements StreamableResourceSource {

	protected String root;
	protected String eTag = System.currentTimeMillis() + "";
	protected Set<String> knownAbsent = new HashSet<String>();
	
	public ClasspathFileStreamableResourceSource(String root) {
		root = root.replace('.', '/');
		if(root.endsWith("/") == false) {
			root += '/';
		}
		this.root = root;
		
	}
	
	public Resource get(String location) {
		if(location.contains("..") || location.contains("~")) {
			return null;
		}
		
		if(knownAbsent.contains(location)) {
			return null;
		}
		
		ClasspathFileStreamableResource resource = new ClasspathFileStreamableResource(root + location, eTag);
		if(resource.getContent() != null) {
			return resource;
		}
		
		knownAbsent.add(location);
		return null;
	}

}
