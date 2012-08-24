package com.emergentideas.webhandle.output;

import com.emergentideas.webhandle.CallSpec;

public interface OutputCreator extends Respondent {
	
	public static final String RESPONSE_VALUE_SOURCE_NAME = "responseValueSource";
	
	public static final String RESPONSE_VALUE_NAME = "response";
	
	public void setResponseObject(Object response);
	
	public void addTransformer(CallSpec spec);
	
	public void setFinalRespondent(Respondent respondent);

}
