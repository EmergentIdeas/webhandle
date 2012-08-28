package com.emergentideas.webhandle.transformers;

import com.emergentideas.webhandle.Location;
import com.emergentideas.webhandle.WebAppLocation;
import com.emergentideas.webhandle.output.SegmentedOutput;

public class TemplateTransformer {
	
	public void transform(String response, SegmentedOutput output, Location location) {
		WebAppLocation.getTemplateSource(location).get(response).render(output, location);
	}

}
