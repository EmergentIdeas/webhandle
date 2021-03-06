package com.emergentideas.webhandle.assumptions.oak.interfaces;

import java.util.List;

/**
 * A very simple service to check and set the identity of users.
 * @author kolz
 *
 */
public interface AuthenticationService {

	public static final String LOCAL_AUTHENTICATION_SYSTEM = "local";
	
	/**
	 * Returns a user as known to by the its primary key in the database.  This method will return null if
	 * no user with that id exists.
	 * @param id
	 * @return
	 */
	public User getUserById(String id);
	
	/**
	 * Returns a user fetched using the what is probably their login name.  This method will return null if
	 * no user with that profile name exists.
	 * @param profileName
	 * @return
	 */
	public User getUserByProfileName(String profileName);
	
	
	/**
	 * Returns true if a user with <code>profileName</code> using password <code>password</code>
	 * exists on the system.
	 * @param profileName
	 * @param password
	 * @return
	 */
	public boolean isAuthenticated(String profileName, String password);
	
	/**
	 * Creates an active user with the given profileName and password.  If other users of the same
	 * name exist, an {@link IllegalArgumentException} is thrown.
	 * @param profileName
	 * @param email
	 * @param password
	 * @return
	 */
	public User createUser(String profileName, String email, String password) throws IllegalArgumentException;
	
	/**
	 * Sets a new password for the user if a user exists, otherwise throws an {@link IllegalArgumentException}
	 * @param profileName
	 * @param password
	 * @throws IllegalArgumentException
	 */
	public void setPassword(String profileName, String password) throws IllegalArgumentException;
	
	/**
	 * Sets the email of a profile.  Throws an {@link IllegalArgumentException} if no user with that
	 * profile name exists.
	 * @param profileName
	 * @param email
	 */
	public void setEmail(String profileName, String email) throws IllegalArgumentException;
	
	/**
	 * Sets the authentication system for a user.  The default is {@link LOCAL_AUTHENTICATION_SYSTEM} which means
	 * the password credentials are managed by this system.  Anything else indicates they are a user that 
	 * we first authenticated through Google, Yahoo, etc.
	 * @param profileName
	 * @param authenticationSystem
	 */
	public void setAuthenticationSystem(String profileName, String authenticationSystem);
	
	/**
	 * Returns the authority name which knows how to authenticate this profile.  If we've stored their password, then
	 * the system will be {@link LOCAL_AUTHENTICATION_SYSTEM}.  Otherwise it will be Google, Yahoo, etc.
	 * @param profileName
	 * @return
	 */
	public String getAuthenticationSystem(String profileName);
	
	/**
	 * Sets the full name for a user.  Throws an {@link IllegalArgumentException} if no user with that
	 * profile name exists.
	 * @param profileName
	 * @param fullName
	 * @throws IllegalArgumentException
	 */
	public void setFullName(String profileName, String fullName) throws IllegalArgumentException;
	
	/**
	 * Sets whether the profile is active or deactivated.  Throws an {@link IllegalArgumentException} if no user with that
	 * profile name exists.
	 * @param profileName
	 * @param active
	 */
	public void setActive(String profileName, boolean active) throws IllegalArgumentException;
	
	
	/**
	 * Deletes a user and their password from the system. Throws an {@link IllegalArgumentException} if the user 
	 * does not exist.
	 * @param profileName
	 */
	public void deleteUser(String profileName) throws IllegalArgumentException;
	
	/**
	 * Creates a new group or throws an {@link IllegalArgumentException} if a group by that name exists.
	 * @param groupName
	 */
	public void createGroup(String groupName) throws IllegalArgumentException;
	
	/**
	 * Returns true if a group by that name exists.
	 * @param groupName
	 * @return
	 */
	public boolean doesGroupExist(String groupName) throws IllegalArgumentException;
	
	/**
	 * Deletes a group or throws an {@link IllegalArgumentException} if the group does not exist.
	 * @param groupName
	 */
	public void deleteGroup(String groupName) throws IllegalArgumentException;
	
	/**
	 * Adds a member to a group or throws an {@link IllegalArgumentException} if the group or user does not exist.
	 * @param groupName
	 * @param profileName
	 */
	public void addMember(String groupName, String profileName) throws IllegalArgumentException;
	
	/**
	 * Removes a member from a group or throws an {@link IllegalArgumentException} if the member or group does not exist.
	 * @param groupName
	 * @param profileName
	 */
	public void removeMember(String groupName, String profileName) throws IllegalArgumentException;
	
	/**
	 * Gets the list of all profile names.
	 * @return
	 */
	public List<String> getProfileNames();
	
	/**
	 * Gets the list of all group names.
	 * @return
	 */
	public List<String> getGroupNames();
	
	/**
	 * Returns the list of all profile names that belong to <code>groupName</code>.
	 * @param groupName
	 * @return
	 */
	public List<String> getProfilesInGroup(String groupName);
	
	/**
	 * Returns the list of all managed users.
	 * @return
	 */
	public List<User> getUsers();
}
