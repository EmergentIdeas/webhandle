package com.emergentideas.webhandle.transformers;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonStructure;

import com.emergentideas.webhandle.InvocationContext;
import com.emergentideas.webhandle.ValueTransformer;
import com.emergentideas.webhandle.exceptions.TransformationException;

public class StringToJsonArrayTransformer implements ValueTransformer<String, String, JsonArray[]> {

	@Override
	public JsonArray[] transform(InvocationContext context,
			Map<String, String> transformationProperties,
			Class finalParameterClass, String parameterName, String... source)
			throws TransformationException {
		List<JsonArray> result = new ArrayList<JsonArray>();
		
		for(int index = 0; index < source.length; index++) {
			JsonStructure str = Json.createReader(new StringReader(source[index])).read();
			if(str instanceof JsonArray) {
				result.add((JsonArray)str);
			}
		}
		
		if(result.size() == 0) {
			return null;
		}
		
		return result.toArray(new JsonArray[result.size()]);
	}

	
}
