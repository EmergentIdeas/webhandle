package com.emergentideas.webhandle;

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
import com.emergentideas.webhandle.configurations.WebParameterMarsahalConfiguration;
import com.emergentideas.webhandle.configurations.WebRequestContextPopulator;
import com.emergentideas.webhandle.handlers.HandleAnnotationHandlerInvestigator;
import com.emergentideas.webhandle.handlers.HandleCaller;
import com.emergentideas.webhandle.handlers.Handler1;
import com.emergentideas.webhandle.investigators.TemplateOutputTransformersInvestigator;

public class Test1 extends HttpServlet {

	protected HandleCaller caller;
	
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
	}


	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		InvocationContext context = new InvocationContext();
		ParameterMarshal marshal = new ParameterMarshal(new WebParameterMarsahalConfiguration(), context);
		new WebRequestContextPopulator().populate(marshal, context);

		caller.call(getServletContext(), request, response, marshal);
	}


}
