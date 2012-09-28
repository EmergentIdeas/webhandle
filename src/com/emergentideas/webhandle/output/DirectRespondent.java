package com.emergentideas.webhandle.output;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.emergentideas.logging.Logger;
import com.emergentideas.logging.SystemOutLogger;

public class DirectRespondent implements Respondent {

	protected String encoding = "UTF-8";
	protected Object output;
	protected Map<String, String> headers = new HashMap<String, String>();
	protected int responseStatus;
	
	protected Logger log = SystemOutLogger.get(DirectRespondent.class);
	
	public DirectRespondent() {
		
	}
	
	public DirectRespondent(Object output) {
		this(output, 200, null);
	}
	
	public DirectRespondent(Object output, int responseStatus, Map<String, String> headers) {
		this.output = output;
		this.responseStatus = responseStatus;
		if(headers != null) {
			this.headers.putAll(headers);
		}
	}
	
	
	public void respond(ServletContext servletContext,
			HttpServletRequest request, HttpServletResponse response) {
		
		response.setStatus(responseStatus);
		
		if(headers != null) {
			for(String key : headers.keySet()) {
				response.addHeader(key, headers.get(key));
			}
		}
		
		InputStream ins = null;
		
		byte[] out = null;
		if(output == null) {
			return;
		}
		else if(output instanceof InputStream) {
			ins = (InputStream)output;
		}
		else if(output instanceof byte[]) {
			out = (byte[])output;
		}
		else if(output instanceof String == false) {
			output = output.toString();
		}
		
		try {
			if(output instanceof String) {
				out = ((String)output).getBytes(encoding);
			}
		}
		catch(UnsupportedEncodingException e) {
			log.error("Could not convert: " + output, e);
		}
		
		if(out != null) {
			ins = new ByteArrayInputStream(out);
		}
		
		if(ins != null) {
			try {
				ServletOutputStream os = response.getOutputStream();
				byte[] temp = new byte[10000];
				int i;
				while((i = ins.read(temp)) > 0) {
					os.write(temp, 0, i);
				}

				os.flush();
			}
			catch(IOException e) {
				log.error("Could not write output.", e);
			}
		}
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public Object getOutput() {
		return output;
	}

	public void setOutput(Object output) {
		this.output = output;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}

	
}
