package com.emergentideas.webhandle.templates.instances;

import com.emergentideas.webhandle.Location;
import com.emergentideas.webhandle.output.SegmentedOutput;
import com.emergentideas.webhandle.templates.TemplateDef;
import com.emergentideas.webhandle.templates.TemplateInstance;

@TemplateDef({"dus", "double-under-score"})
public class DoubleUnderscore implements TemplateInstance {

	public void render(SegmentedOutput output, Location location,
			String elementSourceName, String... processingHints) {
		output.getStream(elementSourceName).append("__");
	}

}
