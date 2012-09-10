package com.emergentideas.webhandle.assumptions.oak;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.emergentideas.webhandle.Name;
import com.emergentideas.webhandle.templates.TemplateInstance;
import com.emergentideas.webhandle.templates.TemplateSource;

@Name("template-library")
public class LibraryTemplateSource implements TemplateSource {

	protected Map<String, TemplateInstance> templates = Collections.synchronizedMap(new HashMap<String, TemplateInstance>());
	
	public TemplateInstance get(String templateName) {
		return templates.get(templateName);
	}
	
	public void add(String name, TemplateInstance instance) {
		templates.put(name, instance);
	}

}
