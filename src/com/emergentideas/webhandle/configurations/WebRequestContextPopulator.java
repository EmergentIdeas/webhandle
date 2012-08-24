package com.emergentideas.webhandle.configurations;

import com.emergentideas.webhandle.AppLocation;
import com.emergentideas.webhandle.Constants;
import com.emergentideas.webhandle.InvocationContext;
import com.emergentideas.webhandle.Location;
import com.emergentideas.webhandle.ParameterMarshal;
import com.emergentideas.webhandle.output.IterativeOutputCreator;
import com.emergentideas.webhandle.output.OutputCreator;
import com.emergentideas.webhandle.output.Respondent;
import com.emergentideas.webhandle.output.SegmentedOutput;

public class WebRequestContextPopulator {

	/**
	 * Adds the SegmentedOutput and iterative respondent classes.  Also creates a new
	 * request level Location and assigns it to the context.
	 * @param marshal
	 * @param context
	 */
	public void populate(ParameterMarshal marshal, InvocationContext context) {
		
		context.setFoundParameter(SegmentedOutput.class, new SegmentedOutput());
		
		IterativeOutputCreator creator = new IterativeOutputCreator(marshal, null);
		
		context.setFoundParameter(Respondent.class, creator);
		context.setFoundParameter(OutputCreator.class, creator);
		
		// configure the location
		AppLocation loc = new AppLocation(context.getLocation());
		loc.put(Constants.REQUEST_LOCATION, loc);
		context.setLocation(loc);
		context.setFoundParameter(Location.class, loc);
	}
}
