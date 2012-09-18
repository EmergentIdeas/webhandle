package com.emergentideas.webhandle.handlers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.emergentideas.webhandle.output.SegmentedOutput;
import com.emergentideas.webhandle.templates.Element;
import com.emergentideas.webhandle.templates.ElementProcessor;
import com.emergentideas.webhandle.templates.ElementStreamProcessor;
import com.emergentideas.webhandle.templates.TripartateParser;

public class UrlRequestElementsProcessor extends ElementStreamProcessor {
	
	protected List<ElementProcessor> processors = Collections.synchronizedList(new ArrayList<ElementProcessor>());
	protected TripartateParser parser = new UrlRequestParser();
	
	public UrlRequestElementsProcessor() {
		processors.add(new StringElementProcessor());
		processors.add(new ParameterElementProcessor());
	}
	
	public UrlRegexOutput process(String url) {
		List<Element> elements = parser.parse(url);
		SegmentedOutput output = new SegmentedOutput();
		process(null, output, processors, elements, null, null);
		return new UrlRegexOutput(output);
	}

}
