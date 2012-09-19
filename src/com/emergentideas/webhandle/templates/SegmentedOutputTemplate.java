package com.emergentideas.webhandle.templates;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

import com.emergentideas.webhandle.output.SegmentedOutput;

public class SegmentedOutputTemplate extends TripartateTemplate {
	
	public SegmentedOutputTemplate(SegmentedOutput output, ElementStreamProcessor processor, Properties hints) {
		super(null, processor, new HashMap<String, String>(), hints);
		
		List<String> processedKeys = new ArrayList<String>();
		
		for(String key : output.getStreamKeys()) {
			sections.put(key, output.getStream(key).toString());
			processedKeys.add(key);
		}
		for(String key : output.getListKeys()) {
			if(processedKeys.contains(key)) {
				continue;
			}
			sections.put(key, toString(output.getList(key)));
			processedKeys.add(key);
		}
		for(String key : output.getPropertySetKeys()) {
			if(processedKeys.contains(key)) {
				continue;
			}
			sections.put(key, toString(output.getPropertySet(key)));
			processedKeys.add(key);
		}
	}
	
	protected String toString(List<String> list) {
		return StringUtils.join(list, "\n");
	}

	protected String toString(Map<String, String> map) {
		List<String> lines = new ArrayList<String>();
		for(Entry<String, String> entry : map.entrySet()) {
			lines.add(entry.getKey() + "=" + entry.getValue());
		}
		return toString(lines);
	}
}
