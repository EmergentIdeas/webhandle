package com.emergentideas.webhandle.transformers;

import java.util.Map;

public class TransformerSpecification {
	
	protected String transformerName;
	protected Map<String, ?> transformerProperties;
	
	public TransformerSpecification() {
		
	}
	
	public TransformerSpecification(String transformerName) {
		this.transformerName = transformerName;
	}
	
	public TransformerSpecification(String transformerName, Map<String, ?> transformerProperties) {
		this(transformerName);
		this.transformerProperties = transformerProperties;
	}
	
	
	public String getTransformerName() {
		return transformerName;
	}
	public void setTransformerName(String transformerName) {
		this.transformerName = transformerName;
	}
	public Map<String, ?> getTransformerProperties() {
		return transformerProperties;
	}
	public void setTransformerProperties(Map<String, ?> transformerProperties) {
		this.transformerProperties = transformerProperties;
	}
	
	
}
