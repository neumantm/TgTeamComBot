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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * TODO: Description
 * 
 * @author Henne
 */
public class Group extends Body {
	private static final long serialVersionUID = -4234018617918494805L;

	private ArrayList<Body> members = new ArrayList<>();

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
		this.members.add(initalUser);
	}

	/**
	 * Creates a new Group with the values from a orig group
	 * 
	 * @param orig
	 *            The original group to get the values from.
	 */
	public Group(Group orig) {
		super(orig);
		for (Body b : orig.members) {
			if (b instanceof User) {
				this.members.add(new User((User) b));
			}
			else if (b instanceof Group) {
				this.members.add(new Group((Group) b));
			}
		}
	}

	/**
	 * @return the user
	 */
	@Override
	public Set<User> getUsers() {
		Set<User> ret = new HashSet<>();
		for (Body b : this.members) {
			ret.addAll(b.getUsers());
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
		this.members.add(body);
	}

	/**
	 * Removes a user
	 * 
	 * @param body
	 *            user
	 */
	public void removeBody(Body body) {
		this.members.remove(body);
	}

}
