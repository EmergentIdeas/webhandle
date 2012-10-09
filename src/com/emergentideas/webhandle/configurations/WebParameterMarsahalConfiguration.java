package com.emergentideas.webhandle.configurations;

import com.emergentideas.webhandle.Name;
import com.emergentideas.webhandle.ParameterMarshalConfiguration;
import com.emergentideas.webhandle.WebAppLocation;
import com.emergentideas.webhandle.composites.db.DbInvestigator;
import com.emergentideas.webhandle.investigators.BeanPropertyNameAndDbTransformerInvestigator;
import com.emergentideas.webhandle.investigators.DebugInfoParameterNameInvestigator;
import com.emergentideas.webhandle.investigators.NameAnnotationPropertyNameInvestigator;
import com.emergentideas.webhandle.investigators.SourceAnnotationSourceSetInvestigator;
import com.emergentideas.webhandle.investigators.TransformersAnnotationTransformersInvestigator;
import com.emergentideas.webhandle.objectors.PreCallObjectorInvestegator;
import com.emergentideas.webhandle.objectors.RolesAllowedObjectorInvestigator;
import com.emergentideas.webhandle.transformers.NumberToStringTransformer;
import com.emergentideas.webhandle.transformers.StringToBooleanTransformer;
import com.emergentideas.webhandle.transformers.StringToDateTransformer;
import com.emergentideas.webhandle.transformers.StringToDoubleTransformer;
import com.emergentideas.webhandle.transformers.StringToIntegerTransformer;

@Name(WebAppLocation.WEB_PARAMETER_MARSHAL_CONFIGURATION)
public class WebParameterMarsahalConfiguration extends
		IntegratorConfiguration {

	protected void addObjectorInvestigators() {
		getObjectorInvestigators().add(new RolesAllowedObjectorInvestigator());
		getObjectorInvestigators().add(new PreCallObjectorInvestegator());
	}
}
