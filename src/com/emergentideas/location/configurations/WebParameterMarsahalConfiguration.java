package com.emergentideas.location.configurations;

import com.emergentideas.location.ParameterMarshalConfiguration;
import com.emergentideas.location.composites.db.DbInvestigator;
import com.emergentideas.location.investigators.BeanPropertyNameAndDbTransformerInvestigator;
import com.emergentideas.location.investigators.NameAnnotationPropertyNameInvestigator;
import com.emergentideas.location.investigators.SourceAnnotationSourceSetInvestigator;
import com.emergentideas.location.investigators.TransformersAnnotationTransformersInvestigator;
import com.emergentideas.location.transformers.NumberToStringTransformer;
import com.emergentideas.location.transformers.StringToBooleanTransformer;
import com.emergentideas.location.transformers.StringToDateTransformer;
import com.emergentideas.location.transformers.StringToDoubleTransformer;
import com.emergentideas.location.transformers.StringToIntegerTransformer;

public class WebParameterMarsahalConfiguration extends
		ParameterMarshalConfiguration {

	public WebParameterMarsahalConfiguration() {
		
		getTypeTransformers().add(new NumberToStringTransformer());
		getTypeTransformers().add(new StringToBooleanTransformer());
		getTypeTransformers().add(new StringToDateTransformer());
		getTypeTransformers().add(new StringToIntegerTransformer());
		getTypeTransformers().add(new StringToDoubleTransformer());
		
		getParameterNameInvestigators().add(new NameAnnotationPropertyNameInvestigator());
		getSourceSetInvestigators().add(new SourceAnnotationSourceSetInvestigator());
		getTransformersInvestigators().add(new TransformersAnnotationTransformersInvestigator());
		
		// add the db investigator
		DbInvestigator dbInvestigator = new DbInvestigator();
		getParameterNameInvestigators().add(dbInvestigator);
		getTransformersInvestigators().add(dbInvestigator);
		
		// add the investigator for populating the members of a bean
		BeanPropertyNameAndDbTransformerInvestigator beanDbInvestigator = new BeanPropertyNameAndDbTransformerInvestigator();
		getParameterNameInvestigators().add(beanDbInvestigator);
		getTransformersInvestigators().add(beanDbInvestigator);
		
	}
}
