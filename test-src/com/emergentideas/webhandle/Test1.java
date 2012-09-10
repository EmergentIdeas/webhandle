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
import com.emergentideas.webhandle.assumptions.oak.CompositeTemplateSource;
import com.emergentideas.webhandle.assumptions.oak.HandleCaller;
import com.emergentideas.webhandle.assumptions.oak.LibraryTemplateSource;
import com.emergentideas.webhandle.configurations.WebParameterMarsahalConfiguration;
import com.emergentideas.webhandle.configurations.WebRequestContextPopulator;
import com.emergentideas.webhandle.handlers.HandleAnnotationHandlerInvestigator;
import com.emergentideas.webhandle.handlers.Handler1;
import com.emergentideas.webhandle.investigators.TemplateOutputTransformersInvestigator;
import com.emergentideas.webhandle.output.SegmentedOutput;
import com.emergentideas.webhandle.templates.TemplateInstance;
import com.emergentideas.webhandle.templates.TripartateFileTemplateSource;

public class Test1 extends HttpServlet {

	protected HandleCaller caller;
	protected Location appLocation;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		
		TemplateOutputTransformersInvestigator outputInvestigator = new TemplateOutputTransformersInvestigator();
		HandleAnnotationHandlerInvestigator handlerInvestigator = new HandleAnnotationHandlerInvestigator();
		
		Handler1 handler = new Handler1();
		handlerInvestigator.analyzeObject(handler);
		
		caller = new HandleCaller();
		caller.setHandlerInvestigator(handlerInvestigator);
		caller.setOutputInvestigator(outputInvestigator);
		
		appLocation = new AppLocation();
		WebAppLocation web = new WebAppLocation(appLocation).init();
		String templatesDir = config.getServletContext().getRealPath("/WEB-INF/testTemplates");
		File templateHome = new File(templatesDir);
		TripartateFileTemplateSource ts = new TripartateFileTemplateSource(templateHome);
		web.init();
		
		LibraryTemplateSource lts = new LibraryTemplateSource();
		lts.add("arbitrary", new TemplateInstance() {
			
			public void render(SegmentedOutput output, Location location) {
				output.getStream("body").append("this is some arbitrary text");
			}
		});
		
		
		CompositeTemplateSource cts = new CompositeTemplateSource();
		cts.addTemplateSource(ts);
		cts.addTemplateSource(lts);
		
		web.setTemplateSource(cts);

	}


	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		InvocationContext context = new InvocationContext(appLocation);
		ParameterMarshal marshal = new ParameterMarshal(new WebParameterMarsahalConfiguration(), context);
		new WebRequestContextPopulator().populate(marshal, context, request);

		caller.call(getServletContext(), request, response, marshal);
	}


}
