package com.emergentideas.webhandle.templates;

import com.emergentideas.webhandle.InvocationContext;
import com.emergentideas.webhandle.Location;
import com.emergentideas.webhandle.output.SegmentedOutput;

public interface ElementProcessor {
	
	/**
	 * Processes an element making any changes needed to the output.
	 * @param location The {@link Location} describing the data being processed
	 * @param output The accumulated output of processing other Elements
	 * @param element The element which should be analyzed for content to be
	 * added to the output
	 * @return Returns true if no further processing of this element is needed, false otherwise
	 */
	public boolean process(Location location, SegmentedOutput output, Element element);

}
