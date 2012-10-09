package com.emergentideas.webhandle.investigators;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.emergentideas.utils.ReflectionUtils;
import com.emergentideas.webhandle.Init;
import com.emergentideas.webhandle.InvocationContext;
import com.emergentideas.webhandle.Location;
import com.emergentideas.webhandle.OutputResponseInvestigator;
import com.emergentideas.webhandle.ParameterMarshal;
import com.emergentideas.webhandle.WebAppLocation;
import com.emergentideas.webhandle.Wire;
import com.emergentideas.webhandle.assumptions.oak.CompositeTemplateSource;
import com.emergentideas.webhandle.assumptions.oak.LibraryTemplateSource;
import com.emergentideas.webhandle.output.BodyRespondent;
import com.emergentideas.webhandle.output.DirectRespondent;
import com.emergentideas.webhandle.output.HtmlDocRespondent;
import com.emergentideas.webhandle.output.IterativeOutputCreator;
import com.emergentideas.webhandle.output.OutputCreator;
import com.emergentideas.webhandle.output.Respondent;
import com.emergentideas.webhandle.output.SegmentedOutput;
import com.emergentideas.webhandle.output.Template;
import com.emergentideas.webhandle.output.Wrap;
import com.emergentideas.webhandle.templates.ElementProcessor;
import com.emergentideas.webhandle.templates.ElementStreamProcessor;
import com.emergentideas.webhandle.templates.ExpressionFactory;
import com.emergentideas.webhandle.templates.SegmentedOutputTemplate;
import com.emergentideas.webhandle.templates.StringElementProcessor;
import com.emergentideas.webhandle.templates.TemplateSource;
import com.emergentideas.webhandle.templates.TripartateTemplateElementProcessor;
import com.emergentideas.webhandle.transformers.ContextRootURLRewriterTransformer;
import com.emergentideas.webhandle.transformers.InputValuesTransformer;
import com.emergentideas.webhandle.transformers.TemplateTransformer;
import com.emergentideas.webhandle.transformers.WrapTransformer;

/**
 * Uses {@link Template} and {@link Wrap} (and perhaps others) to create an OutputCreator capable of 
 * responding to the request.  
 * @author kolz
 *
 */
public class TemplateOutputTransformersInvestigator implements
		OutputResponseInvestigator {
	
	protected ElementStreamProcessor elementStreamProcessor;
	protected ExpressionFactory expressionFactory;
	
	public static final String RESPONSE_WRAPPER_PARAMETER_NAME = "response-wrapper";
	public static final String RESPONSE_PACKAGE_PARAMETER_NAME = "response-package";
	
	
	public TemplateOutputTransformersInvestigator() {
	}
	
	@Init
	public void init() {
		List<ElementProcessor> processors = new ArrayList<ElementProcessor>();
		processors.add(new StringElementProcessor());
		processors.add(new TripartateTemplateElementProcessor(expressionFactory));
		elementStreamProcessor = new ElementStreamProcessor(processors);
	}
	
	
	public Respondent determineTransformers(InvocationContext context,
			Object focus, Method method, Object response) {
		
		if(response instanceof Respondent) {
			return (Respondent)response;
		}
		
		Template template = ReflectionUtils.getAnnotation(method, Template.class);
		Wrap wrap = ReflectionUtils.getAnnotation(method, Wrap.class);
		
		HttpServletRequest request = context.getFoundParameter(HttpServletRequest.class);
		if("none".equalsIgnoreCase(request.getParameter(RESPONSE_WRAPPER_PARAMETER_NAME))) {
			wrap = null;
		}
		
		if(template == null && wrap == null) {
			return new DirectRespondent(response);
		}
		
		OutputCreator creator = new IterativeOutputCreator(context.getFoundParameter(ParameterMarshal.class), response);
		SegmentedOutput output = context.getFoundParameter(SegmentedOutput.class);
		

		if(template != null && wrap == null) {
			TemplateTransformer tt = new TemplateTransformer();
			creator.addTransformer(ReflectionUtils.getFirstMethodCallSpec(tt, "transform"));
		}
		if(wrap != null) {
			// set the name of the the template in the content area should use as a variable
			if(response != null && response instanceof String) {
				context.getLocation().put("contentArea", (String)response);
			}
			
			// set the wrapper template name as the response
			creator.setResponseObject(wrap.value());
			TemplateTransformer tt = new TemplateTransformer();
			creator.addTransformer(ReflectionUtils.getFirstMethodCallSpec(tt, "transform"));
		}

		if(template != null) {
			InputValuesTransformer ivt = new InputValuesTransformer();
			creator.addTransformer(ReflectionUtils.getFirstMethodCallSpec(ivt, "transform"));
			
			ContextRootURLRewriterTransformer contextTransformer = new ContextRootURLRewriterTransformer();
			creator.addTransformer(ReflectionUtils.getFirstMethodCallSpec(contextTransformer, "transform"));
		}
		
		if("body-only".equalsIgnoreCase(request.getParameter(RESPONSE_PACKAGE_PARAMETER_NAME))) {
			creator.setFinalRespondent(new BodyRespondent(output));
		}
		else {
			creator.setFinalRespondent(new HtmlDocRespondent(output));
		}
		
		return creator;
	}


	public ExpressionFactory getExpressionFactory() {
		return expressionFactory;
	}

	@Wire
	public void setExpressionFactory(ExpressionFactory expressionFactory) {
		this.expressionFactory = expressionFactory;
	}
	
	

}
