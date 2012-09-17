package com.emergentideas.webhandle.bootstrap;

import static junit.framework.Assert.*;

import java.util.List;

import org.junit.Test;

import com.emergentideas.utils.StringUtils;
import com.emergentideas.webhandle.AppLocation;
import com.emergentideas.webhandle.WebAppLocation;
import com.emergentideas.webhandle.handlers.HandleAnnotationHandlerInvestigator;
import com.emergentideas.webhandle.handlers.HandlerInvestigator;

public class BasicLoaderTest {

	@Test
	public void testFindDelegateLoader() throws Exception {
		
		
		
		
		
	}
	
	@Test
	public void testIncludedFiles() throws Exception {
		BasicLoader loader = new BasicLoader();
		AppLocation loc = new AppLocation();
		
		loader.load(loc, new FlatFileConfigurationParser().parse(StringUtils.getStreamFromClassPathLocation("com/emergentideas/webhandle/bootstrap/config2.conf")));
		
		
		WebAppLocation webApp = new WebAppLocation(loc);
		assertNotNull(webApp.getServiceByName("hello"));
		assertNull(webApp.getServiceByName("there"));
		
		assertNotNull(webApp.getServiceByType("there"));
		assertNotNull(webApp.getServiceByType("world"));
		assertNull(webApp.getServiceByType("hello"));
		
	}
	
	@Test
	public void testReatomize() throws Exception {
		BasicLoader loader = new BasicLoader();
		AppLocation loc = new AppLocation();
		
		List<ConfigurationAtom> atoms = new FlatFileConfigurationParser().parse(StringUtils.getStreamFromClassPathLocation("com/emergentideas/webhandle/bootstrap/config3.conf")); 
		loader.load(loc, atoms);
		
		ConfigurationAtom atom = atoms.get(atoms.size() - 2);
		assertTrue(atom instanceof FocusAndPropertiesAtom);
		FocusAndPropertiesAtom at = (FocusAndPropertiesAtom)atom;
		assertEquals(1, at.getProperties().size());
		assertEquals("hello", at.getProperties().get("a"));
		assertEquals("com.emergentideas.webhandle.TestObj2", at.getFocus());
		
		atom = atoms.get(atoms.size() - 1);
		assertTrue(atom instanceof FocusAndPropertiesAtom);
		at = (FocusAndPropertiesAtom)atom;
		assertEquals(1, at.getProperties().size());
		assertEquals("world", at.getProperties().get("b"));
		assertEquals("com.emergentideas.webhandle.TestObj2", at.getFocus());
		
		atom = atoms.get(atoms.size() - 3);
		assertFalse(atom instanceof FocusAndPropertiesAtom);
		
	}
	
	
}
