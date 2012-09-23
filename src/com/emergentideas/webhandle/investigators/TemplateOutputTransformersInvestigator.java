package com.emergentideas.webhandle.investigators;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.emergentideas.utils.ReflectionUtils;
import com.emergentideas.webhandle.InvocationContext;
import com.emergentideas.webhandle.Location;
import com.emergentideas.webhandle.OutputResponseInvestigator;
import com.emergentideas.webhandle.ParameterMarshal;
import com.emergentideas.webhandle.WebAppLocation;
import com.emergentideas.webhandle.assumptions.oak.CompositeTemplateSource;
import com.emergentideas.webhandle.assumptions.oak.LibraryTemplateSource;
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
	
	public TemplateOutputTransformersInvestigator() {
		List<ElementProcessor> processors = new ArrayList<ElementProcessor>();
		processors.add(new StringElementProcessor());
		processors.add(new TripartateTemplateElementProcessor());
		elementStreamProcessor = new ElementStreamProcessor(processors);
	}
	
	
	public Respondent determineTransformers(InvocationContext context,
			Object focus, Method method, Object response) {
		
		if(response instanceof Respondent) {
			return (Respondent)response;
		}
		
		Template template = ReflectionUtils.getAnnotation(method, Template.class);
		Wrap wrap = ReflectionUtils.getAnnotation(method, Wrap.class);
		
		if(template == null && wrap == null) {
			return new DirectRespondent(response);
		}
		
		OutputCreator creator = new IterativeOutputCreator(context.getFoundParameter(ParameterMarshal.class), response);
		SegmentedOutput output = context.getFoundParameter(SegmentedOutput.class);
		
		
		if(template != null) {
			TemplateTransformer tt = new TemplateTransformer();
			creator.addTransformer(ReflectionUtils.getFirstMethodCallSpec(tt, "transform"));
			
			InputValuesTransformer ivt = new InputValuesTransformer();
			creator.addTransformer(ReflectionUtils.getFirstMethodCallSpec(ivt, "transform"));
			
			ContextRootURLRewriterTransformer contextTransformer = new ContextRootURLRewriterTransformer();
			creator.addTransformer(ReflectionUtils.getFirstMethodCallSpec(contextTransformer, "transform"));
		}
		if(wrap != null) {
			WrapTransformer wt = new WrapTransformer(wrap.value(), elementStreamProcessor);
			creator.addTransformer(ReflectionUtils.getFirstMethodCallSpec(wt, "transform"));
		}
		
		creator.setFinalRespondent(new HtmlDocRespondent(output));
		
		return creator;
	}

}
