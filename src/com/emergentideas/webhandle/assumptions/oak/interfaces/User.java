package com.emergentideas.webhandle.assumptions.oak.interfaces;

import java.util.Collection;

/**
 * Defines one way to look at user information which is useful for authorization and
 * authentication.
 * @author kolz
 *
 */
public interface User {
	
	/**
	 * Like <code>dkolz</code> or <code>dan@emergentideas.com</code>, a short name which is used to 
	 * identify the user for purposes of sign in. 
	 * @return
	 */
	public String getProfileName();
	
	/**
	 * The name of the person like <code>Dan Kolz</code>.  Basic implementations of this interface
	 * may to return the same as for <code>getProfileName()</code>.
	 * @return
	 */
	public String getFullName();
	
	/**
	 * Returns the group profile names of which this user is a member.
	 * @return
	 */
	public Collection<String> getGroupNames();
	
	
	/**
	 * Often an int (the synthetic key in the database), this method returns the way in which the system/database
	 * identifies this user.
	 * @return
	 */
	public String getId();
	
	/**
	 * The user's email address.  Normally a piece of personal information, but is so important to doing an account
	 * reset that its included here
	 * @return
	 */
	public String getEmail();

	/**
	 * Returns true if this user's account is active and can be used.  An account can be disabled so that it can't be
	 * used for login but need not be deleted.
	 * @return
	 */
	public boolean isActive();


}
