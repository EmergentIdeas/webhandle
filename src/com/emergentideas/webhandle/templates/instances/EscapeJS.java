package com.emergentideas.webhandle.templates.instances;

import org.apache.commons.lang.StringEscapeUtils;

import com.emergentideas.webhandle.templates.TemplateDef;

@TemplateDef("escjs")
public class EscapeJS extends EscapeTemplate {

	@Override
	protected String escape(String s) {
		return StringEscapeUtils.escapeJavaScript(s);
	}

	
}
