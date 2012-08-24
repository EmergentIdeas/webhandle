package com.emergentideas.webhandle.handlers;

import com.emergentideas.webhandle.InvocationContext;
import com.emergentideas.webhandle.output.SegmentedOutput;
import com.emergentideas.webhandle.templates.Element;
import com.emergentideas.webhandle.templates.ElementProcessor;
import com.emergentideas.webhandle.templates.TripartateElement;

public class ParameterElementProcessor implements ElementProcessor {

	public boolean process(InvocationContext context, SegmentedOutput output,
			Element element) {
		
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
