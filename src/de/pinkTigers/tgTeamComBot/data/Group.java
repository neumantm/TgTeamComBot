/*
 * hackathon
 *
 * TODO: Project Beschreibung
 *
 * @author Tim Neumann
 * @version 1.0.0
 *
 */
package de.pinkTigers.tgTeamComBot.data;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

/**
 * TODO: Description
 * 
 * @author Henne
 */
public class Group extends Body {
	private static final long serialVersionUID = -4234018617918494805L;

	private HashMap<Long, Body> members = new HashMap<>();

	/**
	 * Default Constructor for Serializing only
	 */
	public Group() {

	}

	/**
	 * Creates a new Group with given values
	 * 
	 * @param p_key
	 *            Key
	 * @param p_name
	 *            Name
	 * @param initalUser
	 *            A initial user.
	 */
	public Group(long p_key, String p_name, Body initalUser) {
		super(p_key, p_name);
		this.members.put(new Long(initalUser.getKey()), initalUser);
	}

	/**
	 * Creates a new Group with the values from a orig group
	 * 
	 * @param orig
	 *            The original group to get the values from.
	 */
	public Group(Group orig) {
		super(orig);
		for (Entry<Long, Body> e : orig.members.entrySet()) {
			if (e.getValue() instanceof User) {
				this.members.put(e.getKey(), new User((User) e.getValue()));
			}
			else if (e.getValue() instanceof Group) {
				this.members.put(e.getKey(), new Group((Group) e.getValue()));
			}
		}
	}

	/**
	 * @return the user
	 */
	@Override
	public Set<User> getUsers() {
		Set<User> ret = new HashSet<>();
		for (Entry<Long, Body> e : this.members.entrySet()) {
			ret.addAll(e.getValue().getUsers());
		}
		return ret;
	}

	/**
	 * Adds a user
	 * 
	 * @param body
	 *            user
	 */
	public void addBody(Body body) {
		this.members.put(new Long(body.getKey()), body);
	}

	/**
	 * Removes a user
	 * 
	 * @param body
	 *            user
	 */
	public void removeBody(Body body) {
		this.members.remove(new Long(body.getKey()));
	}

}
