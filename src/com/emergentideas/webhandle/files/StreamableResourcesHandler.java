package com.emergentideas.webhandle.files;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;

import com.emergentideas.logging.Logger;
import com.emergentideas.logging.SystemOutLogger;
import com.emergentideas.utils.DateUtils;
import com.emergentideas.webhandle.Name;
import com.emergentideas.webhandle.exceptions.CouldNotHandleException;
import com.emergentideas.webhandle.handlers.Handle;
import com.emergentideas.webhandle.handlers.HttpMethod;
import com.emergentideas.webhandle.output.DirectRespondent;

public class StreamableResourcesHandler {

	protected StreamableResourceSource source;
	protected Logger log = SystemOutLogger.get(StreamableResourcesHandler.class);
	protected int cacheTime;
	
	public StreamableResourcesHandler(StreamableResourceSource source) {
		this(source, 0);
	}
	
	/**
	 * 
	 * @param source
	 * @param cacheTime The number of seconds to specify before the entry becomes invalid.  
	 * Default is zero causing the client to verify the freshness.
	 */
	public StreamableResourcesHandler(StreamableResourceSource source, int cacheTime) {
		this.source = source;
	}
	
	@Handle(value = "/{filePath:.+}", method = HttpMethod.GET)
	public Object handle(String filePath, ServletContext servletContext, @Name("If-None-Match") String existingETag) {
		StreamableResource resource = source.get(filePath);
		if(resource == null) {
			throw new CouldNotHandleException();
		}
		
		Calendar c = Calendar.getInstance();
		if(cacheTime > 0) {
			c.add(Calendar.SECOND, cacheTime);
		}
		else {
			// if the cache time is zero we'll push the expire date back an hour to account for any difference
			// between the client's clock and the server clock
			c.add(Calendar.HOUR, -1);
		}

		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", servletContext.getMimeType(filePath));
		headers.put("Cache-Control" , (cacheTime > 0 ? "public, " : "no-cache, ") + "max-age=" + cacheTime + ", must-revalidate");
		headers.put("Expires", DateUtils.htmlExpiresDateFormat().format(c.getTime()));
		headers.put("ETag", resource.getEtag());

		if(resource.getEtag().equals(trimETag(existingETag))) {
			return new DirectRespondent(null, 304, headers);
		}
		
		try {
			return new DirectRespondent(resource.getContent(), 200, headers);
		}
		catch(Exception e) {
			log.error("Could not serve content for path: " + filePath, e);
			throw new RuntimeException(e);
		}
	}
	
	protected String trimETag(String eTag) {
		if(eTag == null) {
			return null;
		}
		
		int i = eTag.indexOf('"');
		if(i >= 0) {
			eTag = eTag.substring(i + 1);
		}
		
		i = eTag.lastIndexOf('"');
		if(i >= 0) {
			eTag = eTag.substring(0, i);
		}
		
		return eTag;
	}
}
