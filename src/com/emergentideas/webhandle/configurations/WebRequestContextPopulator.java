package com.emergentideas.webhandle.configurations;

import javax.servlet.http.HttpServletRequest;

import com.emergentideas.webhandle.AppLocation;
import com.emergentideas.webhandle.Constants;
import com.emergentideas.webhandle.InvocationContext;
import com.emergentideas.webhandle.Location;
import com.emergentideas.webhandle.ParameterMarshal;
import com.emergentideas.webhandle.output.HTML5SegmentedOutput;
import com.emergentideas.webhandle.output.IterativeOutputCreator;
import com.emergentideas.webhandle.output.OutputCreator;
import com.emergentideas.webhandle.output.Respondent;
import com.emergentideas.webhandle.output.SegmentedOutput;
import com.emergentideas.webhandle.sources.HttpBodyValueSource;

public class WebRequestContextPopulator {

	/**
	 * Calls the non-http specific populate method and adds sources for the request body and header.
	 * @param marshal
	 * @param context
	 * @param request
	 */
	public void populate(ParameterMarshal marshal, InvocationContext context, HttpServletRequest request) {
		populate(marshal, context);
		
		marshal.getSources().put(Constants.REQUEST_BODY_SOURCE_NAME, new HttpBodyValueSource(request));
		context.getLocation().put(Constants.LOCATION_OF_REQUEST, request);
	}
	
	/**
	 * Adds the SegmentedOutput.  Also creates a new
	 * request level Location and assigns it to the context.
	 * @param marshal
	 * @param context
	 */
	public void populate(ParameterMarshal marshal, InvocationContext context) {
		
		context.setFoundParameter(SegmentedOutput.class, new HTML5SegmentedOutput());
		
		// configure the location
		AppLocation loc = new AppLocation(context.getLocation());
		loc.put(Constants.REQUEST_LOCATION, loc);
		context.setLocation(loc);
		context.setFoundParameter(Location.class, loc);
	}
}
