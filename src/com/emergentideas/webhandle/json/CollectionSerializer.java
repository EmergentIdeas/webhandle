package com.emergentideas.webhandle.json;

import java.util.Collection;
import java.util.Iterator;

import com.emergentideas.webhandle.output.SegmentedOutput;

@JSONSerializer
public class CollectionSerializer implements ObjectSerializer<Collection> {

	public void serialize(Serializer callingSerializer, SegmentedOutput output,
			Collection objToSerialize, String... allowedSerializationProfiles) throws Exception {
		StringBuilder sb = output.getStream("body");
		sb.append("[");
		
		Iterator<?> it = objToSerialize.iterator();
		while(it.hasNext()) {
			Object next = it.next();
			
			callingSerializer.serialize(output, next, allowedSerializationProfiles);
			
			if(it.hasNext()) {
				sb.append(", ");
			}
		}
		
		sb.append("]");
		
	}

	
}
