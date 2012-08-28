package com.emergentideas.webhandle.templates;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.emergentideas.webhandle.InvocationContext;
import com.emergentideas.webhandle.Location;
import com.emergentideas.webhandle.output.SegmentedOutput;

public class ElementStreamProcessor {
	
	protected List<ElementProcessor> processors = Collections.synchronizedList(new ArrayList<ElementProcessor>());
	
	public ElementStreamProcessor() {
		
	}
	
	public ElementStreamProcessor(List<ElementProcessor> processors) {
		setProcessors(processors);
	}
	
	public void process(Location location, SegmentedOutput output, List<Element> elements) {
		process(location, output, processors, elements);
	}
	
	/**
	 * Processes a stream with the given processors instead of the object instance's processors
	 * @param context
	 * @param output
	 * @param processors
	 * @param elements
	 */
	public void process(Location location, SegmentedOutput output, List<ElementProcessor> processors, List<Element> elements) {
		for(Element element : elements) {
			for(ElementProcessor processor : processors) {
				if(processor.process(location, output, element)) {
					continue;
				}
			}
		}
	}
	
	public void setProcessors(List<ElementProcessor> processors) {
		this.processors.addAll(processors);
	}

}
