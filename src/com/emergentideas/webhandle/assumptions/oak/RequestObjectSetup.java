package com.emergentideas.webhandle.assumptions.oak;

import javax.servlet.http.HttpServletRequest;

import com.emergentideas.webhandle.Constants;
import com.emergentideas.webhandle.InvocationContext;
import com.emergentideas.webhandle.Location;
import com.emergentideas.webhandle.PreRequest;
import com.emergentideas.webhandle.output.HTML5SegmentedOutput;
import com.emergentideas.webhandle.output.SegmentedOutput;

public class RequestObjectSetup {

	@PreRequest
	public void setup(Location location, InvocationContext context, HttpServletRequest request) {
		context.setFoundParameter(SegmentedOutput.class, new HTML5SegmentedOutput());
		
		RequestMessages errors = new RequestMessages();
		context.setFoundParameter(RequestMessages.class, errors);
		location.put("messages", errors);
		
		location.put(Constants.LOCATION_OF_WEB_APP_CONTEXT_PATH, request.getContextPath());
	}
}
