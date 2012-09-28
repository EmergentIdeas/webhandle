package com.emergentideas.utils;

import java.util.Set;
import java.util.regex.Pattern;

import org.junit.Test;
import org.reflections.Configuration;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.Scanner;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.vfs.Vfs.File;

import com.google.common.base.Predicate;
import com.google.common.collect.Multimap;
import com.pureperfect.ferret.ScanFilter;
import com.pureperfect.ferret.WebClasspathScanner;
import com.pureperfect.ferret.vfs.PathElement;

public class ReflectionsTest {
	
	@Test
	public void testReflections() throws Exception {

		Reflections reflections = new Reflections(new ConfigurationBuilder().setScanners(new ResourcesScanner())
				.addClassLoader(Thread.currentThread().getContextClassLoader()));
		for(String s: reflections.getResources(Pattern.compile(".*\\.conf"))) {
			System.out.println(s);
		}
		
		reflections = new Reflections("com", new Scanner() {

			public void setConfiguration(Configuration configuration) {
				int i1234 = 1234;
				i1234++;
				
			}

			public Multimap<String, String> getStore() {
				int i1234 = 1234;
				i1234++;
				return null;
			}

			public void setStore(Multimap<String, String> store) {
				int i1234 = 1234;
				i1234++;
				
			}

			public Scanner filterResultsBy(Predicate<String> filter) {
				int i1234 = 1234;
				i1234++;
				return null;
			}

			public boolean acceptsInput(String file) {
				int i1234 = 1234;
				i1234++;
				return false;
			}

			public void scan(File file) {
				// TODO Auto-generated method stub
				int i1234 = 1234;
				i1234++;
			}

			public boolean acceptResult(String fqn) {
				int i1234 = 1234;
				i1234++;
				return false;
			}
			
		});

	}

}
