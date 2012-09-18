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

public class TripartateFileTemplateSource implements TemplateSource {
	
	protected File root;
	
	protected ElementStreamProcessor elementStreamProcessor;
	protected Properties defaultHints;
	
	protected Logger logger = SystemOutLogger.get(TripartateFileTemplateSource.class);
	
	public static final String HINTS_EXTENSION = "hints";
	
	
	public TripartateFileTemplateSource() {
		
		List<ElementProcessor> processors = new ArrayList<ElementProcessor>();
		processors.add(new StringElementProcessor());
		processors.add(new TripartateTemplateElementProcessor());
		elementStreamProcessor = new ElementStreamProcessor(processors);
		
		// set up the default hints that will apply if no others have been selected
		defaultHints = new Properties();
		try {
			defaultHints.load(StringUtils.getStreamFromClassPathLocation("com/emergentideas/webhandle/templates/defaultTemplateHints.properties"));
		}
		catch(Exception e) {
			logger.error("Could not load default tripartate template hints", e);
		}
	}
	
	public TripartateFileTemplateSource(File root) {
		this();
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
			for(File child : parent.listFiles()) {
				if(child.getAbsolutePath().startsWith(path)) {
					// get the part name
					String suffix = child.getAbsolutePath().substring(prefixLength + 1);
					
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
