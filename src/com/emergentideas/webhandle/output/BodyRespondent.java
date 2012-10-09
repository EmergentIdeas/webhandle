package com.emergentideas.webhandle.output;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BodyRespondent extends HtmlDocRespondent {

	public BodyRespondent(SegmentedOutput output) {
		super(output);
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
			
			
			write(os, output.getStream("bodyPre"), true);
			
			write(os, output.getStream("body"), true);
			
			addJavascriptLibraries(os, output.getList("footerLibraries"));
			addJavascript(os, output.getStream("footerScript"));

			write(os, output.getStream("bodyPost"), true);
		}
		catch(IOException e) {
			log.error("Could not write document output.", e);
		}
	}
	

	
}
