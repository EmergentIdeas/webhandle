package com.emergentideas.webhandle.apps.oak.login;

import static junit.framework.Assert.*;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.emergentideas.utils.StringUtils;
import com.emergentideas.webhandle.WebAppLocation;
import com.emergentideas.webhandle.assumptions.oak.AppLoader;
import com.emergentideas.webhandle.assumptions.oak.interfaces.AuthenticationService;
import com.emergentideas.webhandle.assumptions.oak.interfaces.User;

public class OakAuthenticationServiceTest {

	protected WebAppLocation webApp;
	protected EntityManager manager;
	protected AuthenticationService auth;
	
	public OakAuthenticationServiceTest() {
		AppLoader loader = new AppLoader();
		try {
			loader.load(StringUtils.getStreamFromClassPathLocation("com/emergentideas/webhandle/apps/oak/login/oakAuthenticationServiceTest.conf"), new File("").getAbsoluteFile());
	
			webApp = new WebAppLocation(loader.getLocation());
			manager = webApp.getServiceByType(EntityManager.class);
			auth = (OakAuthenticationService)webApp.getServiceByType(AuthenticationService.class);
		}
		catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@Before
	public void setup() throws Exception {
		manager.getTransaction().begin();
		
	}
	
	@Test
	public void testCreateUser() throws Exception {
		String profileName = "kolz";
		String password = "password";
		String notThePassword = "notThePassword";
		String password2 = "another password";
		
		assertNull(auth.getUserByProfileName(profileName));
		
		User user = auth.createUser(profileName, "d@e.com", password);
		assertEquals(profileName, user.getProfileName());
		assertEquals("d@e.com", user.getEmail());
		
		try {
			auth.createUser(profileName, "d@e.com", password);
			fail();
		} catch(IllegalArgumentException e) {}
		
		user = auth.getUserById(profileName);
		assertEquals(profileName, user.getProfileName());
		
		user = auth.getUserByProfileName(profileName);
		assertEquals(profileName, user.getProfileName());

		assertTrue(auth.isAuthenticated(profileName, password));
		assertFalse(auth.isAuthenticated(profileName, notThePassword));
		
		auth.setPassword(profileName, password2);
		
		assertFalse(auth.isAuthenticated(profileName, password));
		assertFalse(auth.isAuthenticated(profileName, notThePassword));
		assertTrue(auth.isAuthenticated(profileName, password2));
		
		auth.setActive(profileName, false);
		assertFalse(auth.isAuthenticated(profileName, password2));
		
		auth.setActive(profileName, true);
		assertTrue(auth.isAuthenticated(profileName, password2));
		
		auth.setEmail(profileName, "a@e.com");
		
		user = auth.getUserByProfileName(profileName);
		assertEquals("a@e.com", user.getEmail());
		
		
		auth.deleteUser(profileName);
		assertFalse(auth.isAuthenticated(profileName, password2));
		
		
		assertNull(auth.getUserByProfileName(profileName));
		
		
		try {
			auth.setPassword(profileName, password2);
			fail();
		}
		catch(IllegalArgumentException e) {
		}
		
		try {
			auth.setEmail(profileName, "d@e.com");
			fail();
		}
		catch(IllegalArgumentException e) {
		}
		
		
	}
	
	
	@Test
	public void testGroups() throws Exception {
		
		String groupName = "group1";
		String profileName = "kolz";
		String password = "password";
		
		assertFalse(auth.doesGroupExist(groupName));
		auth.createGroup(groupName);
		assertTrue(auth.doesGroupExist(groupName));
		
		try {
			auth.createGroup(groupName);
			fail();
		} catch(IllegalArgumentException e) {}
		
		auth.createUser(profileName, profileName, password);
		
		assertEquals(0, auth.getUserByProfileName(profileName).getGroupNames().size());
		
		auth.addMember(groupName, profileName);
		
		Collection<String> groupNames = auth.getUserByProfileName(profileName).getGroupNames();
		assertEquals(1, groupNames.size());
		assertTrue(groupNames.contains(groupName));
		
		auth.createUser(profileName + "2", profileName, password);
		auth.addMember(groupName, profileName + "2");

		try {
			auth.addMember("alsdkfjeialsjf", profileName + "2");
			fail();
		} catch(IllegalArgumentException e) {}

		try {
			auth.addMember(groupName, profileName + "asfasf");
			fail();
		} catch(IllegalArgumentException e) {}
		
		manager.flush();
		
		List<String> profileNames = auth.getProfilesInGroup(groupName);
		assertEquals(2, profileNames.size());
		assertTrue(profileNames.contains(profileName));
		assertTrue(profileNames.contains(profileName + "2"));
		
		try {
			auth.getProfilesInGroup("laksjfdlaskfj");
			fail();
		} catch(IllegalArgumentException e) {}

		
		try {
			auth.getProfilesInGroup("aldskjfasf");
			fail();
		} catch(IllegalArgumentException e) {}
		
		
		groupNames = auth.getGroupNames();
		assertEquals(1, groupNames.size());
		assertTrue(groupNames.contains(groupName));
		
		profileNames = auth.getProfileNames();
		assertEquals(2, profileNames.size());
		assertTrue(profileNames.contains(profileName));
		assertTrue(profileNames.contains(profileName + "2"));
		
		auth.removeMember(groupName, profileName);
		profileNames = auth.getProfilesInGroup(groupName);
		assertEquals(1, profileNames.size());
		assertFalse(profileNames.contains(profileName));
		assertTrue(profileNames.contains(profileName + "2"));
		
		manager.flush();
		
		try {
			auth.removeMember("alsdfkja", profileName + "2");
			fail();
		} catch(IllegalArgumentException e) {}
		
		
		auth.deleteGroup(groupName);
		manager.flush();
		
		assertFalse(auth.doesGroupExist(groupName));
		try {
			auth.deleteGroup(groupName);
			fail();
		} catch(IllegalArgumentException e) {}
		
	}
	
	@Test
	public void testAuthSystem() throws Exception {
		auth.createUser("userName", "a@a.com", null);
		auth.setAuthenticationSystem("userName", "Google");
		assertEquals("Google", auth.getAuthenticationSystem("userName"));
		assertFalse(auth.isAuthenticated("userName", null));
	}
	
	@After
	public void after() throws Exception {
		manager.flush();
		manager.getTransaction().rollback();
		manager.close();
	}
}
