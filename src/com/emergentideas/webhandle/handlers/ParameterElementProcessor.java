package com.emergentideas.webhandle.handlers;

import com.emergentideas.webhandle.Location;
import com.emergentideas.webhandle.output.SegmentedOutput;
import com.emergentideas.webhandle.templates.Element;
import com.emergentideas.webhandle.templates.ElementProcessor;
import com.emergentideas.webhandle.templates.TripartateElement;

public class ParameterElementProcessor implements ElementProcessor {

	public boolean process(Location location, SegmentedOutput output,
			Element element, String elementSourceName, String... processingHints) {
		
		if(element instanceof TripartateElement) {
			UrlRegexOutput urlOut = new UrlRegexOutput(output);
			TripartateElement tp = (TripartateElement)element;
			
			urlOut.getParameterNames().add(tp.getDataSelectorExpression());
			if(tp.getHandlingExpression() == null) {
				urlOut.getRegexString().append("([^/]+)");
			}
			else {
				urlOut.getRegexString().append("(" + tp.getHandlingExpression() + ")");
			}
			
			return true;
		}
		return false;
	}

}
