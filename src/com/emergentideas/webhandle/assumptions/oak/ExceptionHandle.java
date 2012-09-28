package com.emergentideas.webhandle.assumptions.oak;

import com.emergentideas.logging.Logger;
import com.emergentideas.logging.SystemOutLogger;
import com.emergentideas.webhandle.ExceptionHandler;
import com.emergentideas.webhandle.output.Template;
import com.emergentideas.webhandle.output.Wrap;

public class ExceptionHandle {

	protected Logger log = SystemOutLogger.get(ExceptionHandle.class);
	
	@ExceptionHandler(Exception.class)
	@Template
	@Wrap("public_page")
	public Object caughtAnException(Exception exception) {
		log.error("Found a generic unanticipated exception.", exception);
		return "errorPage";
	}
}
