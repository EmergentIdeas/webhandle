package com.emergentideas.webhandle.files;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CompositeStreamableResourceSource implements
		StreamableResourceSource {

	protected List<StreamableResourceSource> sources = Collections.synchronizedList(new ArrayList<StreamableResourceSource>());
	
	/**
	 * Adds a new source.  New sources will be checked before existing sources.
	 * @param source
	 */
	public void addSource(StreamableResourceSource source) {
		sources.add(0, source);
	}
	
	public StreamableResource get(String location) {
		for(StreamableResourceSource source : sources) {
			StreamableResource resource = source.get(location);
			if(resource != null) {
				return resource;
			}
		}
		
		return null;
	}

}
