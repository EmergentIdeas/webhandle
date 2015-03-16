package com.emergentideas.webhandle.json;

import org.apache.commons.lang.StringEscapeUtils;

import com.emergentideas.webhandle.output.SegmentedOutput;

@JSONSerializer
public class EnumSerializer implements ObjectSerializer<Enum> {

	public void serialize(Serializer callingSerializer, SegmentedOutput output,
			Enum objToSerialize, String... allowedSerializationProfiles) {
		if(objToSerialize != null) {
			output.getStream("body").append("\"" + StringEscapeUtils.escapeJavaScript(objToSerialize.toString()).replace("\\'", "'") + "\"");
		}
	}
}
