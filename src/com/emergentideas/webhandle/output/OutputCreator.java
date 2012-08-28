package com.emergentideas.webhandle.output;

import com.emergentideas.webhandle.CallSpec;
import com.emergentideas.webhandle.Constants;

/**
 * A generic way of applying a number of transformers and writing that transformed object and 
 * {@link SegmentedOutput} to an HTTP response 
 * @author kolz
 *
 */
public interface OutputCreator extends Respondent {
	
	public static final String RESPONSE_VALUE_SOURCE_NAME = "responseValueSource";
	
	public static final String RESPONSE_VALUE_NAME = Constants.HANDLER_RESPONSE;
	
	public void setResponseObject(Object response);
	
	public void addTransformer(CallSpec spec);
	
	public void setFinalRespondent(Respondent respondent);

}
