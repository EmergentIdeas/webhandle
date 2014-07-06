package com.emergentideas.webhandle.transformers;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonStructure;

import com.emergentideas.webhandle.InvocationContext;
import com.emergentideas.webhandle.ValueTransformer;
import com.emergentideas.webhandle.exceptions.TransformationException;

public class StringToJsonObjectTransformer implements ValueTransformer<String, String, JsonObject[]> {

	@Override
	public JsonObject[] transform(InvocationContext context,
			Map<String, String> transformationProperties,
			Class finalParameterClass, String parameterName, String... source)
			throws TransformationException {
		List<JsonObject> result = new ArrayList<JsonObject>();
		
		for(int index = 0; index < source.length; index++) {
			JsonStructure str = Json.createReader(new StringReader(source[index])).read();
			if(str instanceof JsonObject) {
				result.add((JsonObject)str);
			}
		}
		
		if(result.size() == 0) {
			return null;
		}
		
		return result.toArray(new JsonObject[result.size()]);
	}

	
}
