package com.emergentideas.utils;

import static junit.framework.Assert.*;

import javax.servlet.http.HttpServletRequest;

import org.junit.Test;
import org.mockito.Mockito;

import com.emergentideas.webhandle.AppLocation;
import com.emergentideas.webhandle.output.SegmentedOutput;
import com.emergentideas.webhandle.transformers.ContextRootURLRewriterTransformer;

public class BindingUtilsTest {
	
	@Test
	public void testPrefix() throws Exception {
		SegmentedOutput seg = new SegmentedOutput();
		String html = StringUtils.getStringFromClassPathLocation("com/emergentideas/utils/bindingUtilsTest1.html");
		seg.getStream("body").append(html);
		ContextRootURLRewriterTransformer transformer = new ContextRootURLRewriterTransformer();
		
		HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		Mockito.when(request.getContextPath()).thenReturn("/context");
		
		
		transformer.transform(seg, new AppLocation(), request);
		String out = seg.getStream("body").toString();
		
		for(String must : mustFindStrings) {
			assertTrue(out.contains(must));
		}
	}
	
	protected String[] mustFindStrings = new String[] {
	"action=\"/context/login/user/25\"", 
	"href=\"\"", 
	"href=\"#\"", 
	"href=\"#rel\"", 
	"href=\"/context/login/now/25\"", 
	"href=\"//localhost/login/now/25\"", 
	"href=\"http://localhost/login/now/25\"", 
	"href=\"https://localhost/login/now/25\"", 
	"href=\"/context/otherurl\"",
	"href=\"login/now/25\"", 
	"<img src=\"\"", 
	"<img src=\"#\"", 
	"<img src=\"#rel\"", 
	"<img src=\"/context/login/now/25\"", 
	"<img src=\"//localhost/login/now/25\"", 
	"<img src=\"http://localhost/login/now/25\"", 
	"<img src=\"https://localhost/login/now/25\"", 
	"<img src=\"login/now/25\"", 
	"<img src=\"/context/otherurl\"",
	"action=\"login/user/25\"", 
	"action=\"/context/otherurl\"",
	"action=\"//localhost/login/user/25\"", 
	"action=\"http://localhost/login/user/25\"", 
	"action=\"https://localhost/login/user/25\"" 
	};
}
