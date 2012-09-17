package com.emergentideas.webhandle.bootstrap;

import java.nio.charset.Charset;

import org.apache.commons.lang.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

import com.emergentideas.logging.Logger;
import com.emergentideas.logging.SystemOutLogger;

/**
 * Takes a focus with parameters definition like: 
 * <pre>
 * 		com.emergentideas.webhandle.Class1?attr1=one&attr2=two
 * </pre>
 * and correctly populates the focus (com.emergentideas.webhandle.Class1) and properties
 * <pre>
 * 	attr1	=	one
 * 	attr2	=	two
 * </pre>
 * in a {@link FocusAndPropertiesAtom}.
 * @author kolz
 *
 */
public class URLFocusAndPropertiesAtomizer implements Atomizer {

	protected Charset charset;
	protected Logger log = SystemOutLogger.get(URLFocusAndPropertiesAtomizer.class);
	
	public FocusAndPropertiesAtom atomize(String type, String value) {
		FocusAndPropertiesConfigurationAtom atom = new FocusAndPropertiesConfigurationAtom();
		atom.setType(type);
		atom.setValue(value);
		
		int questionIndex = value.indexOf('?');
		if(questionIndex >= 0) {
			atom.setFocus(value.substring(0, questionIndex));
			String parms = value.substring(questionIndex + 1);
			for(NameValuePair nvp : URLEncodedUtils.parse(parms, getCharset())) {
				atom.getProperties().put(nvp.getName(), nvp.getValue());
			}
		}
		else {
			atom.setFocus(value);
		}
		
		return atom;
	}
	
	/**
	 * Returns the cached charset or creates a new one.
	 * @return
	 */
	protected Charset getCharset() {
		if(charset == null) {
			try {
				charset = Charset.forName("UTF-8");
			}
			catch(Exception e) {
				log.error("Could not load UTF-8 char set", e);
				charset = Charset.defaultCharset();
			}
		}
		
		return charset;
	}

}
