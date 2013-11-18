package com.emergentideas.webhandle.json;

import java.util.Iterator;
import java.util.Map;

import com.emergentideas.webhandle.output.SegmentedOutput;

@JSONSerializer
public class MapSerializer implements ObjectSerializer<Map> {

	public void serialize(Serializer callingSerializer, SegmentedOutput output,
			Map objToSerialize, String... allowedSerializationProfiles) throws Exception {
		StringBuilder sb = output.getStream("body");
		sb.append("{");
		
		Iterator<?> it = objToSerialize.keySet().iterator();
		while(it.hasNext()) {
			Object key = it.next();
			
			sb.append("\"" + key.toString() + "\": ");
			
			Object next = objToSerialize.get(key);
			callingSerializer.serialize(output, next, allowedSerializationProfiles);
			
			if(it.hasNext()) {
				sb.append(", ");
			}
		}
		
		sb.append("}");
		
	}

	
}
