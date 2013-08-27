package com.emergentideas.webhandle.templates.instances;

import org.apache.commons.lang.StringEscapeUtils;

import com.emergentideas.webhandle.Location;
import com.emergentideas.webhandle.output.SegmentedOutput;
import com.emergentideas.webhandle.templates.TemplateInstance;

public class EscapeTemplate implements TemplateInstance {

	public void render(SegmentedOutput output, Location location,
			String elementSourceName, String... processingHints) {
		Object o = location.get("$this");
		if(o != null) {
			output.getStream(elementSourceName).append(escape(convertToString(o)));
		}

	}
	
	protected String convertToString(Object o) {
		return o.toString();
	}
	
	protected String escape(String s) {
		return StringEscapeUtils.escapeHtml(s);
	}

}
