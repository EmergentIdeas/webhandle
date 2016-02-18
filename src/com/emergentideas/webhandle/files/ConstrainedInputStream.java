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

	@Override
	public int read(byte[] b) throws IOException {
		int i = decorated.read(b);
		length -= i;
		return i;
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		int i = super.read(b, off, len);
		length -= i;
		return i;
	}

	@Override
	public long skip(long n) throws IOException {
		return decorated.skip(n);
	}

	@Override
	public int available() throws IOException {
		return decorated.available();
	}

	@Override
	public void close() throws IOException {
		decorated.close();
	}

	@Override
	public synchronized void mark(int readlimit) {
		decorated.mark(readlimit);
	}

	@Override
	public synchronized void reset() throws IOException {
		decorated.reset();
	}

	@Override
	public boolean markSupported() {
		return decorated.markSupported();
	}
	
	
}
