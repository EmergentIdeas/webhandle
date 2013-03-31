package com.emergentideas.webhandle.files;

/**
 * Provides a way to access disk or net resources using a simple path.
 * @author kolz
 *
 */
public interface StreamableResourceSource {

	/**
	 * Returns a StreamableResource if one can be found at the location or null otherwise.
	 * @param location
	 * @return
	 */
	public Resource get(String location);
}
