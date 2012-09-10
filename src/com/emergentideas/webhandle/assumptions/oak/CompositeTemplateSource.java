package com.emergentideas.webhandle.assumptions.oak;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.emergentideas.webhandle.Type;
import com.emergentideas.webhandle.templates.TemplateInstance;
import com.emergentideas.webhandle.templates.TemplateSource;

@Type("com.emergentideas.webhandle.templates.TemplateSource")
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
	
	public List<TemplateSource> getSources() {
		return sources;
	}

}
