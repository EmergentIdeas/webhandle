package com.emergentideas.webhandle.json;

import com.emergentideas.webhandle.output.SegmentedOutput;

public interface Serializer {

	/**
	 * Serializes the <code>objToSerialize</code> to the <code>body</code> stream of the
	 * <code>output</code> object using only the <code>allowedSerializationProfiles</code>.
	 * @param output
	 * @param objToSerialize
	 * @param allowedSerializationProfiles The serialization profiles that can be used in serializing 
	 * the <code>objToSerialize</code>. If no profiles are specified, "default" should be used.
	 */
	public void serialize(SegmentedOutput output, Object objToSerialize, String ... allowedSerializationProfiles) throws Exception;
}
