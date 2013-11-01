package com.emergentideas.webhandle.templates;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class TripartateFileTemplateSource extends TripartateTemplateSource {
	
	protected File root;
	protected FileCache cache = new FileCache();
	
	public TripartateFileTemplateSource() {
		super();
	}
	
	public TripartateFileTemplateSource(File root) {
		super();
		this.root = root;
	}
	

	public TemplateInstance get(String templateName) {
		if(templateName.contains("..") || templateName.contains("~")) {
			return null;
		}
		
		File templateFile = new File(root, templateName);
		
		
		Map<String, String> parts = new HashMap<String, String>();
		Properties hints = defaultHints;
		try {
			String path = templateFile.getAbsolutePath();
			int prefixLength = path.length();
			path += '.';
			
			File parent = templateFile.getParentFile();
			File[] siblingFiles = parent.listFiles();
			if(siblingFiles == null) {
				return null;
			}
			for(File child : siblingFiles) {
				if(child.isDirectory()) {
					continue;
				}
				
				if(child.getAbsolutePath().startsWith(path)) {
					// get the part name
					String suffix = child.getAbsolutePath();
					if(suffix.length() == prefixLength) {
						// This file doesn't have suffix, so I'm not sure what to do with it
						continue;
					}
					suffix = suffix.substring(prefixLength + 1);
					
					if(HINTS_EXTENSION.equals(suffix)) {
						hints = new Properties(hints);
						hints.load(new StringReader(cache.get(child)));
					}
					else {
						parts.put(suffix, cache.get(child));
					}
				}
			}
			
			if(parts.size() == 0) {
				return null;
			}
			
			TemplateInstance template = new TripartateTemplate(this, elementStreamProcessor, parts, hints);
			return template;
		}
		catch(IOException e) {
			logger.error("Could not read template file: " + templateName, e);
			return null;
		}
	}

	public void setLocation(String location) {
		root = new File(location);
	}
}
