package com.emergentideas.webhandle.handlers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.emergentideas.webhandle.output.SegmentedOutput;

public class UrlRegexOutput {
	
	public static final String REGEX_SEGMENT = "regexSegment";
	public static final String PARAMETER_NAMES_SEGMENT = "parameterNames";
	
	protected SegmentedOutput output;
	
	protected Pattern pattern;
	
	public UrlRegexOutput(SegmentedOutput output) {
		this.output = output;
	}
	
	public StringBuilder getRegexString() {
		return output.getStream(REGEX_SEGMENT);
	}
	
	public List<String> getParameterNames() {
		return output.getList(PARAMETER_NAMES_SEGMENT);
	}
	
	/**
	 * Generates a new Pattern based on the current regex string.  This will
	 * be done automatically and exists only so that the pattern can be regenerated
	 * at will.
	 */
	public void generatePattern() {
		pattern = Pattern.compile(getRegexString().toString());
	}
	
	/**
	 * If the url matches, a map of the parameters is returned.  If it does not match
	 * then null is returned.
	 * @param url The url to match against the regular expression.
	 * @return
	 */
	public Map<String, String> matches(String url) {
		if(pattern == null) {
			generatePattern();
		}
		
		Matcher m = pattern.matcher(url);
		if(m.matches()) {
			Map<String, String> result = new HashMap<String, String>();
			List<String> parameters = getParameterNames();
			
			for(int i = 0; i < parameters.size(); i++) {
				result.put(parameters.get(i), m.group(i + 1));
			}
			
			return result;
		}
		
		return null;
	}

}
