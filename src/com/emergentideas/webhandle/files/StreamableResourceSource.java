package com.emergentideas.webhandle.files;

public interface StreamableResourceSource {

	/**
	 * Returns a StreamableResource if one can be found at the location or null otherwise.
	 * @param location
	 * @return
	 */
	public StreamableResource get(String location);
}
