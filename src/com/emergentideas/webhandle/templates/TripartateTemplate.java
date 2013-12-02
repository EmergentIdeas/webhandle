package com.emergentideas.webhandle.templates;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.emergentideas.logging.Logger;
import com.emergentideas.logging.SystemOutLogger;
import com.emergentideas.utils.OrderedProperties;
import com.emergentideas.utils.ReflectionUtils;
import com.emergentideas.utils.StringUtils;
import com.emergentideas.webhandle.Location;
import com.emergentideas.webhandle.output.SegmentedOutput;
import com.emergentideas.webhandle.output.SegmentedOutputOverlay;

public class TripartateTemplate implements TemplateInstance {

	protected Map<String, String> sections = Collections.synchronizedMap(new HashMap<String, String>());
	
	protected Map<String, List<Element>> compiledSections = Collections.synchronizedMap(new HashMap<String, List<Element>>()); 
	
	protected Properties hints;
	
	protected ElementStreamProcessor processor;
	
	protected Logger log = SystemOutLogger.get(TripartateTemplate.class);
	
	public static final String APPEND_MARKER = "append";
	public static final String REPLACE_MARKER = "replace";
	
	public static final String STREAM_MARKER = "stream";
	public static final String LIST_MARKER = "list";
	public static final String MAP_MARKER = "map";
	
	protected static final TripartateParser parser = new TripartateParser(); 
	
	
	public TripartateTemplate(TemplateSource templateSource, ElementStreamProcessor processor, Map<String, String> sections, Properties hints) {
		this.sections.putAll(sections);
		this.processor = processor;
		this.hints = hints;
		compileSections();
	}
	
	protected void compileSections() {
		for(String name : sections.keySet()) {
			String templateText = sections.get(name);
			if(org.apache.commons.lang.StringUtils.isBlank(templateText)) {
				continue;
			}
			List<Element> compiled = parser.parse(templateText);
			compiledSections.put(name, compiled);
		}
	}
	
	public void render(SegmentedOutput output, Location location, String elementSourceName, String... processingHints) {
		
		for(String sectionName : createSectionRenderOrder()) {
			String hintsString = hints.getProperty(sectionName);
			if(hintsString == null) {
				hintsString = hints.getProperty("$default");
			}
			
			String[] hints = hintsString.split(",");
			
			List<Element> elements = compiledSections.get(sectionName);
			if(elements == null) {
				continue;
			}
			
			if(ReflectionUtils.contains(hints, STREAM_MARKER) && ReflectionUtils.contains(hints, APPEND_MARKER)) {
				// if we're appending to the stream, we don't have to do anything because the natural
				// behavior of our processors should do everything we want
				processor.process(location, output, elements, sectionName, hints);
			}
			else {
				// we'll have to create an overlay so that we can pick up all of the additions and post process
				// them either to replace the current text or to split it into list or map formats
				SegmentedOutputOverlay overlay = new SegmentedOutputOverlay(output, sectionName);
				processor.process(location, overlay, elements, sectionName, hints);
				if(ReflectionUtils.contains(hints, STREAM_MARKER) && ReflectionUtils.contains(hints, REPLACE_MARKER)) {
					// easy enough
					StringBuilder sb = output.getStream(sectionName);
					sb.delete(0, sb.length());
					sb.append(overlay.getStream(sectionName));
				}
				else if(ReflectionUtils.contains(hints, LIST_MARKER)) {
					if(ReflectionUtils.contains(hints, REPLACE_MARKER)) {
						output.getList(sectionName).clear();
					}
					
					output.getList(sectionName).addAll(parseList(overlay.getStream(sectionName).toString()));
				}
				else if(ReflectionUtils.contains(hints, MAP_MARKER)) {
					Map<String, String> newProperties = parseProperties(overlay.getStream(sectionName).toString());
					if(ReflectionUtils.contains(hints, REPLACE_MARKER)) {
						output.getPropertySet(sectionName).putAll(newProperties);
					}
					else if(ReflectionUtils.contains(hints, APPEND_MARKER)) {
						Map<String, String> existing = output.getPropertySet(sectionName);
						for(String key : newProperties.keySet()) {
							String value = existing.get(key);
							if(value == null) {
								value = "";
							}
							value += newProperties.get(key);
							existing.put(key, value);
						}
					}
				}
			}
		}
	}
	
	protected List<String> createSectionRenderOrder() {
		List<String> order = new ArrayList<String>();
		String orderHints = hints.getProperty("$order");
		if(org.apache.commons.lang.StringUtils.isBlank(orderHints) == false) {
			for(String s : orderHints.split(",")) {
				order.add(s);
			}
		}
		
		for(String s : sections.keySet()) {
			if(order.contains(s) == false) {
				order.add(s);
			}
		}
		
		return order;
	}
	
	protected List<String> parseList(String info) {
		List<String> result = new ArrayList<String>();
		
		info = StringUtils.replaceString(info, "\\r\\n", "\\n");
		for(String s : info.split("\\n")) {
			result.add(s);
		}
		
		return result;
	}
	
	protected Map<String, String> parseProperties(String info) {
		Properties properties = new OrderedProperties();
		try {
			properties.load(new ByteArrayInputStream(info.getBytes()));
		}
		catch(Exception ex) {
			log.error("Could not parse properties", ex);
		}
		
		Map<String, String> result = new LinkedHashMap<String, String>();
		for(Object key : properties.keySet()) {
			result.put(key.toString(), properties.getProperty(key.toString()));
		}
		return result;
	}

	public Map<String, String> getSections() {
		return sections;
	}

	public void setSections(Map<String, String> sections) {
		this.sections = sections;
	}

	public Properties getHints() {
		return hints;
	}

	public void setHints(Properties hints) {
		this.hints = hints;
	}

	public ElementStreamProcessor getProcessor() {
		return processor;
	}

	public void setProcessor(ElementStreamProcessor processor) {
		this.processor = processor;
	}

	public Logger getLog() {
		return log;
	}

	public void setLog(Logger log) {
		this.log = log;
	}
}
