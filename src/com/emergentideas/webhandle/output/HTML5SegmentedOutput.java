package com.emergentideas.webhandle.output;

import java.io.IOException;
import java.util.Properties;
import java.util.Set;

import com.emergentideas.logging.Logger;
import com.emergentideas.logging.SystemOutLogger;
import com.emergentideas.utils.StringUtils;

public class HTML5SegmentedOutput extends SegmentedOutput {
	
	Logger logger = SystemOutLogger.get(HTML5SegmentedOutput.class);
	
	public HTML5SegmentedOutput() throws IOException {
		this(getDefaultProperties());
	}
	
	public HTML5SegmentedOutput(Properties defaults) {
		setDefaults(defaults);
	}
	
	protected void setDefaults(Properties defaults) {
		for(String key : ((Set<String>)(Set)defaults.keySet())) {
			getStream(key).append(defaults.get(key).toString());
		}
	}
	
	public static Properties getDefaultProperties() throws IOException {
		Properties defaults = new Properties();
		defaults.load(StringUtils.getStreamFromClassPathLocation("com/emergentideas/webhandle/output/html5OutputDefaults.properties"));
		return defaults;
	}


}
