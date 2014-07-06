package com.emergentideas.webhandle.configurations;

import com.emergentideas.webhandle.ParameterMarshalConfiguration;
import com.emergentideas.webhandle.composites.db.DbInvestigator;
import com.emergentideas.webhandle.investigators.BeanPropertyNameAndDbTransformerInvestigator;
import com.emergentideas.webhandle.investigators.DebugInfoParameterNameInvestigator;
import com.emergentideas.webhandle.investigators.NameAnnotationPropertyNameInvestigator;
import com.emergentideas.webhandle.investigators.ResourceAnnotationPropertyNameInvestigator;
import com.emergentideas.webhandle.investigators.SourceAnnotationSourceSetInvestigator;
import com.emergentideas.webhandle.investigators.TransformersAnnotationTransformersInvestigator;
import com.emergentideas.webhandle.transformers.FileItemToBytesTransformer;
import com.emergentideas.webhandle.transformers.FileItemToStringTransformer;
import com.emergentideas.webhandle.transformers.NumberToStringTransformer;
import com.emergentideas.webhandle.transformers.StringToBigDecimalTransformer;
import com.emergentideas.webhandle.transformers.StringToBooleanTransformer;
import com.emergentideas.webhandle.transformers.StringToDateTransformer;
import com.emergentideas.webhandle.transformers.StringToDoubleTransformer;
import com.emergentideas.webhandle.transformers.StringToEnumTransformer;
import com.emergentideas.webhandle.transformers.StringToFloatTransformer;
import com.emergentideas.webhandle.transformers.StringToIntegerTransformer;
import com.emergentideas.webhandle.transformers.StringToJsonArrayTransformer;
import com.emergentideas.webhandle.transformers.StringToJsonObjectTransformer;
import com.emergentideas.webhandle.transformers.StringToJsonStructureTransformer;

public class IntegratorConfiguration extends ParameterMarshalConfiguration {
	
	public IntegratorConfiguration() {
		addCustomInvestigators();
		
		addNameAnnotationNameInvestigators();
		
		// adding composite investigators first so they take precedence
		addDbInvestigators();
		addBeanPropertyInvestigators();
		
		addDefaultParameterNameInvestigators();
		addDefaultSourceSetInvestigators();
		addDefaultTransformerInvestigators();
		
		addAutomaticTypeTransformers();

		addObjectorInvestigators();
		
		addOutputInvestigators();
	}
	
	protected void addCustomInvestigators() {
	}
	
	protected void addNameAnnotationNameInvestigators() {
		getParameterNameInvestigators().add(new NameAnnotationPropertyNameInvestigator());
	}
	
	protected void addOutputInvestigators() {
		
	}
	
	protected void addObjectorInvestigators() {
	}
	
	protected void addAutomaticTypeTransformers() {
		getTypeTransformers().add(new NumberToStringTransformer());
		getTypeTransformers().add(new StringToBooleanTransformer());
		getTypeTransformers().add(new StringToDateTransformer());
		getTypeTransformers().add(new StringToIntegerTransformer());
		getTypeTransformers().add(new StringToDoubleTransformer());
		getTypeTransformers().add(new StringToBigDecimalTransformer());
		getTypeTransformers().add(new StringToFloatTransformer());
		getTypeTransformers().add(new StringToEnumTransformer());
		getTypeTransformers().add(new FileItemToBytesTransformer());
		getTypeTransformers().add(new FileItemToStringTransformer());
		getTypeTransformers().add(new StringToJsonStructureTransformer());
		getTypeTransformers().add(new StringToJsonObjectTransformer());
		getTypeTransformers().add(new StringToJsonArrayTransformer());
	}
	
	protected void addDbInvestigators() {
		// add the db investigator
		DbInvestigator dbInvestigator = new DbInvestigator();
		getParameterNameInvestigators().add(dbInvestigator);
		getTransformersInvestigators().add(dbInvestigator);
	}
	
	protected void addBeanPropertyInvestigators() {
		getParameterNameInvestigators().add(new ResourceAnnotationPropertyNameInvestigator());

		// add the investigator for populating the members of a bean
		BeanPropertyNameAndDbTransformerInvestigator beanDbInvestigator = new BeanPropertyNameAndDbTransformerInvestigator();
		getParameterNameInvestigators().add(beanDbInvestigator);
		getTransformersInvestigators().add(beanDbInvestigator);
	}
	
	protected void addDefaultParameterNameInvestigators() {
		getParameterNameInvestigators().add(new DebugInfoParameterNameInvestigator());
	}
	
	protected void addDefaultSourceSetInvestigators() {
		getSourceSetInvestigators().add(new SourceAnnotationSourceSetInvestigator());
	}
	
	protected void addDefaultTransformerInvestigators() {
		getTransformersInvestigators().add(new TransformersAnnotationTransformersInvestigator());
	}


}
