package com.emergentideas.webhandle.handlers;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Test;

public class UrlRequestElementsProcessorTest {

	@Test
	public void testParsingAndMatching() {
		UrlRequestElementsProcessor processor = new UrlRequestElementsProcessor();
		Map<String, String> parameters;
		
		UrlRegexOutput reg = processor.process("/hello");
		
		assertNotNull(reg.matches("/hello"));
		assertNull(reg.matches("/helloo"));
		assertNull(reg.matches("/hell"));
		
		reg = processor.process("/users/{userId}");
		parameters = reg.matches("/users/15");
		assertNotNull(parameters);
		assertEquals("15", parameters.get("userId"));
		
		assertNull(reg.matches("/users/16/asdfasf"));
		assertNull(reg.matches("/users/16/"));
		
		reg = processor.process("/users/{userId}/books/{bookId}");
		parameters = reg.matches("/users/15/books/23");
		assertNotNull(parameters);
		assertEquals("15", parameters.get("userId"));
		assertEquals("23", parameters.get("bookId"));
		
		reg = processor.process("/users/{userId}_{userName}/books/{bookId}");
		parameters = reg.matches("/users/15_dan/books/23");
		assertNotNull(parameters);
		assertEquals("15", parameters.get("userId"));
		assertEquals("23", parameters.get("bookId"));
		assertEquals("dan", parameters.get("userName"));
		
		reg = processor.process("/users/{userId}{userName}/books/{bookId}");
		parameters = reg.matches("/users/15dan/books/23");
		assertNotNull(parameters);
		assertEquals("15da", parameters.get("userId"));
		assertEquals("23", parameters.get("bookId"));
		assertEquals("n", parameters.get("userName"));
		
		reg = processor.process("/users/{userId:\\d+}{userName:\\w+}/books/{bookId}");
		parameters = reg.matches("/users/15dan/books/23");
		assertNotNull(parameters);
		assertEquals("15", parameters.get("userId"));
		assertEquals("23", parameters.get("bookId"));
		assertEquals("dan", parameters.get("userName"));
		
		parameters = reg.matches("/users/a15dan/books/23");
		assertNull(parameters);
		
		reg = processor.process("/file/{filePath:.*}");
		parameters = reg.matches("/file/this/is/my/path");
		assertNotNull(parameters);
		assertEquals("this/is/my/path", parameters.get("filePath"));
		
		
		
	}
}
