package com.emergentideas.webhandle.templates;

import com.emergentideas.webhandle.Location;
import com.emergentideas.webhandle.output.SegmentedOutput;

public interface TemplateInstance {

	/**
	 * Allows content to be added to the output stream
	 * @param output
	 * @param location
	 * @param elementSourceName The name like body, header, etc. of the section  that doing the calling
	 * @param processingHints
	 */
	public void render(SegmentedOutput output, Location location, String elementSourceName, String... processingHints);
}
