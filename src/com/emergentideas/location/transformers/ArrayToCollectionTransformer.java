package com.emergentideas.location.transformers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.emergentideas.location.exceptions.TransformationException;

public class ArrayToCollectionTransformer {

	public <C, D extends Class<C>, B extends Collection<C>, A extends Class<B>> B transform(A parameterClass, D collectionComponentClass,
			C... source) throws TransformationException {
		if(List.class.isAssignableFrom(parameterClass)) {
			return (B)createAsList(source);
		}
		
		if(Set.class.isAssignableFrom(parameterClass)) {
			Set<C> result = new HashSet<C>();
			for(int i = 0; i < source.length; i++) {
				result.add(source[i]);
			}
			return (B)result;
		}
		
		if(Collection.class.isAssignableFrom(parameterClass)) {
			return (B)createAsList(source);
		}
		
		return null;
	}
	
	protected <C> List createAsList(C... source) {
		List<C> result = new ArrayList<C>();
		for(int i = 0; i < source.length; i++) {
			result.add(source[i]);
		}
		return result;
	}

	
}
