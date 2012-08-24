package com.emergentideas.webhandle.output;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SegmentedOutput {
	
	protected Map<String, StringBuilder> namedStreams = new HashMap<String, StringBuilder>();
	
	protected Map<String, Map<String, String>> namedPropertySets = new HashMap<String, Map<String,String>>();
	
	protected Map<String, List<String>> namedValues = new HashMap<String, List<String>>();

	public StringBuilder getStream(String name) {
		StringBuilder sb = namedStreams.get(name);
		if(sb == null) {
			sb = new StringBuilder();
			namedStreams.put(name, sb);
		}
		return sb;
	}
	
	public Map<String, String> getPropertySet(String name) {
		Map<String, String> props = namedPropertySets.get(name);
		if(props == null) {
			props = new HashMap<String, String>();
			namedPropertySets.put(name, props);
		}
		return props;
	}
	
	public List<String> getList(String name) {
		List<String> list = namedValues.get(name);
		if(list == null) {
			list = new ArrayList<String>();
			namedValues.put(name, list);
		}
		return list;
	}
}
