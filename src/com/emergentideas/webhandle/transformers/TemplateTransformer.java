package com.emergentideas.webhandle.transformers;

import com.emergentideas.logging.Logger;
import com.emergentideas.logging.SystemOutLogger;
import com.emergentideas.webhandle.Location;
import com.emergentideas.webhandle.WebAppLocation;
import com.emergentideas.webhandle.output.SegmentedOutput;
import com.emergentideas.webhandle.templates.TemplateInstance;

public class TemplateTransformer {
	
	protected Logger log = SystemOutLogger.get(TemplateTransformer.class);
	
	public void transform(String response, SegmentedOutput output, Location location) {
		TemplateInstance ti = new WebAppLocation(location).getTemplateSource().get(response);
		if(ti == null) {
			log.error("Could not find template: " + response);
		}
		else {
			try {
				ti.render(output, location, null, null);
			}
			catch(Exception e) {
				log.error("Some unknown exception when rendering template: " + response, e);
			}
		}
	}

}
