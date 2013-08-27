package com.emergentideas.webhandle.handlers;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletOutputStream;

public class TestServletOutputStream extends ServletOutputStream {

	protected OutputStream out;
	
	public TestServletOutputStream(OutputStream out) {
		this.out = out;
	}
	
	@Override
	public void write(int b) throws IOException {
		out.write(b);
	}
	
}