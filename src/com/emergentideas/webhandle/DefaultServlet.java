package com.emergentideas.webhandle;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.emergentideas.webhandle.assumptions.oak.AppLoader;
import com.emergentideas.webhandle.output.Respondent;

public class DefaultServlet extends HttpServlet {

	protected Respondent handle;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		
		AppLoader loader = new AppLoader();
		try {
			String hostAppsFileName = getServletContext().getInitParameter("host.apps.definitions");
			File conf = null;
			if(StringUtils.isBlank(hostAppsFileName) == false ) {
				conf = new File(hostAppsFileName);
				if(conf.isAbsolute() == false) {
					conf = new File(getServletContext().getRealPath(hostAppsFileName));
				}
				if(conf.exists() == false) {
					conf = null;
				}
			}

			if(conf != null) {
				WebAppLocation webApp = new WebAppLocation(loader.getLocation());
				webApp.setServiceByType(ServletConfig.class.getName(), config);
				webApp.setServiceByType(ServletContext.class.getName(), getServletContext());
				loader.load(conf);
				handle = (Respondent)webApp.getServiceByName("request-handler");
			}
			else {
				System.err.println("No configuration found");
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}

	}


	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if(handle == null) {
			System.err.println("No configuration found");
		}
		else {
			handle.respond(getServletContext(), request, response);
		}
	}

}
