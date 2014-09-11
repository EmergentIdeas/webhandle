package com.emergentideas.webhandle.output;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.emergentideas.logging.Logger;
import com.emergentideas.logging.SystemOutLogger;
import com.emergentideas.utils.DateUtils;
import com.emergentideas.webhandle.files.StreamableResource;

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
			// no problem really, it's just that the client closed the connection
//			log.error("Could not write output.", e);
		}
	}
	
	/**
	 * Add cache headers to the result. If <code>cacheSeconds</code> is 0 or less the expires date
	 * will be set to 1 hour in the past.
	 * @param cacheSeconds
	 */
	public void addCacheHeaders(int cacheSeconds) {
		// must-revalidate causes the browser to rigorously adhere to the caching rules without take
		// what the spec refers to as "liberties". It does not, as the name would imply, cause every
		// use of a cached object to be revalidated against the server copy.
		String revalidateSegment = ", must-revalidate";
		
		Calendar c = Calendar.getInstance();
		if(cacheSeconds > 0) {
			c.add(Calendar.SECOND, cacheSeconds);
		}
		else {
			// if the cache time is zero we'll push the expire date back an hour to account for any difference
			// between the client's clock and the server clock
			c.add(Calendar.HOUR, -1);
		}
		
		headers.put("Cache-Control" , (cacheSeconds > 0 ? "public, " : "no-cache, ") + "max-age=" + cacheSeconds + revalidateSegment);
		headers.put("Expires", DateUtils.htmlExpiresDateFormat().format(c.getTime()));
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

	public int getResponseStatus() {
		return responseStatus;
	}

	public void setResponseStatus(int responseStatus) {
		this.responseStatus = responseStatus;
	}

	public Logger getLog() {
		return log;
	}

	public void setLog(Logger log) {
		this.log = log;
	}
}
