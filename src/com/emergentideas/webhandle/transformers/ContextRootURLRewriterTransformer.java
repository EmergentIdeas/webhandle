package com.emergentideas.webhandle.transformers;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import com.emergentideas.utils.BindingUtils;
import com.emergentideas.webhandle.Location;
import com.emergentideas.webhandle.output.SegmentedOutput;

public class ContextRootURLRewriterTransformer {

	public void transform(SegmentedOutput output, Location location, HttpServletRequest request) {
		StringBuilder sb = output.getStream("body");
		String context = request.getContextPath();
		if(StringUtils.isBlank(context) || "/".equals(context)) {
			return;
		}
		String result = BindingUtils.prefixURLsWithContextRoot(context, null, sb.toString());
		sb.delete(0, sb.length());
		sb.append(result);
	}
}
