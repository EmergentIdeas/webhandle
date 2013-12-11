package com.emergentideas.webhandle.assumptions.oak;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;

/**
 * This class loader will sit in the hierarchy and make it look like the isolated classes are
 * loaded at this level instead of the level where the class resource bytes are actually loaded.
 * This allows virtual hosts to load some "static" classes in info separately from the other 
 * virtual hosts running in the JVM.
 * @author kolz
 *
 */
public class IsolatingClassLoader extends ClassLoader {

	protected Set<String> knownIsolatedClasses = Collections.synchronizedSet(new HashSet<String>());
	protected Set<String> knownNomralClasses = Collections.synchronizedSet(new HashSet<String>());
	protected List<Pattern> isolatedClassPatterns = Collections.synchronizedList(new ArrayList<Pattern>());
	
	public IsolatingClassLoader(ClassLoader parent) {
		super(parent);
	}

	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		name = name.replace('.', '/') + ".class";
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		InputStream is = getParent().getResourceAsStream(name);
		if(is == null) {
			throw new ClassNotFoundException(name);
		}
		try {
			IOUtils.copy(is, baos);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		byte[] b = baos.toByteArray();
		return defineClass(null, b, 0, b.length, null);
	}

	protected boolean doesMatchPattern(String name) {
		for(Pattern pattern : isolatedClassPatterns) {
			if(pattern.matcher(name).matches()) {
				knownIsolatedClasses.add(name);
				return true;
			}
		}
		return false;
	}

	@Override
	protected Class<?> loadClass(String name, boolean resolve)
			throws ClassNotFoundException {
		if(knownIsolatedClasses.contains(name)) {
			return loadTheClass(name, resolve);
		}
		if(knownNomralClasses.contains(name)) {
			return super.loadClass(name, resolve);
		}
		if(doesMatchPattern(name)) {
			return loadTheClass(name, resolve);
		}
		return super.loadClass(name, resolve);
	}

	protected Class<?> loadTheClass(String name, boolean resolve) throws ClassNotFoundException {
		synchronized (getClassLoadingLock(name)) {
			// First, check if the class has already been loaded
			Class c = findLoadedClass(name);
			if (c == null) {
				if (c == null) {
					// If still not found, then invoke findClass in order
					// to find the class.
					c = findClass(name);
				}
			}
			if (resolve) {
				resolveClass(c);
			}
			return c;
		}
	}

	/**
	 * Adds a class to the list of isolated classes. If the class name contains a plus, star, or paren,
	 * the name will be treated as a regex pattern. This will, of course, have some unfortunate runtime
	 * performance effects. However, steps are taken to minimize them.
	 * @param pattern
	 */
	public void addClassOrPattern(String pattern) {
		if(pattern.contains("+") || pattern.contains("*") || pattern.contains("(") || pattern.contains(")")) {
			isolatedClassPatterns.add(Pattern.compile(pattern));
		}
		else {
			knownIsolatedClasses.add(pattern);
		}
	}

	public Set<String> getKnownIsolatedClasses() {
		return knownIsolatedClasses;
	}

	public void setKnownIsolatedClasses(Set<String> knownIsolatedClasses) {
		this.knownIsolatedClasses = knownIsolatedClasses;
	}

	public Set<String> getKnownNomralClasses() {
		return knownNomralClasses;
	}

	public void setKnownNomralClasses(Set<String> knownNomralClasses) {
		this.knownNomralClasses = knownNomralClasses;
	}

	public List<Pattern> getIsolatedClassPatterns() {
		return isolatedClassPatterns;
	}

	public void setIsolatedClassPatterns(List<Pattern> isolatedClassPatterns) {
		this.isolatedClassPatterns = isolatedClassPatterns;
	}
	
	
}
