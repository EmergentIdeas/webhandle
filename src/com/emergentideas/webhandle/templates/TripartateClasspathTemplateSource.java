package com.emergentideas.webhandle.templates;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.reflections.Configuration;
import org.reflections.Reflections;
import org.reflections.scanners.Scanner;
import org.reflections.vfs.Vfs.File;

import com.emergentideas.utils.StringUtils;
import com.google.common.base.Predicate;
import com.google.common.collect.Multimap;

public class TripartateClasspathTemplateSource extends TripartateTemplateSource {

	protected Map<String, Map<String, String>> partsByTemplateName = new HashMap<String, Map<String,String>>();
	protected Map<String, Properties> hintsByTemplateName = Collections.synchronizedMap(new HashMap<String, Properties>());
	protected Map<String, TemplateInstance> cachedTemplates = Collections.synchronizedMap(new HashMap<String, TemplateInstance>());
	protected String classpathPrefix;
	
	public TripartateClasspathTemplateSource(String classpathPrefix) {
		super();
		this.classpathPrefix = classpathPrefix;
		populateParts();
	}
	
	protected void populateParts() {
		if(classpathPrefix.endsWith("/") == false) {
			classpathPrefix += "/";
		}
		
		classpathPrefix = classpathPrefix.replace('/', '.');
		
		final Pattern pat = Pattern.compile(".*(" + Pattern.quote(classpathPrefix) + ")(.*?)\\.([^.]*)$");
		
		
		Reflections reflections = new Reflections("com", new Scanner() {

			public void setConfiguration(Configuration configuration) {
				// TODO Auto-generated method stub
				
			}

			public Multimap<String, String> getStore() {
				// TODO Auto-generated method stub
				return null;
			}

			public void setStore(Multimap<String, String> store) {
				// TODO Auto-generated method stub
				
			}

			public Scanner filterResultsBy(Predicate<String> filter) {
				// TODO Auto-generated method stub
				return null;
			}

			public boolean acceptsInput(String file) {
				Matcher m = pat.matcher(file);
				if(m.matches()) {
					try {
						addElement(m.group(2), m.group(3), m.group(1));
					}
					catch(Exception e) {
						logger.error("Could not load template of type: " + file, e);
					}
				}
				return false;
			}

			public void scan(File file) {
				// TODO Auto-generated method stub
				
			}

			public boolean acceptResult(String fqn) {
				// TODO Auto-generated method stub
				return false;
			}
			
		});


//		ClasspathScanner scanner = new ClasspathScanner();
//		scanner.add(Thread.currentThread().getContextClassLoader());
//		Set<PathElement> found = scanner.scan(new ScanFilter() {
//			
//			public boolean accept(PathElement cpElement) {
//				Matcher m = pat.matcher(cpElement.getFullPath());
//				if(m.matches()) {
//					try {
//						addElement(m.group(2), m.group(3), m.group(1));
//					}
//					catch(Exception e) {
//						logger.error("Could not load template of type: " + cpElement.getFullPath(), e);
//					}
//				}
//				return false;
//			}
//		});

	}
	
	protected void addElement(String templateName, String suffix, String wholeLocationPrefix) throws Exception {
		wholeLocationPrefix = wholeLocationPrefix.replace('.', '/');
		templateName = templateName.replace('.', '/');
		String classPathLocation = wholeLocationPrefix + templateName + "." + suffix;
		if(HINTS_EXTENSION.equals(suffix)) {
			Properties hints = new Properties(defaultHints);
			hints.load(StringUtils.getStreamFromClassPathLocation(classPathLocation));
			hintsByTemplateName.put(templateName, hints);
		}
		else {
			Map<String, String> parts = getPartsMap(templateName);
			parts.put(suffix, StringUtils.getStringFromClassPathLocation(classPathLocation));
		}
	}
	
	protected Map<String, String> getPartsMap(String templateName) {
		Map<String, String> parts = partsByTemplateName.get(templateName);
		if(parts == null) {
			parts = Collections.synchronizedMap(new HashMap<String, String>());
			partsByTemplateName.put(templateName, parts);
		}
		return parts;
	}
	
	public TemplateInstance get(String templateName) {
		if(partsByTemplateName.containsKey(templateName) == false) {
			return null;
		}
		
		TemplateInstance template = cachedTemplates.get(templateName);
		if(template == null) {
			Properties phints = hintsByTemplateName.get(templateName);
			if(phints == null) {
				phints = defaultHints;
			}

			template = new TripartateTemplate(this, elementStreamProcessor, getPartsMap(templateName), phints);
			cachedTemplates.put(templateName, template);
		}
		
		return template;

	}

}
