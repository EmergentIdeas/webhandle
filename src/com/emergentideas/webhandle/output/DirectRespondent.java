package com.emergentideas.webhandle.output;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
		InputStream ins = transformUserObjectToInputStream(output);
		writeToOutputStream(servletContext, request, response, ins);
	}
	
	protected InputStream transformUserObjectToInputStream(Object useObject) {
		InputStream ins = null;
		
		byte[] out = null;
		if(useObject == null) {
			return null;
		}
		else if(useObject instanceof InputStream) {
			ins = (InputStream)useObject;
		}
		else if(useObject instanceof byte[]) {
			out = (byte[])useObject;
		}
		else if(useObject instanceof String == false) {
			useObject = useObject.toString();
		}
		
		try {
			if(useObject instanceof String) {
				out = ((String)useObject).getBytes(encoding);
			}
		}
		catch(UnsupportedEncodingException e) {
			log.error("Could not convert: " + useObject, e);
		}
		
		if(out != null) {
			ins = new ByteArrayInputStream(out);
		}
		
		return ins;
	}
	
	protected void writeToOutputStream(ServletContext servletContext, HttpServletRequest request, 
			HttpServletResponse response, InputStream userMessage) {
		response.setStatus(responseStatus);
		
		if(headers != null) {
			for(String key : headers.keySet()) {
				response.addHeader(key, headers.get(key));
			}
		}
		
		try {
			OutputStream os = createOutputStream(servletContext, request, response);
			if(userMessage != null) {
				byte[] temp = new byte[10000];
				int i;
				while((i = userMessage.read(temp)) > 0) {
					os.write(temp, 0, i);
				}
			}
			
			if(os != null) {
				os.flush();
				os.close();
			}
		}
		catch(IOException e) {
			log.error("Could not write output.", e);
		}
	}
	
	protected OutputStream createOutputStream(ServletContext servletContext, HttpServletRequest request, 
			HttpServletResponse response) throws IOException {
		ServletOutputStream os = response.getOutputStream();
		return os;
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
