package com.emergentideas.webhandle.transformers;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.emergentideas.webhandle.Constants;
import com.emergentideas.webhandle.ParameterMarshal;
import com.emergentideas.webhandle.TestObj;
import com.emergentideas.webhandle.composites.db.DbIdToObjectTransformer;
import com.emergentideas.webhandle.configurations.WebParameterMarsahalConfiguration;
import com.emergentideas.webhandle.sources.MapValueSource;
import com.emergentideas.webhandle.transformers.CommandValueTransformer;

import static org.junit.Assert.*;

public class CommandValueTransformerTest {

	@Test
	public void testTransformer() throws Exception {
		CommandValueTransformer trans = new CommandValueTransformer();
		ParameterMarshal pm = new ParameterMarshal(new WebParameterMarsahalConfiguration());
		
		pm.getConfiguration().getTransformers().put(Constants.DB_TO_OBJECT_TRANSFORMER_NAME_DEFAULT, new DbIdToObjectTransformer() {

			@Override
			protected Object loadObject(String id, Class finalParameterClass) {
				if(id != null) {
					TestObj obj = new TestObj();
					obj.setId(id);
					return obj;
				}
				return null;
			}
			
		});
		
		Map<String, String> data = new HashMap<String, String>();
		data.put("a", "one");
		data.put("child2", "15");
		MapValueSource source = new MapValueSource(data);
		
		pm.addSource("data", source);
		
		TestObj obj = new TestObj(null, "three");
		
		trans.transform(pm.getContext(), new HashMap<String, String>(), TestObj.class, "something", obj);
		
		assertEquals("one", obj.getA());
		assertEquals("three", obj.getB());
		
		TestObj child2 = obj.getChild2();
		assertNotNull(child2);
		assertEquals("15", child2.getId());
		
		
	}
}
