package com.emergentideas.webhandle.json;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;

import com.emergentideas.webhandle.output.SegmentedOutput;

@JSONSerializer
public class ArraySerializer implements ObjectSerializer<Object> {

	public void serialize(Serializer callingSerializer, SegmentedOutput output,
			Object objToSerialize, String... allowedSerializationProfiles) throws Exception {
		
		if(objToSerialize.getClass().isArray() == false) {
			return;
		}
		
		StringBuilder sb = output.getStream("body");
		sb.append("[");
		
		int length = Array.getLength(objToSerialize);
		
		for(int i = 0; i < length; i++) {
			Object next = Array.get(objToSerialize, i);
			
			callingSerializer.serialize(output, next, allowedSerializationProfiles);
			
			if(i < length - 1) {
				sb.append(", ");
			}
		}
		
		sb.append("]");
		
	}

	
}
