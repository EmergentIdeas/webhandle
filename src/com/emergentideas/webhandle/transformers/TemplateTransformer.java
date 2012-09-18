package com.emergentideas.webhandle.transformers;

import com.emergentideas.webhandle.Location;
import com.emergentideas.webhandle.WebAppLocation;
import com.emergentideas.webhandle.output.SegmentedOutput;

public class TemplateTransformer {
	
	public void transform(String response, SegmentedOutput output, Location location) {
		new WebAppLocation(location).getTemplateSource().get(response).render(output, location, null, null);
	}

}
