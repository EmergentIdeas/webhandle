package com.emergentideas.webhandle.files;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import com.emergentideas.logging.Logger;
import com.emergentideas.logging.SystemOutLogger;

public class FileStreamableResource implements StreamableResource, NamedResource, FixedSizeResource {

	protected File source;
	protected static Logger log = SystemOutLogger.get(FileStreamableResource.class);
	protected String eTag;
	
	public FileStreamableResource(File source) {
		this.source = source;
	}
	
	public FileStreamableResource(File source, String eTag) {
		this.source = source;
		this.eTag = eTag;
	}
	
	public String getEtag() {
		if(eTag != null) {
			return eTag;
		}
		return "" + source.lastModified();
	}

	public InputStream getContent() {
		try {
			return new FileInputStream(source);
		}
		catch(Exception e) {
			log.error("Could not get content for file: " + source.getAbsolutePath(), e);
			throw new IllegalArgumentException(e);
		}
	}

	
	public String getName() {
		return source.getName();
	}

	public String geteTag() {
		return eTag;
	}

	public void seteTag(String eTag) {
		this.eTag = eTag;
	}

	@Override
	public long getSizeInBytes() {
		return source.length();
	}


	
}
