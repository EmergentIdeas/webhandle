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
		IsolatingClassLoader n1 = new IsolatingClassLoader(Thread.currentThread().getContextClassLoader());
		n1.addClassOrPattern(specialClass);
		n1.addClassOrPattern(specialClass2 + ".*");
		
		IsolatingClassLoader n2 = new IsolatingClassLoader(Thread.currentThread().getContextClassLoader());
		n2.addClassOrPattern(specialClass);
		n2.addClassOrPattern(specialClass2);
		
		assertTrue(ReflectionUtils.getClassForName(specialClass) == StaticDataHolder.class);
		assertTrue(ReflectionUtils.getClassForName(specialClass) != n1.loadClass(specialClass));
		assertTrue(n1.loadClass(specialClass) != n2.loadClass(specialClass));
		assertTrue(n1.loadClass(specialClass) == n1.loadClass(specialClass));
		assertTrue(n1.loadClass(notSpecialClass) == n2.loadClass(notSpecialClass));
		assertTrue(n1.loadClass(specialClass2) != n2.loadClass(specialClass2));
		
		
		ClassLoader testCaseLoader = Thread.currentThread().getContextClassLoader();

		Runnable r1 = (Runnable)ReflectionUtils.getClassForName(LoadedRunnable.class.getName()).newInstance();
		r1.run();
		Thread.sleep(40);
		Runnable r2 = (Runnable)ReflectionUtils.getClassForName(LoadedRunnable.class.getName()).newInstance();
		r2.run();
		
		assertEquals(r1.toString(), r2.toString());
		
		Thread.sleep(40);
		Thread.currentThread().setContextClassLoader(n1);
		
		Runnable r3 = (Runnable)ReflectionUtils.getClassForName(LoadedRunnable.class.getName()).newInstance();
		r3.run();
		Thread.sleep(40);
		Runnable r4 = (Runnable)ReflectionUtils.getClassForName(LoadedRunnable.class.getName()).newInstance();
		r4.run();
		
		assertEquals(r3.toString(), r4.toString());
		
	}
}
