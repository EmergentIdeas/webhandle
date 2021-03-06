package com.emergentideas.webhandle.output;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

import com.emergentideas.logging.Logger;
import com.emergentideas.logging.SystemOutLogger;
import com.emergentideas.utils.DateUtils;

public class HtmlDocRespondent extends DirectRespondent {

	protected SegmentedOutput output;
	protected Logger log = SystemOutLogger.get(HtmlDocRespondent.class);
	protected String characterSet = "UTF-8";
	protected String lineReturn = "\n";
	protected byte[] lineReturnBytes;
	
	
	public HtmlDocRespondent(SegmentedOutput output) {
		this.output = output;
	}
	
	public void respond(ServletContext servletContext,
			HttpServletRequest request, HttpServletResponse response) {

		
		try {
			lineReturnBytes = lineReturn.getBytes(characterSet);
			
			try {
				response.setStatus(Integer.parseInt(output.getStream("status").toString()));
			} catch(NumberFormatException ex) {
				log.error("Could not convert status code to a number: " + output.getStream("status").toString(), ex);
			}
			
			addDefaultHeaders();
			
			Map<String,String> headers = output.getPropertySet("httpHeader");
			for(String key : headers.keySet()) {
				response.addHeader(key, headers.get(key));
			}
			
			OutputStream os = createOutputStream(servletContext, request, response);
			
			// get the doc type and html element created
			write(os, output.getStream("docType"), true);
			write(os, output.getStream("htmlTag"), true);
			
			// add the header content
			write(os, "<head>", true);
			
			write(os, output.getStream("htmlHeader"), true);
			
			
			if(output.getStream("title").length() > 0) {
				write(os, "<title>" + StringEscapeUtils.escapeHtml(output.getStream("title").toString()) + "</title>", true);
			}
			
			// Write the meta tags that are just name/value pairs
			Map<String, String> namedMeta = output.getPropertySet("namedMeta");
			for(String key : namedMeta.keySet()) {
				String value = namedMeta.get(key);
				if(StringUtils.isNotBlank(key) && StringUtils.isNotBlank(value)) {
					write(os, "<meta name=\"" + key + "\" content=\"" + StringEscapeUtils.escapeHtml(value) + "\" />", true);
				}
			}
			
			Map<String, String> cssIncludes = output.getPropertySet("cssIncludes");
			
			if(cssIncludes.size() > 0) {
				write(os, "<style type=\"text/css\">", true);
			}
			for(String key : cssIncludes.keySet()) {
				String mediaQuery = cssIncludes.get(key);
				write(os, "@import url(" + key + ") ", false);
				if(StringUtils.isBlank(mediaQuery) == false) {
					write(os, mediaQuery, false);
				}
				write(os, ";", true);
			}
			if(cssIncludes.size() > 0) {
				write(os, "</style>", true);
			}
			
			
			addJavascriptLibraries(os, output.getList("headerLibraries"));
			
			addJavascript(os, output.getStream("headerScript"));
			
			StringBuilder sb = output.getStream("headerStyle");
			if(sb.length() > 0) {
				write(os, "<style type=\"text/css\">", true);
				write(os, sb, true);
				write(os, "</style>", true);
			}
			
			write(os, "</head>", true);
			
			// done with the header, let's work on the body
			write(os, output.getStream("bodyOpen"), true);
			write(os, output.getStream("bodyPre"), true);
			
			write(os, output.getStream("body"), true);
			
			addJavascriptLibraries(os, output.getList("footerLibraries"));
			addJavascript(os, output.getStream("footerScript"));

			write(os, output.getStream("bodyPost"), true);
			write(os, output.getStream("docClose"), false);
		}
		catch(IOException e) {
			log.error("Could not write document output.", e);
		}
	}
	
	protected void addDefaultHeaders() {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.HOUR, -1);
		
		Map<String,String> headers = output.getPropertySet("httpHeader");
		if(headers.containsKey("Content-Type") == false) {
			headers.put("Content-Type", "text/html; charset=" + characterSet.toLowerCase());
		}
		if(headers.containsKey("Cache-Control") == false) {
			headers.put("Cache-Control", "no-cache, must-revalidate, max-age=0");
		}
		if(headers.containsKey("Pragma") == false) {
			headers.put("Pragma", "no-cache");
		}
		if(headers.containsKey("Expires") == false) {
			headers.put("Expires", DateUtils.htmlExpiresDateFormat().format(c.getTime()));
		}
		if(headers.containsKey("X-UA-Compatible") == false) {
			headers.put("X-UA-Compatible", "IE=edge");
		}
		

	}
	
	protected void addJavascriptLibraries(OutputStream os, List<String> libraries) throws UnsupportedEncodingException, IOException  {
		for(String library : libraries) {
			if(StringUtils.isBlank(library)) {
				continue;
			}
			write(os, "<script type=\"text/javascript\" src=\"" + library + "\"></script>", true);
		}
	}
	
	protected void addJavascript(OutputStream os, StringBuilder sb) throws UnsupportedEncodingException, IOException  {
		if(sb.length() > 0) {
			write(os, "<script type=\"text/javascript\">", true);
			write(os, sb, true);
			write(os, "</script>", true);
		}
	}
	
	protected void write(OutputStream os, StringBuilder s, boolean appendReturn) throws UnsupportedEncodingException, IOException {
		write(os, s.toString(), appendReturn);
	}
	protected void write(OutputStream os, StringBuilder s) throws UnsupportedEncodingException, IOException {
		write(os, s, false);
	}
	
	protected void write(OutputStream os, String s, boolean appendReturn) throws UnsupportedEncodingException, IOException {
		os.write(s.getBytes(characterSet));
		if(appendReturn) {
			appendReturn(os);
		}
	}
	
	protected void appendReturn(OutputStream os) throws IOException {
		os.write(lineReturnBytes);
	}

	public SegmentedOutput getOutput() {
		return output;
	}

	public void setOutput(SegmentedOutput output) {
		this.output = output;
	}

	public Logger getLog() {
		return log;
	}

	public void setLog(Logger log) {
		this.log = log;
	}

	public String getCharacterSet() {
		return characterSet;
	}

	public void setCharacterSet(String characterSet) {
		this.characterSet = characterSet;
	}

	public String getLineReturn() {
		return lineReturn;
	}

	public void setLineReturn(String lineReturn) {
		this.lineReturn = lineReturn;
	}

	
}
