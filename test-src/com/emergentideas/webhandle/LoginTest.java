package com.emergentideas.webhandle;

import java.io.File;
import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.emergentideas.webhandle.InvocationContext;
import com.emergentideas.webhandle.ParameterMarshal;
import com.emergentideas.webhandle.assumptions.oak.AppLoader;
import com.emergentideas.webhandle.assumptions.oak.CompositeTemplateSource;
import com.emergentideas.webhandle.assumptions.oak.HandleCaller;
import com.emergentideas.webhandle.assumptions.oak.LibraryTemplateSource;
import com.emergentideas.webhandle.configurations.WebParameterMarsahalConfiguration;
import com.emergentideas.webhandle.configurations.WebRequestContextPopulator;
import com.emergentideas.webhandle.handlers.HandleAnnotationHandlerInvestigator;
import com.emergentideas.webhandle.handlers.Handler1;
import com.emergentideas.webhandle.investigators.TemplateOutputTransformersInvestigator;
import com.emergentideas.webhandle.output.Respondent;
import com.emergentideas.webhandle.output.SegmentedOutput;
import com.emergentideas.webhandle.templates.TemplateInstance;
import com.emergentideas.webhandle.templates.TripartateFileTemplateSource;

public class LoginTest extends HttpServlet {

	protected Respondent handle;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		
		AppLoader loader = new AppLoader();
		try {
			loader.load(new File(config.getServletContext().getRealPath("/loginTest.conf")));
			WebAppLocation webApp = new WebAppLocation(loader.getLocation());
			handle = (Respondent)webApp.getServiceByName("request-handler");
		}
		catch(Exception e) {
			e.printStackTrace();
		}

	}


	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		handle.respond(getServletContext(), request, response);
	}


}
