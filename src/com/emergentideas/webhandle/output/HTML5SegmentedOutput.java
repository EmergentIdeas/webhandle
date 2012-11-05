package com.emergentideas.webhandle.output;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Properties;
import java.util.Set;
import java.util.TimeZone;

import com.emergentideas.logging.Logger;
import com.emergentideas.logging.SystemOutLogger;
import com.emergentideas.utils.DateUtils;
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
			
			getPropertySet("httpHeader").put("X-UA-Compatible", "IE=edge");
			
			Calendar c = Calendar.getInstance();
			c.add(Calendar.HOUR, -1);
			
			// add default cache control information
			getPropertySet("httpHeader").put("Cache-Control", "public, must-revalidate, max-age=0");
			getPropertySet("httpHeader").put("Content-Type", "text/html");
			getPropertySet("httpHeader").put("Expires", DateUtils.htmlExpiresDateFormat().format(c.getTime()));
		}
		catch(Exception e) {
			logger.error("Could not load html5 defaults.", e);
		}
	}


}
