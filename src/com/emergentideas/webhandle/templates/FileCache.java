package com.emergentideas.webhandle.templates;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.emergentideas.utils.FileUtils;

public class FileCache {
	
	protected Map<String, Long> updateTimes = Collections.synchronizedMap(new HashMap<String, Long>());

	protected Map<String, String> fileContents = Collections.synchronizedMap(new HashMap<String, String>());
	
	public synchronized String get(File file) throws IOException {
		String path = file.getAbsolutePath();
		Long cur = file.lastModified();
		if(updateTimes.containsKey(path) == false) {
			addToCache(file, path, cur);
		}
		else if(updateTimes.get(path).equals(cur) == false) {
				addToCache(file, path, cur);
		}
		
		return fileContents.get(path);
	}
	
	protected void addToCache(File file, String path, Long cur) throws IOException {
		if(cur == null) {
			cur = file.lastModified();
		}
		updateTimes.put(path, cur);
		fileContents.put(path, FileUtils.getFileAsString(file));
	}
}
