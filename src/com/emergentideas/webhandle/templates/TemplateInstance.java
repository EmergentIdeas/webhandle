package com.emergentideas.webhandle.templates;

import com.emergentideas.webhandle.Location;
import com.emergentideas.webhandle.output.SegmentedOutput;

public interface TemplateInstance {

	public void render(SegmentedOutput output, Location location);
}
