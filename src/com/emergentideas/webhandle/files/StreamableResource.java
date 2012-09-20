package com.emergentideas.webhandle.files;

import java.io.InputStream;

public interface StreamableResource {

	public String getEtag();
	
	public InputStream getContent();
}
