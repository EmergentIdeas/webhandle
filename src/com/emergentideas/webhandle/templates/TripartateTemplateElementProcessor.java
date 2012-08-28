package com.emergentideas.webhandle.templates;

import java.util.List;

import com.emergentideas.logging.Logger;
import com.emergentideas.logging.SystemOutLogger;
import com.emergentideas.webhandle.AppLocation;
import com.emergentideas.webhandle.InvocationContext;
import com.emergentideas.webhandle.Location;
import com.emergentideas.webhandle.WebAppLocation;
import com.emergentideas.webhandle.output.SegmentedOutput;

public class TripartateTemplateElementProcessor implements ElementProcessor {

	protected Logger logger = SystemOutLogger.get(TripartateTemplateElementProcessor.class);
	
	public TripartateTemplateElementProcessor() {
	}
	
	public boolean process(Location location, SegmentedOutput output, Element element) {
		if(element instanceof TripartateElement) {
			TripartateElement te = (TripartateElement)element;
			
			TemplateInstance ti = null;
			
			if(te.getHandlingExpression() != null) {
				ti = WebAppLocation.getTemplateSource(location).get(te.getHandlingExpression());
				if(ti == null) {
					logger.error("Could not find template named: " + te.getHandlingExpression());
					return true;
				}
			}
			
			String dataSelector = te.getDataSelectorExpression();
			if(dataSelector == null || "".equals(dataSelector.trim())) {
				ti.render(output, location);
			}
			else {
				List<Object> data = location.all(dataSelector);
				for(Object o : data) {
					if(ti != null) {
						Location callLocation = new AppLocation(location);
						callLocation.add(o);
						ti.render(output, callLocation);
					}
					else {
						output.getStream("body").append(o.toString());
					}
				}
			}
			
			return true;
		}
		return false;
	}

}
