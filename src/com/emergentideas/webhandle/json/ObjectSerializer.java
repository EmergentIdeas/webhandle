package com.emergentideas.webhandle.json;

import com.emergentideas.webhandle.output.SegmentedOutput;

public interface ObjectSerializer<T> {

	/**
	 * Serializes a single object, and potentially its children, given whatever methods
	 * see appropriate. The <code>callingSerializer</code> is passed so that it can delegate
	 * the child objects (as it should) to the serializer that called it
	 * @param callingSerializer
	 * @param output
	 * @param objToSerialize
	 * @param allowedSerializationProfiles
	 */
	public void serialize(Serializer callingSerializer, SegmentedOutput output, 
			T objToSerialize, String ... allowedSerializationProfiles) throws Exception;

}
