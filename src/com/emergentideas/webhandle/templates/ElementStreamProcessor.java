package com.emergentideas.webhandle.templates;

import java.util.List;

import com.emergentideas.webhandle.InvocationContext;
import com.emergentideas.webhandle.output.SegmentedOutput;

public class ElementStreamProcessor {
	
	public void process(InvocationContext context, SegmentedOutput output, List<ElementProcessor> processors, List<Element> elements) {
		for(Element element : elements) {
			for(ElementProcessor processor : processors) {
				if(processor.process(context, output, element)) {
					continue;
				}
			}
		}
	}

}
