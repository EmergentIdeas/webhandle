package com.emergentideas.webhandle.templates;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.emergentideas.logging.Logger;
import com.emergentideas.logging.SystemOutLogger;
import com.emergentideas.utils.FileUtils;
import com.emergentideas.utils.ReflectionUtils;
import com.emergentideas.utils.StringUtils;

public class TripartateFileTemplateSource extends TripartateTemplateSource {
	
	protected File root;
	
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
						hints.load(new FileInputStream(child));
					}
					else {
						parts.put(suffix, FileUtils.getFileAsString(child));
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
