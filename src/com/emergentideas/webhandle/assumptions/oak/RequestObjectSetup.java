package com.emergentideas.webhandle.assumptions.oak;

import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.emergentideas.logging.Logger;
import com.emergentideas.logging.SystemOutLogger;
import com.emergentideas.webhandle.Constants;
import com.emergentideas.webhandle.InvocationContext;
import com.emergentideas.webhandle.Location;
import com.emergentideas.webhandle.PostResponse;
import com.emergentideas.webhandle.PreRequest;
import com.emergentideas.webhandle.files.StreamableResourcesHandler;
import com.emergentideas.webhandle.output.HTML5SegmentedOutput;
import com.emergentideas.webhandle.output.SegmentedOutput;

public class RequestObjectSetup {
	
	/**
	 * A string that when it prefixes a url for a static (file) resource will cause the the
	 * {@link StreamableResourcesHandler} to add never expires headers and will be stripped
	 * from the start of any url served by them. This value is always in the pattern
	 *  <code>/vrsc/&lt;digits</code>
	 *  
	 *  This value is created only once, so if you'd like to set it to something, you can do
	 *  so at any point in the app load process (or later if you want) and pages will start
	 *  using the new value.
	 */
	protected String resourceCacheKey;
	
	protected Properties segmentedOutputDefaults;
	
	@Resource
	protected RequestTermCache requestTermCache;
	
	protected Logger log = SystemOutLogger.get(RequestObjectSetup.class);

	
	public RequestObjectSetup() {
		resourceCacheKey = "/vrsc/" + (System.currentTimeMillis() / 1000);
	}

	@PreRequest
	public void setup(Location location, InvocationContext context, HttpServletRequest request) {
		context.setFoundParameter(SegmentedOutput.class, new HTML5SegmentedOutput(getDefaultProperties()));
		
		Location sessionLocation = (Location)location.get(Constants.SESSION_LOCATION);
		
		// See if we've got request messages left over from another session
		RequestMessages errors = (RequestMessages)sessionLocation.get(RequestMessages.SESSION_LOCATION_KEY);
		// Clear the messages no matter what
		sessionLocation.put(RequestMessages.SESSION_LOCATION_KEY, null);
		
		if(errors == null) {
			errors = new RequestMessages(sessionLocation);
		}
		
		context.setFoundParameter(RequestMessages.class, errors);
		location.put("messages", errors);
		
		location.put(Constants.LOCATION_OF_WEB_APP_CONTEXT_PATH, request.getContextPath());
		location.put("vrsc", resourceCacheKey);
		
		context.setFoundParameter(ParmManipulator.class, new ParmManipulator(context));
		
		clearCache();
	}
	
	@PostResponse
	public void afterRequest() {
		clearCache();
	}
	
	protected Properties getDefaultProperties() {
		if(segmentedOutputDefaults == null) {
			try {
				segmentedOutputDefaults = HTML5SegmentedOutput.getDefaultProperties();
			}
			catch(Exception e) {
				log.error("Could not initialize the segemented output properties for HTML5", e);
				segmentedOutputDefaults = new Properties();
			}
		}
		return segmentedOutputDefaults;
	}
	
	protected void clearCache() {
		if(requestTermCache != null) {
			requestTermCache.clear();
		}
	}

	public String getResourceCacheKey() {
		return resourceCacheKey;
	}

	public void setResourceCacheKey(String resourceCacheKey) {
		this.resourceCacheKey = resourceCacheKey;
	}

	public RequestTermCache getCache() {
		return requestTermCache;
	}

	public void setCache(RequestTermCache cache) {
		this.requestTermCache = cache;
	}

	public Properties getSegmentedOutputDefaults() {
		return segmentedOutputDefaults;
	}

	public void setSegmentedOutputDefaults(Properties segmentedOutputDefaults) {
		this.segmentedOutputDefaults = segmentedOutputDefaults;
	}

	public RequestTermCache getRequestTermCache() {
		return requestTermCache;
	}

	public void setRequestTermCache(RequestTermCache requestTermCache) {
		this.requestTermCache = requestTermCache;
	}

	public Logger getLog() {
		return log;
	}

	public void setLog(Logger log) {
		this.log = log;
	}
	
	
}
