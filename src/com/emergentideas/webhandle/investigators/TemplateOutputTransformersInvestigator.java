package com.emergentideas.webhandle.investigators;

import java.lang.reflect.Method;

import com.emergentideas.utils.ReflectionUtils;
import com.emergentideas.webhandle.InvocationContext;
import com.emergentideas.webhandle.OutputResponseInvestigator;
import com.emergentideas.webhandle.ParameterMarshal;
import com.emergentideas.webhandle.output.DirectRespondent;
import com.emergentideas.webhandle.output.HtmlDocRespondent;
import com.emergentideas.webhandle.output.IterativeOutputCreator;
import com.emergentideas.webhandle.output.OutputCreator;
import com.emergentideas.webhandle.output.Respondent;
import com.emergentideas.webhandle.output.SegmentedOutput;
import com.emergentideas.webhandle.output.Template;
import com.emergentideas.webhandle.output.Wrap;
import com.emergentideas.webhandle.templates.TemplateSource;
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
		if(template != null) {
			TemplateTransformer tt = new TemplateTransformer();
			creator.addTransformer(ReflectionUtils.getFirstMethodCallSpec(tt, "transform"));
		}
		if(wrap != null) {
			WrapTransformer wt = new WrapTransformer();
			creator.addTransformer(ReflectionUtils.getFirstMethodCallSpec(wt, "transform"));
		}
		
		creator.setFinalRespondent(new HtmlDocRespondent(context.getFoundParameter(SegmentedOutput.class)));
		
		return creator;
	}

}
