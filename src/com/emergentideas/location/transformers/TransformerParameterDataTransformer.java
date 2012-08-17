package com.emergentideas.location.transformers;

import java.util.Map;

/**
 * Can transform a string which represents properties into a Map of properties.
 * @author kolz
 *
 */
public interface TransformerParameterDataTransformer {
	
	public Map<String, ?> transform(String properties);

}
