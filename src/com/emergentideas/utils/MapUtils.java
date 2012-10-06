package com.emergentideas.utils;

import java.util.HashMap;
import java.util.Map;

public class MapUtils {

	public static <E, F> Map<F, E> reverse(Map<E, F> source) {
		Map<F, E> result = new HashMap<F, E>();
		for(Map.Entry<E, F> entry : source.entrySet()) {
			result.put(entry.getValue(), entry.getKey());
		}
		
		return result;
	}
	

}
