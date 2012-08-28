package com.emergentideas.webhandle.templates;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CompositeTemplateSource implements TemplateSource {

	List<TemplateSource> sources = Collections.synchronizedList(new ArrayList<TemplateSource>());
	
	public TemplateInstance get(String templateName) {
		for(TemplateSource source : sources) {
			TemplateInstance ti = source.get(templateName);
			if(ti != null) {
				return ti;
			}
		}
		
		return null;
	}
	
	public void addTemplateSource(TemplateSource source) {
		sources.add(source);
	}

}
