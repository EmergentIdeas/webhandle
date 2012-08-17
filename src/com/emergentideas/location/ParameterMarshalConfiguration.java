package com.emergentideas.location;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.emergentideas.location.transformers.TransformerParameterDataTransformer;
import com.emergentideas.location.transformers.URLPropertiesTransformerPropertyDataTransformer;

public class ParameterMarshalConfiguration {

	// holds named transformers for converting and transforming request and return data
	protected Map<String, ValueTransformer<?, ?, ?>> transformers = Collections.synchronizedMap(new HashMap<String, ValueTransformer<?,?,?>>());
	
	// holds transforms that a generic for converting between types
	protected List<ValueTransformer<?, ?, ?>> typeTransformers = Collections.synchronizedList(new ArrayList<ValueTransformer<?,?,?>>());
	
	protected List<ParameterNameInvestigator> parameterNameInvestigators = Collections.synchronizedList(new ArrayList<ParameterNameInvestigator>());
	
	protected List<SourceSetInvestigator> sourceSetInvestigators = Collections.synchronizedList(new ArrayList<SourceSetInvestigator>());
	
	protected List<ParameterTransformersInvestigator> transformersInvestigators = Collections.synchronizedList(new ArrayList<ParameterTransformersInvestigator>());
	
	protected TransformerParameterDataTransformer transformerPropertiesDataTransformer = new URLPropertiesTransformerPropertyDataTransformer();

	public Map<String, ValueTransformer<?, ?, ?>> getTransformers() {
		return transformers;
	}

	public List<ValueTransformer<?, ?, ?>> getTypeTransformers() {
		return typeTransformers;
	}

	public List<ParameterNameInvestigator> getParameterNameInvestigators() {
		return parameterNameInvestigators;
	}

	public List<SourceSetInvestigator> getSourceSetInvestigators() {
		return sourceSetInvestigators;
	}

	public List<ParameterTransformersInvestigator> getTransformersInvestigators() {
		return transformersInvestigators;
	}

	public TransformerParameterDataTransformer getTransformerPropertiesDataTransformer() {
		return transformerPropertiesDataTransformer;
	}

	public void setTransformerPropertyDataTransformer(
			TransformerParameterDataTransformer transformerPropertyDataTransformer) {
		this.transformerPropertiesDataTransformer = transformerPropertyDataTransformer;
	}
	

}
