package com.emergentideas.webhandle.bootstrap;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.emergentideas.utils.StringUtils;

/**
 * Parses a flat file configuration where each line is in the format:
 * <pre>
 * object_type->object_value
 * </pre>
 * 
 * Any lines starting with # will be ignored.
 * 
 * @author kolz
 *
 */
public class FlatFileConfigurationParser {
	
	protected static final String separator = "->";

	public List<ConfigurationAtom> parse(InputStream input) throws IOException {
		String[] lines = StringUtils.splitIntoLines(StringUtils.readStream(input));
		
		List<ConfigurationAtom> result = new ArrayList<ConfigurationAtom>();
		
		for(String line : lines) {
			if(org.apache.commons.lang.StringUtils.isBlank(line)) {
				continue;
			}
			
			if(line.startsWith("#")) {
				continue;
			}
			
			ConfigurationAtomBase atom = new ConfigurationAtomBase();
			result.add(atom);
			
			int sepLocation = line.indexOf(separator);
			if(sepLocation > -1) {
				atom.setType(line.substring(0, sepLocation));
				atom.setValue(line.substring(sepLocation + separator.length()));
			}
			else {
				atom.setType("");
				atom.setValue(line);
			}
		}
		
		return result;
	}
}
