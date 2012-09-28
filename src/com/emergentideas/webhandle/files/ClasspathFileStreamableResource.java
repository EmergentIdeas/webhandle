package com.emergentideas.webhandle.files;

import java.io.InputStream;

import com.emergentideas.logging.Logger;
import com.emergentideas.logging.SystemOutLogger;
import com.emergentideas.utils.StringUtils;

public class ClasspathFileStreamableResource implements StreamableResource {

	protected String location;
	protected String eTag;
	protected static Logger log = SystemOutLogger.get(ClasspathFileStreamableResource.class);
	
	public ClasspathFileStreamableResource(String location) {
		this(location, System.currentTimeMillis() + "");
	}
	
	public ClasspathFileStreamableResource(String location, String eTag) {
		this.location = location;
		this.eTag = eTag;
	}
	
	public String getEtag() {
		return eTag;
	}

	public InputStream getContent() {
		try {
			return StringUtils.getStreamFromClassPathLocation(location);
		}
		catch(Exception e) {
			log.error("Could not get content for file: " + location, e);
			throw new IllegalArgumentException(e);
		}
	}

	
}
