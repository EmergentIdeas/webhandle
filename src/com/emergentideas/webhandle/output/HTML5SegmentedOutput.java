package com.emergentideas.webhandle.output;

import java.util.Properties;
import java.util.Set;

import com.emergentideas.logging.Logger;
import com.emergentideas.logging.SystemOutLogger;
import com.emergentideas.utils.StringUtils;

public class HTML5SegmentedOutput extends SegmentedOutput {
	
	Logger logger = SystemOutLogger.get(HTML5SegmentedOutput.class);
	
	public HTML5SegmentedOutput() {
		Properties defaults = new Properties();
		try {
			defaults.load(StringUtils.getStreamFromClassPathLocation("com/emergentideas/webhandle/output/html5OutputDefaults.properties"));
			for(String key : ((Set<String>)(Set)defaults.keySet())) {
				getStream(key).append(defaults.get(key).toString());
			}
		}
		catch(Exception e) {
			logger.error("Could not load html5 defaults.", e);
		}
	}

}