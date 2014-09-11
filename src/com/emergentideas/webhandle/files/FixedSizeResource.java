package com.emergentideas.webhandle.files;

/**
 * Represents a resource which has a known current size.
 * @author kolz
 *
 */
public interface FixedSizeResource {
	
	public long getSizeInBytes();
}
