package com.emergentideas.webhandle.transformers;

import java.util.Properties;

import com.emergentideas.logging.Logger;
import com.emergentideas.logging.SystemOutLogger;
import com.emergentideas.utils.StringUtils;
import com.emergentideas.webhandle.AppLocation;
import com.emergentideas.webhandle.Location;
import com.emergentideas.webhandle.WebAppLocation;
import com.emergentideas.webhandle.assumptions.oak.CompositeTemplateSource;
import com.emergentideas.webhandle.assumptions.oak.LibraryTemplateSource;
import com.emergentideas.webhandle.output.SegmentedOutput;
import com.emergentideas.webhandle.templates.ElementStreamProcessor;
import com.emergentideas.webhandle.templates.SegmentedOutputTemplate;
import com.emergentideas.webhandle.templates.TemplateInstance;

public class WrapTransformer {
	
	protected String wrapperTemplateName;
	protected ElementStreamProcessor elementStreamProcessor;
	protected Properties defaultHints;
	protected Logger log = SystemOutLogger.get(WrapTransformer.class);
	
	public WrapTransformer(String wrapperTemplateName, ElementStreamProcessor elementStreamProcessor) {
		this.wrapperTemplateName = wrapperTemplateName;
		this.elementStreamProcessor = elementStreamProcessor;
		
		// set up the default hints that will apply if no others have been selected
		defaultHints = new Properties();
		try {
			defaultHints.load(StringUtils.getStreamFromClassPathLocation("com/emergentideas/webhandle/templates/defaultTemplateHints.properties"));
		}
		catch(Exception e) {
			log.error("Could not load default tripartate template hints", e);
		}

	}
	
	
	public void transform(SegmentedOutput output, Location location) {
		
		location = new AppLocation(location);
		
		LibraryTemplateSource lts = new LibraryTemplateSource();
		lts.add("contentArea", new SegmentedOutputTemplate(output, elementStreamProcessor, defaultHints));
		
		CompositeTemplateSource cts = new CompositeTemplateSource();
		WebAppLocation webApp = new WebAppLocation(location);
		TemplateInstance wrapperTemplate = webApp.getTemplateSource().get(wrapperTemplateName);
		
		if(wrapperTemplate != null) {
			cts.addTemplateSource(webApp.getTemplateSource());
			cts.addTemplateSource(lts);
			webApp.setTemplateSource(cts);
			
			SegmentedOutput newOutput = new SegmentedOutput();
			wrapperTemplate.render(newOutput, location, null, null);
			
			output.replaceInfoWith(newOutput);
		}
	}

}
