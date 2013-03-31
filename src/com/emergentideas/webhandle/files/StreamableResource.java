package com.emergentideas.webhandle.files;

import java.io.InputStream;

public interface StreamableResource extends Resource {

	public String getEtag();
	
	public InputStream getContent();
}
