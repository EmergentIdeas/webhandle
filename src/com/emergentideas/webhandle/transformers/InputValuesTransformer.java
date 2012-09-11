package com.emergentideas.webhandle.transformers;

import com.emergentideas.utils.BindingUtils;
import com.emergentideas.webhandle.Location;
import com.emergentideas.webhandle.output.SegmentedOutput;

/**
 * Given a location, sets the values of html input elements to be the values available in the
 * location.
 * @author kolz
 *
 */
public class InputValuesTransformer {

	public void transform(SegmentedOutput output, Location location) {
		StringBuilder sb = output.getStream("body");
		String result = BindingUtils.addValuesToAllElementTypes(location, sb.toString());
		sb.delete(0, sb.length());
		sb.append(result);
	}

}
