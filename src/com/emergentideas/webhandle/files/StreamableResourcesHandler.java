package com.emergentideas.webhandle.files;

import com.emergentideas.logging.Logger;
import com.emergentideas.logging.SystemOutLogger;
import com.emergentideas.utils.StringUtils;
import com.emergentideas.webhandle.exceptions.CouldNotHandleException;
import com.emergentideas.webhandle.handlers.Handle;
import com.emergentideas.webhandle.handlers.HttpMethod;
import com.emergentideas.webhandle.output.DirectRespondent;

public class StreamableResourcesHandler {

	protected StreamableResourceSource source;
	protected Logger log = SystemOutLogger.get(StreamableResourcesHandler.class);
	
	public StreamableResourcesHandler(StreamableResourceSource source) {
		this.source = source;
	}
	
	@Handle(value = "/{filePath:.+}", method = HttpMethod.GET)
	public Object handle(String filePath) {
		StreamableResource resource = source.get(filePath);
		if(resource == null) {
			throw new CouldNotHandleException();
		}
		
		try {
			return new DirectRespondent(StringUtils.readStreamBytes(resource.getContent()), 200, null);
		}
		catch(Exception e) {
			log.error("Could not serve content for path: " + filePath, e);
			throw new RuntimeException(e);
		}
	}
}
