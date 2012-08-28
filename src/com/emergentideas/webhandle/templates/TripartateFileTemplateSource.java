package com.emergentideas.webhandle.templates;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.emergentideas.logging.Logger;
import com.emergentideas.logging.SystemOutLogger;
import com.emergentideas.utils.FileUtils;

public class TripartateFileTemplateSource implements TemplateSource {
	
	protected File root;
	
	protected ElementStreamProcessor elementStreamProcessor;
	
	protected Logger logger = SystemOutLogger.get(TripartateFileTemplateSource.class);
	
	public TripartateFileTemplateSource() {
		
		List<ElementProcessor> processors = new ArrayList<ElementProcessor>();
		processors.add(new StringElementProcessor());
		processors.add(new TripartateTemplateElementProcessor());
		elementStreamProcessor = new ElementStreamProcessor(processors);
	}
	
	public TripartateFileTemplateSource(File root) {
		this();
		this.root = root;
	}
	

	public TemplateInstance get(String templateName) {
		if(templateName.contains("..")) {
			return null;
		}
		
		
		File templateFile = new File(root, templateName);
		
		if(templateFile.exists() == false) {
			return null;
		}
		
		Map<String, String> parts = new HashMap<String, String>();
		
		try {
			if(templateName.endsWith(".template")) {
				parts.put("template", FileUtils.getFileAsString(templateFile));
				
				String path = templateFile.getAbsolutePath();
				String prefix = path.substring(0, path.length() - "template".length());
				int prefixLength = prefix.length();
				
				File parent = templateFile.getParentFile();
				for(File child : parent.listFiles()) {
					if(child.getAbsolutePath().startsWith(prefix)) {
						String suffix = child.getAbsolutePath().substring(prefixLength);
						parts.put(suffix, FileUtils.getFileAsString(child));
					}
				}
			}
			
			return new TripartateTemplate(this, elementStreamProcessor, parts);
		}
		catch(IOException e) {
			logger.error("Could not read template file", e);
			return null;
		}
	}

	public void setLocation(String location) {
		root = new File(location);
	}
}
