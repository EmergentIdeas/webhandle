package com.emergentideas.webhandle.transformers;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import javax.json.JsonArray;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonStructure;
import javax.json.JsonValue;
import javax.json.JsonValue.ValueType;

import org.junit.Test;

public class StringToJsonTransformerTest {

	protected StringToJsonStructureTransformer transformer = new StringToJsonStructureTransformer();
	
	@Test
	public void testTransform() throws Exception {
		
		
		JsonStructure[] o;
		
		o = transformer.transform(null, null, null, null, "{ \"name\": \"hello\", \"val\": 123, \"val2\": 12.3}");
		
		assertEquals(ValueType.OBJECT, o[0].getValueType());
		JsonObject obj = (JsonObject)o[0];
		assertEquals("hello", obj.getString("name"));
		assertEquals(123, obj.getInt("val"));
		
		JsonNumber o1 = obj.getJsonNumber("val2");
		assertEquals(new BigDecimal("12.3"), o1.bigDecimalValue());
		
		o = transformer.transform(null, null, null, null, "[{ \"name\": \"hello\", \"val\": 123, \"val2\": 12.3}, \"world\"]");
		
		assertEquals(ValueType.ARRAY, o[0].getValueType());
		JsonArray ar = (JsonArray)o[0];
		obj = (JsonObject)ar.getJsonObject(0);
		assertEquals("hello", obj.getString("name"));
		assertEquals(123, obj.getInt("val"));
		
		o1 = obj.getJsonNumber("val2");
		assertEquals(new BigDecimal("12.3"), o1.bigDecimalValue());
		
		assertEquals("world", ar.getJsonString(1).getString());
	}
}
