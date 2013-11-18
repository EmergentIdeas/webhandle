package com.emergentideas.webhandle.json;

import org.apache.commons.codec.binary.Base64;

import com.emergentideas.utils.ReflectionUtils;
import com.emergentideas.webhandle.output.SegmentedOutput;

@JSONSerializer({"bytes-as-array", "bytes-as-base64", "default"})
public class ByteArraySerializer implements ObjectSerializer<byte[]> {

	protected ArraySerializer arraySerializer = new ArraySerializer();
	
	public void serialize(Serializer callingSerializer, SegmentedOutput output,
			byte[] objToSerialize, String... allowedSerializationProfiles)
			throws Exception {
		boolean asArray = ReflectionUtils.contains(allowedSerializationProfiles, "bytes-as-array");
		if(asArray) {
			arraySerializer.serialize(callingSerializer, output, objToSerialize, allowedSerializationProfiles);
		}
		else {
			StringBuilder sb = output.getStream("body");
			sb.append('"' + Base64.encodeBase64String(objToSerialize) + '"');
		}
	}

}
