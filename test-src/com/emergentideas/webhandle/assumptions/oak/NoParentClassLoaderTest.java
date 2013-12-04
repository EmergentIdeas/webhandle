package com.emergentideas.webhandle.assumptions.oak;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import org.junit.Test;




import com.emergentideas.utils.ReflectionUtils;
import com.emergentideas.webhandle.AppLocation;

import static org.junit.Assert.*;

public class NoParentClassLoaderTest {
	
	protected String specialClass = "com.emergentideas.webhandle.assumptions.oak.StaticDataHolder";
	protected String specialClass2 = "com.emergentideas.webhandle.assumptions.oak.LoadedRunnable";
	protected String notSpecialClass = "com.emergentideas.webhandle.CallSpec";

	@SuppressWarnings("unused")
	@Test
	public void testNoParent() throws Exception {
		ClassLoader n1 = new NoParentLoader(Thread.currentThread().getContextClassLoader());
		ClassLoader n2 = new NoParentLoader(Thread.currentThread().getContextClassLoader());
		
		assertTrue(ReflectionUtils.getClassForName(specialClass) == StaticDataHolder.class);
		assertTrue(ReflectionUtils.getClassForName(specialClass) != n1.loadClass(specialClass));
		assertTrue(n1.loadClass(specialClass) != n2.loadClass(specialClass));
		assertTrue(n1.loadClass(specialClass) == n1.loadClass(specialClass));
		assertTrue(n1.loadClass(notSpecialClass) == n2.loadClass(notSpecialClass));
		
		
		ClassLoader testCaseLoader = Thread.currentThread().getContextClassLoader();

		Runnable r1 = (Runnable)ReflectionUtils.getClassForName(LoadedRunnable.class.getName()).newInstance();
		r1.run();
		Thread.sleep(40);
		Runnable r2 = (Runnable)ReflectionUtils.getClassForName(LoadedRunnable.class.getName()).newInstance();
		r2.run();
		
		Thread.sleep(40);
		Thread.currentThread().setContextClassLoader(n1);
		
		Runnable r3 = (Runnable)ReflectionUtils.getClassForName(LoadedRunnable.class.getName()).newInstance();
		r3.run();
		Thread.sleep(40);
		Runnable r4 = (Runnable)ReflectionUtils.getClassForName(LoadedRunnable.class.getName()).newInstance();
		r4.run();
		
		boolean b = r1 == r2;
		
		
		
	}
	
	
	class NoParentLoader extends ClassLoader {
		public NoParentLoader(final ClassLoader parent) throws Exception {
			super(parent);
		}
		
		

		@Override
		protected Class<?> findClass(String name) throws ClassNotFoundException {
			name = name.replace('.', '/') + ".class";
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			InputStream is = getParent().getResourceAsStream(name);
			try {
				IOUtils.copy(is, baos);
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			byte[] b = baos.toByteArray();
			return defineClass(null, b, 0, b.length, null);
		}

		@Override
		protected Class<?> loadClass(String name, boolean resolve)
				throws ClassNotFoundException {
			if(name.equals(specialClass) == false && name.equals(specialClass2) == false) {
				return super.loadClass(name, resolve);
			}
	        synchronized (getClassLoadingLock(name)) {
	            // First, check if the class has already been loaded
	            Class c = findLoadedClass(name);
	            if (c == null) {
	                long t0 = System.nanoTime();

	                if (c == null) {
	                    // If still not found, then invoke findClass in order
	                    // to find the class.
	                    long t1 = System.nanoTime();
	                    c = findClass(name);

	                }
	            }
	            if (resolve) {
	                resolveClass(c);
	            }
	            return c;
	        }

		}
		
	}
}
