package com.emergentideas.webhandle.files;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;

import com.emergentideas.utils.DateUtils;
import com.emergentideas.utils.StringUtils;
import com.emergentideas.webhandle.AppLocation;
import com.emergentideas.webhandle.CallSpec;
import com.emergentideas.webhandle.WebAppLocation;
import com.emergentideas.webhandle.assumptions.oak.AppLoader;
import com.emergentideas.webhandle.assumptions.oak.HandleCaller;
import com.emergentideas.webhandle.exceptions.CouldNotHandle;
import com.emergentideas.webhandle.handlers.HttpMethod;
import com.emergentideas.webhandle.output.DirectRespondent;
import com.emergentideas.webhandle.output.Show;

import static junit.framework.Assert.*;
import static org.mockito.Mockito.*;

public class StreamableResourcesHandlerTest {

	protected StreamableResourcesHandler handler;
	protected StreamableResourceSource source;
	protected ServletContext servletContext;
	
	@Before
	public void before() throws Exception {
		
		Resource fileResource = new StreamableResource() {
			public String getEtag() {
				return "123";
			}
			public InputStream getContent() {
				return new ByteArrayInputStream("hello".getBytes());
			}
		};
		
		Resource directoryResource = new Directory() {
			public List<Resource> getEntries() {
				return new ArrayList<Resource>();
			}
		};
		
		source = mock(StreamableResourceSource.class);
		when(source.get("file")).thenReturn(fileResource);
		when(source.get("dir")).thenReturn(directoryResource);
		when(source.get("dir/")).thenReturn(directoryResource);
		
		handler = new StreamableResourcesHandler(source);
		
		servletContext = mock(ServletContext.class);
		when(servletContext.getMimeType(anyString())).thenReturn("text/html");
		
	}
	
	@Test
	public void testDirectoryForward() throws Exception {
		Object ret = handler.handle("dir", null, "123", new AppLocation(), mock(HttpServletRequest.class));
		assertTrue(ret instanceof CouldNotHandle);
		handler.setShowDirectoryContents(true);
		ret = handler.handle("dir", null, "123", new AppLocation(), mock(HttpServletRequest.class));
		assertTrue(ret instanceof Show);
	}
	
	@Test
	public void testCacheTimeWithNoCacheDeclaration() throws Exception {
		AppLoader loader = new AppLoader();
		loader.load(StringUtils.getStreamFromClassPathLocation("com/emergentideas/webhandle/files/streamResourcesHandlerTest1.conf"), new File("").getAbsoluteFile());
		
		WebAppLocation loc = new WebAppLocation(loader.getLocation());
		
		HandleCaller caller = (HandleCaller)loc.getServiceByName("request-handler");
		CallSpec[] specs = caller.getHandlerInvestigator().determineHandlers("/file", HttpMethod.GET);
		
		assertEquals(1, specs.length);
		
		StreamableResourcesHandler handler = (StreamableResourcesHandler)specs[0].getFocus();
		handler.setSource(source);
		
		Date now = new Date();
		DirectRespondent resp = (DirectRespondent)handler.handle("file", servletContext, "234", new AppLocation(), mock(HttpServletRequest.class));
		assertTrue(DateUtils.htmlExpiresDateFormat().parse(resp.getHeaders().get("Expires")).before(now));
	}
	
	@Test
	public void testCacheTimeWithOneHourCacheDeclaration() throws Exception {
		AppLoader loader = new AppLoader();
		loader.load(StringUtils.getStreamFromClassPathLocation("com/emergentideas/webhandle/files/streamResourcesHandlerTest2.conf"), new File("").getAbsoluteFile());
		
		WebAppLocation loc = new WebAppLocation(loader.getLocation());
		
		HandleCaller caller = (HandleCaller)loc.getServiceByName("request-handler");
		CallSpec[] specs = caller.getHandlerInvestigator().determineHandlers("/file", HttpMethod.GET);
		
		assertEquals(1, specs.length);
		
		StreamableResourcesHandler handler = (StreamableResourcesHandler)specs[0].getFocus();
		handler.setSource(source);
		
		Date now = new Date();
		DirectRespondent resp = (DirectRespondent)handler.handle("file", servletContext, "234", new AppLocation(), mock(HttpServletRequest.class));
		assertFalse(DateUtils.htmlExpiresDateFormat().parse(resp.getHeaders().get("Expires")).before(now));
	}
	
	@Test
	public void testVirtualResourceCode() throws Exception {
		StreamableResourcesHandler handler = new StreamableResourcesHandler(null);
		assertTrue(handler.isVirtualResource("vrsc/1234/hello"));
		assertFalse(handler.isVirtualResource("vrscs/1234/hello"));
		
		assertEquals("hello", handler.getNonVirtualPath("vrsc/1234/hello"));
		assertEquals("hello", handler.getNonVirtualPath("vrsc/12344/hello"));
		assertNull(handler.getNonVirtualPath("vrsc/1234/"));
		assertNull(handler.getNonVirtualPath("vrsc/1234"));
		assertNull(handler.getNonVirtualPath("vrsc/"));
		
	}

}
