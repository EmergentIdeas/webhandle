package com.emergentideas.webhandle.templates;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import javax.annotation.Resource;

import com.emergentideas.webhandle.assumptions.oak.RequestTermCache;

public class TripartateFileTemplateSource extends TripartateTemplateSource {
	
	protected File root;
	protected FileCache cache = new FileCache();
	
	protected String uid = UUID.randomUUID().toString();
	protected String cachePrefix = "template:" + uid + ":";
	
	@Resource
	protected RequestTermCache requestTermCache;
	
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
		
		if(requestTermCache != null && requestTermCache.containsKey(cachePrefix + templateName)) {
			TemplateInstance ti = (TemplateInstance)requestTermCache.get(cachePrefix + templateName);
			return ti;
		}
		
		return getFromDisk(templateName);
	}
	
	protected TemplateInstance getFromDisk(String templateName) {
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
				if(requestTermCache != null) {
					requestTermCache.put(cachePrefix + templateName, null);
				}
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
				if(requestTermCache != null) {
					requestTermCache.put(cachePrefix + templateName, null);
				}
				return null;
			}
			
			TemplateInstance template = new TripartateTemplate(this, elementStreamProcessor, parts, hints);
			if(requestTermCache != null) {
				requestTermCache.put(cachePrefix + templateName, template);
			}
			return template;
		}
		catch(IOException e) {
			logger.error("Could not read template file: " + templateName, e);
			if(requestTermCache != null) {
				requestTermCache.put(cachePrefix + templateName, null);
			}
			return null;
		}		
	}

	public void setLocation(String location) {
		root = new File(location);
	}

	public FileCache getCache() {
		return cache;
	}

	public void setCache(FileCache cache) {
		this.cache = cache;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getCachePrefix() {
		return cachePrefix;
	}

	public void setCachePrefix(String cachePrefix) {
		this.cachePrefix = cachePrefix;
	}

	public RequestTermCache getRequestTermCache() {
		return requestTermCache;
	}

	public void setRequestTermCache(RequestTermCache requestTermCache) {
		this.requestTermCache = requestTermCache;
	}
	
	
}
