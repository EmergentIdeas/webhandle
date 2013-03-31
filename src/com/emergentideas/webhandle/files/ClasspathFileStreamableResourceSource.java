package com.emergentideas.webhandle.files;

public class ClasspathFileStreamableResourceSource implements StreamableResourceSource {

	protected String root;
	protected String eTag = System.currentTimeMillis() + "";
	
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
		
		ClasspathFileStreamableResource resource = new ClasspathFileStreamableResource(root + location, eTag);
		if(resource.getContent() != null) {
			return resource;
		}
		return null;
	}

}
