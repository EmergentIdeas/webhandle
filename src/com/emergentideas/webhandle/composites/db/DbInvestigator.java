package com.emergentideas.webhandle.composites.db;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import com.emergentideas.webhandle.Constants;
import com.emergentideas.webhandle.InvocationContext;
import com.emergentideas.webhandle.Name;
import com.emergentideas.webhandle.ParameterNameInvestigator;
import com.emergentideas.webhandle.ParameterTransformersInvestigator;
import com.emergentideas.webhandle.transformers.TransformerSpecification;

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
			InvocationContext context, Integer argumentIndex) {
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
