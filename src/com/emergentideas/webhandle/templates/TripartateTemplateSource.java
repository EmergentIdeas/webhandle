package com.emergentideas.webhandle.templates;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.emergentideas.logging.Logger;
import com.emergentideas.logging.SystemOutLogger;
import com.emergentideas.utils.StringUtils;
import com.emergentideas.webhandle.Init;
import com.emergentideas.webhandle.Wire;

public abstract class TripartateTemplateSource implements TemplateSource {

	protected ElementStreamProcessor elementStreamProcessor;
	protected Properties defaultHints;
	protected ExpressionFactory expressionFactory;
	
	protected Logger logger = SystemOutLogger.get(TripartateFileTemplateSource.class);
	
	public static final String HINTS_EXTENSION = "hints";
	
	
	public TripartateTemplateSource() {
		
	}
	
	@Init
	public void init() {
		List<ElementProcessor> processors = new ArrayList<ElementProcessor>();
		processors.add(new StringElementProcessor());
		processors.add(new TripartateTemplateElementProcessor(expressionFactory));
		elementStreamProcessor = new ElementStreamProcessor(processors);
		
		// set up the default hints that will apply if no others have been selected
		defaultHints = new Properties();
		try {
			defaultHints.load(StringUtils.getStreamFromClassPathLocation("com/emergentideas/webhandle/templates/defaultTemplateHints.properties"));
		}
		catch(Exception e) {
			logger.error("Could not load default tripartate template hints", e);
		}
	}

	public ExpressionFactory getExpressionFactory() {
		return expressionFactory;
	}

	@Wire
	public void setExpressionFactory(ExpressionFactory expressionFactory) {
		this.expressionFactory = expressionFactory;
	}


}
