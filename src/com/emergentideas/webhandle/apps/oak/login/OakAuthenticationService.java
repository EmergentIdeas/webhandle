package com.emergentideas.webhandle.apps.oak.login;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.commons.lang.StringUtils;

import com.emergentideas.logging.Logger;
import com.emergentideas.logging.SystemOutLogger;
import com.emergentideas.webhandle.Type;
import com.emergentideas.webhandle.Wire;
import com.emergentideas.webhandle.assumptions.oak.interfaces.AuthenticationService;
import com.emergentideas.webhandle.assumptions.oak.interfaces.User;

@Type("com.emergentideas.webhandle.assumptions.oak.interfaces.AuthenticationService")
public class OakAuthenticationService implements AuthenticationService {

	protected EntityManager entityManager;
	protected String applicationSalt = "vlkNj2!@#34kv.jQ@P#%$4lksdjf132246@^#N$^@4lks@^$%^djlkj4W3lkf";
	protected int numberOfIterations = 10322;
	
	protected Logger log = SystemOutLogger.get(OakAuthenticationService.class);
	
	public User getUserById(String id) {
		return getUserByProfileName(id);
	}

	public User getUserByProfileName(String profileName) {
		List<?> l = entityManager.createQuery("from OakUser where profileName = ?1").setParameter(1, profileName).getResultList();
		if(l.size() == 0) {
			return null;
		}
		return (User)l.get(0);
	}

	public boolean isAuthenticated(String profileName, String password) {
		User user = getUserByProfileName(profileName);
		
		if(user == null) {
			return false;
		}
		
		if(user.isActive() == false) {
			return false;
		}
		
		if(LOCAL_AUTHENTICATION_SYSTEM.equals(user.getAuthenticationSystem()) == false) {
			return false;
		}
		
		OakPassword pass = getPassword(profileName);
		if(pass == null) {
			return false;
		}
		
		try {
			byte[] presentedHash = hash(profileName, password);
			byte[] storedHash = pass.getHashedPassword();
			if(match(storedHash, presentedHash)) {
				return true;
			}
		}
		catch(Exception e) {
			log.error("Could not has the password for user: " + profileName, e);
			return false;
		}
		return false;
	}
	
	protected OakPassword getPassword(String profileName) {
		return entityManager.find(OakPassword.class, profileName);
	}

	public User createUser(String profileName, String email, String password)
			throws IllegalArgumentException {
		if(getUserByProfileName(profileName) != null) {
			throw new IllegalArgumentException();
		}
		
		OakUser user = new OakUser();
		
		user.setProfileName(profileName);
		user.setFullName(profileName);
		user.setEmail(email);
		user.setActive(true);
		
		entityManager.persist(user);
		
		OakPassword pass = new OakPassword();
		pass.setUserId(profileName);
		try {
			pass.setHashedPassword(hash(profileName, password));
		}
		catch(Exception e) {
			throw new RuntimeException(e);
		}
		entityManager.persist(pass);
		
		return user;
	}
	
	/**
	 * Returns true if the byte streams match, false if they do not.
	 * @param one
	 * @param two
	 * @return
	 */
	protected boolean match(byte[] one, byte[] two) {
		if(one == null && two == null) {
			return true;
		}
		
		if(one == null || two == null) {
			return false;
		}
		
		if(one.length != two.length) {
			return false;
		}
		
		
		for(int i = 0; i < one.length; i++) {
			if(one[i] != two[i]) {
				return false;
			}
		}
		
		return true;
	}

	public void setPassword(String profileName, String password)
			throws IllegalArgumentException {
		getCheckedUserByProfileName(profileName);
		OakPassword pass = getPassword(profileName);
		if(pass == null) {
			pass = new OakPassword();
			pass.setUserId(profileName);
		}
		
		try {
			pass.setHashedPassword(hash(profileName, password));
		}
		catch(Exception e) {
			log.error("Could not hash password for user: " + profileName, e);
			throw new RuntimeException(e);
		}
		entityManager.persist(pass);
	}

	
	public void setEmail(String profileName, String email) {
		OakUser user = getCheckedUserByProfileName(profileName);
		
		user.setEmail(email);
	}

	public void setFullName(String profileName, String fullName) {
		OakUser user = getCheckedUserByProfileName(profileName);
		
		user.setFullName(fullName);
	}

	public void setActive(String profileName, boolean active) {
		OakUser user = getCheckedUserByProfileName(profileName);
		
		user.setActive(active);
	}
	
	protected OakUser getCheckedUserByProfileName(String profileName) {
		OakUser user = (OakUser)getUserByProfileName(profileName);
		if(user == null) {
			throw new IllegalArgumentException("User " + profileName + " does not exist.");
		}
		
		return user;
	}

	public void deleteUser(String profileName) {
		User user = getUserByProfileName(profileName);
		OakPassword pass = getPassword(profileName);

		entityManager.remove(user);
		entityManager.remove(pass);
	}

	
	public byte[] hash(String profileName, String password) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		MessageDigest digest = MessageDigest.getInstance("SHA-512");
		digest.reset();
		byte[] input = digest.digest((applicationSalt + profileName + password).getBytes("UTF-8"));
		for (int i = 0; i < numberOfIterations; i++) {
			digest.reset();
			input = digest.digest(input);
		}
		return input;
	}
	
	

	public void createGroup(String groupName) {
		if(doesGroupExist(groupName)) {
			throw new IllegalArgumentException("Group " + groupName + " already exists.");
		}
		
		OakGroup group = new OakGroup();
		group.setGroupName(groupName);
		entityManager.persist(group);
	}

	public boolean doesGroupExist(String groupName) {
		return getGroup(groupName) != null;
	}
	
	/**
	 * Gets a group by group name if it exists.  Otherwise null.
	 * @param groupName
	 * @return
	 */
	protected OakGroup getGroup(String groupName) {
		List<OakGroup> result = entityManager.createQuery("from OakGroup g where groupName = ?1").setParameter(1, groupName).getResultList();
		if(result.size() > 0) {
			return result.get(0);
		}
		return null;
	}

	public void deleteGroup(String groupName) {
		OakGroup group = getGroup(groupName);
		if(group == null) {
			throw new IllegalArgumentException("Group " + groupName + " does not exist.");
		}
		
		// this is an awful thing to do.
		// you'd think there's a better jpa way to do this
		// such is the life of a jpa novice
		for(String profileName : getProfilesInGroup(groupName)) {
			removeMember(groupName, profileName);
		}
		
		entityManager.remove(group);
	}

	public void addMember(String groupName, String profileName) {
		OakGroup group = getGroup(groupName);
		OakUser user = getCheckedUserByProfileName(profileName);
		if(group == null) {
			throw new IllegalArgumentException("Profile " + profileName + " could not be added to group " + groupName + " because the group does not exist.");
		}
		
		user.getGroups().add(group);
	}

	public void removeMember(String groupName, String profileName) {
		OakGroup group = getGroup(groupName);
		OakUser user = getCheckedUserByProfileName(profileName);
		if(group == null) {
			throw new IllegalArgumentException("Profile " + profileName + " could not be removed from the group " + groupName + " because the group does not exist.");
		}
		
		user.getGroups().remove(group);
	}
	
	

	public List<String> getProfileNames() {
		List<String> names = new ArrayList<String>();
		for(User user : getUsers()) {
			names.add(user.getProfileName());
		}
		return names;
	}
	
	public List<User> getUsers() {
		return (List<User>)entityManager.createQuery("Select u from OakUser u").getResultList();
	}


	public List<String> getGroupNames() {
		List<String> names = new ArrayList<String>();
		for(OakGroup g : (List<OakGroup>)entityManager.createQuery("Select g from OakGroup g").getResultList()) {
			names.add(g.getGroupName());
		}
		return names;
	}

	public List<String> getProfilesInGroup(String groupName) {
		if(getGroup(groupName) == null) {
			throw new IllegalArgumentException("Could not get profiles in group " + groupName + " because that group does not exist.");
		}
		
		List<String> names = new ArrayList<String>();
		for(User user : (List<User>)entityManager.createQuery("Select u from OakUser u inner join u.groups g where g.groupName = ?1").setParameter(1,groupName).getResultList()) {
			names.add(user.getProfileName());
		}
		return names;
	}
	
	

	public void setAuthenticationSystem(String profileName,
			String authenticationSystem) {
		OakUser user = getCheckedUserByProfileName(profileName);
		user.setAuthenticationSystem(authenticationSystem);
		
	}

	public String getAuthenticationSystem(String profileName) {
		String system = getCheckedUserByProfileName(profileName).getAuthenticationSystem();
		if(StringUtils.isBlank(system)) {
			return LOCAL_AUTHENTICATION_SYSTEM;
		}
		return system;
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}
	
	@Wire
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public void setApplicationSalt(String applicationSalt) {
		this.applicationSalt = applicationSalt;
	}

	public void setNumberOfIterations(int numberOfIterations) {
		this.numberOfIterations = numberOfIterations;
	}

}
