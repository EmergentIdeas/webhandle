package com.emergentideas.webhandle.templates;

import com.emergentideas.webhandle.Location;
import com.emergentideas.webhandle.output.SegmentedOutput;

public interface ElementProcessor {
	
	/**
	 * Processes an element making any changes needed to the output.
	 * @param location The {@link Location} describing the data being processed
	 * @param output The accumulated output of processing other Elements
	 * @param element The element which should be analyzed for content to be
	 * added to the output
	 * @param elementSourceName Where the element comes from, like body, header, meta-description
	 * @param processingHints A set of strings that give hints on how to process this element like: append, replace, tripartate, html,
	 * map, list, stream etc.
	 * @return Returns true if no further processing of this element is needed, false otherwise
	 */
	public boolean process(Location location, SegmentedOutput output, Element element, String elementSourceName, String... processingHints);

}
