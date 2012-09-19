package com.emergentideas.webhandle.output;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.emergentideas.logging.Logger;
import com.emergentideas.logging.SystemOutLogger;
import com.emergentideas.utils.ReflectionUtils;
import com.emergentideas.webhandle.CallSpec;
import com.emergentideas.webhandle.InvocationContext;
import com.emergentideas.webhandle.ParameterMarshal;
import com.emergentideas.webhandle.ValueSource;

public class IterativeOutputCreator implements OutputCreator {
	
	protected static final String CALL_SPECIFIC_DATA_SOURCE_NAME = "callSpecificDataSourceName";
	
	protected ParameterMarshal marshal;
	protected Object response;
	
	
	protected Respondent finalRespondent;
	protected List<CallSpec> transformers = new ArrayList<CallSpec>();
	
	protected Logger log = SystemOutLogger.get(IterativeOutputCreator.class);
	
	public IterativeOutputCreator(ParameterMarshal marshal, Object response) {
		this(marshal, response, marshal.getContext().getFoundParameter(SegmentedOutput.class));
	}
	
	protected IterativeOutputCreator(ParameterMarshal marshal, Object response, SegmentedOutput output) {
		this.marshal = marshal;
		this.response = response;
		
		if(output == null) {
			output = new HTML5SegmentedOutput();
			marshal.getContext().setFoundParameter(SegmentedOutput.class, output);
		}
		
		marshal.addSource(RESPONSE_VALUE_SOURCE_NAME, new ResponseValueSource());
	}
	
	public IterativeOutputCreator() {
		this(null, null, null);
	}

	public void respond(ServletContext servletContext, HttpServletRequest request, HttpServletResponse response) {
		
		for(CallSpec spec : transformers) {
			try {
				Object ret = marshal.call(spec.getFocus(), spec.getMethod(), spec.isFailOnMissingParameter(), CALL_SPECIFIC_DATA_SOURCE_NAME, spec.getCallSpecificProperties());
				if(ReflectionUtils.isReturnTypeVoid(spec.getMethod()) == false) {
					// If the return type isn't void, we'd like to assign the return value of the transformer
					// to the the current response type even if the value is null.
					this.response = ret;
					
					// If we get a response which is a respondent, then we'll use it as the final respondent
					if(this.response instanceof Respondent) {
						((Respondent)this.response).respond(servletContext, request, response);
						return;
					}
				}
			}
			catch(Throwable t) {
				log.error("Could not transform for method: " + spec.getMethod().toString(), t);
			}
		}
		
		finalRespondent.respond(servletContext, request, response);

	}

	public void addTransformer(CallSpec spec) {
		transformers.add(spec);
	}

	public void setFinalRespondent(Respondent respondent) {
		this.finalRespondent = respondent;
	}

	public void setResponseObject(Object response) {
		this.response = response;
	}
	
	public ParameterMarshal getMarshal() {
		return marshal;
	}

	public void setMarshal(ParameterMarshal marshal) {
		this.marshal = marshal;
	}

	public Object getResponse() {
		return response;
	}

	public void setResponse(Object response) {
		this.response = response;
	}

	public List<CallSpec> getTransformers() {
		return transformers;
	}

	public void setTransformers(List<CallSpec> transformers) {
		this.transformers = transformers;
	}

	public Respondent getFinalRespondent() {
		return finalRespondent;
	}



	class ResponseValueSource implements ValueSource<Object> {

		public <T> Object get(String name, Class<T> type,
				InvocationContext context) {
			if(RESPONSE_VALUE_NAME.equals(name)) {
				return response;
			}
			return null;
		}

		public <T> boolean canGet(String name, Class<T> type,
				InvocationContext context) {
			return RESPONSE_VALUE_NAME.equals(name);
		}

		public boolean isCachable() {
			return false;
		}
		
	}
}
