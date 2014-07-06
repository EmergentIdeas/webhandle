package com.emergentideas.webhandle.transformers;

import java.io.StringReader;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonStructure;

import com.emergentideas.webhandle.InvocationContext;
import com.emergentideas.webhandle.ValueTransformer;
import com.emergentideas.webhandle.exceptions.TransformationException;

public class StringToJsonStructureTransformer implements ValueTransformer<String, String, JsonStructure[]> {

	@Override
	public JsonStructure[] transform(InvocationContext context,
			Map<String, String> transformationProperties,
			Class finalParameterClass, String parameterName, String... source)
			throws TransformationException {
		JsonStructure[] result = new JsonStructure[source.length];
		
		for(int index = 0; index < source.length; index++) {
			result[index] = Json.createReader(new StringReader(source[index])).read();
		}
		
		return result;
	}

	
}
