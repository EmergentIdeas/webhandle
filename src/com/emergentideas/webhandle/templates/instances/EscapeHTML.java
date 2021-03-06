package com.emergentideas.webhandle.templates.instances;

import org.apache.commons.lang.StringEscapeUtils;

import com.emergentideas.webhandle.templates.TemplateDef;

@TemplateDef("esc")
public class EscapeHTML extends EscapeTemplate {

	@Override
	protected String escape(String s) {
		return StringEscapeUtils.escapeHtml(s);
	}

	
}
