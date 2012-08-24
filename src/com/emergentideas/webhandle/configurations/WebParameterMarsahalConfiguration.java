package com.emergentideas.webhandle.configurations;

import com.emergentideas.webhandle.ParameterMarshalConfiguration;
import com.emergentideas.webhandle.composites.db.DbInvestigator;
import com.emergentideas.webhandle.investigators.BeanPropertyNameAndDbTransformerInvestigator;
import com.emergentideas.webhandle.investigators.DebugInfoParameterNameInvestigator;
import com.emergentideas.webhandle.investigators.NameAnnotationPropertyNameInvestigator;
import com.emergentideas.webhandle.investigators.SourceAnnotationSourceSetInvestigator;
import com.emergentideas.webhandle.investigators.TransformersAnnotationTransformersInvestigator;
import com.emergentideas.webhandle.objectors.RolesAllowedObjectorInvestigator;
import com.emergentideas.webhandle.transformers.NumberToStringTransformer;
import com.emergentideas.webhandle.transformers.StringToBooleanTransformer;
import com.emergentideas.webhandle.transformers.StringToDateTransformer;
import com.emergentideas.webhandle.transformers.StringToDoubleTransformer;
import com.emergentideas.webhandle.transformers.StringToIntegerTransformer;

public class WebParameterMarsahalConfiguration extends
		ParameterMarshalConfiguration {

	public WebParameterMarsahalConfiguration() {
		
		addCustomInvestigators();
		
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
	
	protected void addOutputInvestigators() {
		
	}
	
	protected void addObjectorInvestigators() {
		getObjectorInvestigators().add(new RolesAllowedObjectorInvestigator());
	}
	
	protected void addAutomaticTypeTransformers() {
		getTypeTransformers().add(new NumberToStringTransformer());
		getTypeTransformers().add(new StringToBooleanTransformer());
		getTypeTransformers().add(new StringToDateTransformer());
		getTypeTransformers().add(new StringToIntegerTransformer());
		getTypeTransformers().add(new StringToDoubleTransformer());
	}
	
	protected void addDbInvestigators() {
		// add the db investigator
		DbInvestigator dbInvestigator = new DbInvestigator();
		getParameterNameInvestigators().add(dbInvestigator);
		getTransformersInvestigators().add(dbInvestigator);
	}
	
	protected void addBeanPropertyInvestigators() {
		// add the investigator for populating the members of a bean
		BeanPropertyNameAndDbTransformerInvestigator beanDbInvestigator = new BeanPropertyNameAndDbTransformerInvestigator();
		getParameterNameInvestigators().add(beanDbInvestigator);
		getTransformersInvestigators().add(beanDbInvestigator);
	}
	
	protected void addDefaultParameterNameInvestigators() {
		getParameterNameInvestigators().add(new NameAnnotationPropertyNameInvestigator());
		getParameterNameInvestigators().add(new DebugInfoParameterNameInvestigator());
		
	}
	
	protected void addDefaultSourceSetInvestigators() {
		getSourceSetInvestigators().add(new SourceAnnotationSourceSetInvestigator());
	}
	
	protected void addDefaultTransformerInvestigators() {
		getTransformersInvestigators().add(new TransformersAnnotationTransformersInvestigator());
	}
}
