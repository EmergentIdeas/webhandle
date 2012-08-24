package com.emergentideas.webhandle.templates;

import com.emergentideas.webhandle.InvocationContext;
import com.emergentideas.webhandle.output.SegmentedOutput;

public interface ElementProcessor {
	
	/**
	 * Processes an element making any changes needed to the output.
	 * @param context The context that the element is being processed in
	 * @param output The accumulated output of processing other Elements
	 * @param element The element which should be analyzed for content to be
	 * added to the output
	 * @return Returns true if no further processing of this element is needed, false otherwise
	 */
	public boolean process(InvocationContext context, SegmentedOutput output, Element element);

}
