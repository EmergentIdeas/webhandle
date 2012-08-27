package com.emergentideas.webhandle.output;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.emergentideas.logging.Logger;
import com.emergentideas.logging.SystemOutLogger;

public class DirectRespondent implements Respondent {

	protected String encoding = "UTF-8";
	protected Object output;
	protected Map<String, String> headers = new HashMap<String, String>();
	
	protected Logger log = SystemOutLogger.get(DirectRespondent.class);
	
	public DirectRespondent() {
		
	}
	
	public DirectRespondent(Object output) {
		this(output, null);
	}
	
	public DirectRespondent(Object output, Map<String, String> headers) {
		this.output = output;
		if(headers != null) {
			this.headers.putAll(headers);
		}
	}
	
	
	public void respond(ServletContext servletContext,
			HttpServletRequest request, HttpServletResponse response) {
		
		if(headers != null) {
			for(String key : headers.keySet()) {
				response.addHeader(key, headers.get(key));
			}
		}
		
		byte[] out = null;
		if(output == null) {
			return;
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
			try {
				response.getOutputStream().write(out);
				response.getOutputStream().flush();
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
