package com.emergentideas.webhandle.templates;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.emergentideas.webhandle.Location;
import com.emergentideas.webhandle.output.SegmentedOutput;

public class TripartateTemplate implements TemplateInstance {

	protected Map<String, String> sections = Collections.synchronizedMap(new HashMap<String, String>());
	
	protected List<Element> templateElements = Collections.synchronizedList(new ArrayList<Element>());
	
	protected ElementStreamProcessor processor;
	
	public TripartateTemplate(TemplateSource templateSource, ElementStreamProcessor processor, Map<String, String> sections) {
		this.sections.putAll(sections);
		this.processor = processor;
		
		String template = sections.get("template");
		templateElements.addAll(new TripartateParser().parse(template));
	}
	
	public void render(SegmentedOutput output, Location location) {
		processor.process(location, output, templateElements);

	}

	public Map<String, String> getSections() {
		return sections;
	}

	public void setSections(Map<String, String> sections) {
		this.sections = sections;
	}

	public List<Element> getTemplateElements() {
		return templateElements;
	}

	public void setTemplateElements(List<Element> templateElements) {
		this.templateElements = templateElements;
	}
	
	
}
