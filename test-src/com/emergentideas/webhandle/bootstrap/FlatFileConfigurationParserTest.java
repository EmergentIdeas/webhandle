package com.emergentideas.webhandle.bootstrap;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.emergentideas.utils.StringUtils;

public class FlatFileConfigurationParserTest {

	@Test
	public void testParse() throws Exception {
		FlatFileConfigurationParser parser = new FlatFileConfigurationParser();
		List<ConfigurationAtom> atoms = parser.parse(StringUtils.getStreamFromClassPathLocation("com/emergentideas/webhandle/bootstrap/config1.conf"));
		
		assertEquals(3, atoms.size());
		ConfigurationAtom atom;
		
		atom = atoms.get(0);
		
		assertEquals("lets-get-this-party-started", atom.getType());
		assertEquals("com.emergentideas.webhandle.StandAloneServer", atom.getValue());
		
		atom = atoms.get(1);
		
		assertEquals("directory-template-source", atom.getType());
		assertEquals("templates", atom.getValue());
		
		atom = atoms.get(2);
		
		assertEquals("", atom.getType());
		assertEquals("com.emergentideas.webhandle.bootstrap.Wire", atom.getValue());
		
		
		
	}
}
