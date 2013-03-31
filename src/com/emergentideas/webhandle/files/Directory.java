package com.emergentideas.webhandle.files;

import java.io.InputStream;
import java.util.List;

public interface Directory extends Resource {

	public List<Resource> getEntries();
}
