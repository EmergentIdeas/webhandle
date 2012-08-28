package com.emergentideas.webhandle.templates;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class LibraryTemplateSource implements TemplateSource {

	protected Map<String, TemplateInstance> templates = Collections.synchronizedMap(new HashMap<String, TemplateInstance>());
	
	public TemplateInstance get(String templateName) {
		return templates.get(templateName);
	}
	
	public void add(String name, TemplateInstance instance) {
		templates.put(name, instance);
	}

}
