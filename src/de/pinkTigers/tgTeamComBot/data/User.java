package de.pinkTigers.tgTeamComBot.data;

import java.util.HashSet;
import java.util.Set;

/**
 * TODO: Description
 * 
 * @author Henne
 */
public class User extends Body {
	private static final long serialVersionUID = 1947250923485077010L;

	/**
	 * Default Constructor for Serializing only
	 */
	public User() {

	}

	/**
	 * Creates a new User with given values
	 * 
	 * @param p_key
	 *            Key
	 * @param p_name
	 *            Name
	 */
	public User(long p_key, String p_name) {
		super(p_key, p_name);
	}

	/**
	 * Creates a new User with the values from a orig user
	 * 
	 * @param orig
	 *            The original user to get the values from.
	 */
	public User(User orig) {
		super(orig);
	}

	/**
	 * @see de.pinkTigers.tgTeamComBot.data.Body#getUsers()
	 */
	@Override
	public Set<User> getUsers() {
		Set<User> toReturn = new HashSet<>();
		toReturn.add(this);
		return toReturn;
	}
}
