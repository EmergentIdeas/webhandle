package com.emergentideas.webhandle.assumptions.oak;

import com.emergentideas.webhandle.InvocationContext;
import com.emergentideas.webhandle.Location;
import com.emergentideas.webhandle.PreRequest;
import com.emergentideas.webhandle.output.HTML5SegmentedOutput;
import com.emergentideas.webhandle.output.SegmentedOutput;

public class RequestObjectSetup {

	@PreRequest
	public void setup(Location location, InvocationContext context) {
		context.setFoundParameter(SegmentedOutput.class, new HTML5SegmentedOutput());
		
		RequestMessages errors = new RequestMessages();
		context.setFoundParameter(RequestMessages.class, errors);
		location.put("messages", errors);
	}
}
