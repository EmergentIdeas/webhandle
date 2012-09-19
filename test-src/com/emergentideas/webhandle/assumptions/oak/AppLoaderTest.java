package com.emergentideas.webhandle.assumptions.oak;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.catalina.session.StandardSession;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.emergentideas.webhandle.HttpMockUtils;
import com.emergentideas.webhandle.WebAppLocation;
import com.emergentideas.webhandle.output.Respondent;

public class AppLoaderTest {

	@Test
	public void testDynamicClassDirectories() throws Exception {
		AppLoader appLoader = new AppLoader();
		appLoader.load(new File("appLoaderTest.conf"));
		
		WebAppLocation webApp = new WebAppLocation(appLoader.getLocation());
		ClassLoader cl = (ClassLoader)webApp.getServiceByName(AppLoader.CLASS_LOADER_NAME);
		Class c = cl.loadClass("com.emergentideas.webhandle.TestObjNoSource");
		
		assertNotNull(c);
		assertEquals("com.emergentideas.webhandle.TestObjNoSource", c.getName());
	}
	
	@Test
	public void testNotNullAndCommand() throws Exception {
		AppLoader loader = new AppLoader();
		loader.load(new FileInputStream(new File(new File("").getAbsoluteFile(), "WebContent/test2.conf")), new File("WebContent").getAbsoluteFile());
		WebAppLocation webApp = new WebAppLocation(loader.getLocation());
		Respondent handle = (Respondent)webApp.getServiceByName("request-handler");
		
		
		final Map<String, String[]> parms = new HashMap<String, String[]>();
		parms.put("a", new String[] { "hello" });
		parms.put("b", new String[] { "world" });
		parms.put("id", new String[] { "2" });
		
		
		
		final ByteArrayOutputStream realOut = new ByteArrayOutputStream();
		
		HttpServletRequest request = HttpMockUtils.createRequest("/ten", "GET", parms);
		HttpServletResponse response = HttpMockUtils.createResponse(realOut);
		
		handle.respond(null, request, response);
		
		String result = new String(realOut.toByteArray());
		assertTrue(result.contains("<input name=\"a\" value=\"hello\"  type=\"text\" />"));
		assertTrue(result.contains("<textarea name=\"b\">world</textarea>"));
		assertTrue(result.contains("<input name=\"id\" value=\"2\"  type=\"hidden\" />"));
		

	}
}
