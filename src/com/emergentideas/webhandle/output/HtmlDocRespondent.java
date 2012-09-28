package com.emergentideas.webhandle.output;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.emergentideas.logging.Logger;
import com.emergentideas.logging.SystemOutLogger;

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
				write(os, "<title>" + output.getStream("title") + "</title>", true);
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
	
	protected void addJavascriptLibraries(OutputStream os, List<String> libraries) throws UnsupportedEncodingException, IOException  {
		for(String library : libraries) {
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
