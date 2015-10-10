package com.emergentideas.webhandle.files;

import java.io.IOException;
import java.io.InputStream;

public class ConstrainedInputStream extends InputStream {
	final InputStream decorated;
	long length;
	
	public ConstrainedInputStream(InputStream decorated, long length) {
		this.decorated = decorated;
		this.length = length;
	}

	@Override public int read() throws IOException {
		return (length-- <= 0) ? -1 : decorated.read();
	}
}
