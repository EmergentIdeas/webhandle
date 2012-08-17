package com.emergentideas.location.composites.db;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import com.emergentideas.location.Constants;
import com.emergentideas.location.InvocationContext;
import com.emergentideas.location.Name;
import com.emergentideas.location.ParameterTransformersInvestigator;
import com.emergentideas.location.ParameterNameInvestigator;
import com.emergentideas.location.transformers.TransformerSpecification;

public class DbInvestigator implements ParameterNameInvestigator,
		ParameterTransformersInvestigator {
	
	protected String dbIdToObjectTransformerName = Constants.DB_TO_OBJECT_TRANSFORMER_NAME_DEFAULT;

	public <T> TransformerSpecification[] determineTransformers(Object focus,
			Method method, Class<T> parameterClass, String parameterName,
			Annotation[] parameterAnnotations, InvocationContext context) {
		for(Annotation annotation : parameterAnnotations) {
			if(annotation instanceof Db) {
				return new TransformerSpecification[] { new TransformerSpecification(dbIdToObjectTransformerName)};
			}
		}
		
		return null;
	}

	public <T> String determineParameterName(Object focus, Method method,
			Class<T> parameterClass, Annotation[] parameterAnnotations,
			InvocationContext context) {
		for(Annotation annotation : parameterAnnotations) {
			if(annotation instanceof Db) {
				return ((Db)annotation).value();
			}
		}
		
		return null;
	}

	public String getDbIdToObjectTransformerName() {
		return dbIdToObjectTransformerName;
	}

	public void setDbIdToObjectTransformerName(String dbIdToObjectTransformerName) {
		this.dbIdToObjectTransformerName = dbIdToObjectTransformerName;
	}

}
