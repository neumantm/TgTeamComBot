/*
 * hackathon
 *
 * TODO: Project Beschreibung
 *
 * @author Tim Neumann
 * @version 1.0.0
 *
 */
package de.pinkTigers.hackathon.data;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO: Description
 * @author Henne
 */
public class Group extends Body{
	private static final long serialVersionUID = -4234018617918494805L;
	
	private String name;
	private ArrayList<User> Users;
	/**
	 * Get's {@link #name name}
	 * @return  name
	 */
	public String getName() {
		return this.name;
	}
	/**
	 * Set's {@link #name name}
	 * @param name  name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	
	/**
	 * Set's {@link #Users users}
	 * @param users  users
	 */
	public void setUsers(ArrayList<User> users) {
		this.Users = users;
	}
	
	/**
	 * @param user user
	 */
	public void setUsers(User user) {
		this.Users.add(user);
	}
	/**
	 * @return the user
	 */
	public List<User> getUsers() {
		return this.Users;
	}
	
	/**
	 * @param key key
	 * @param users users
	 * @param name name
	 */
	public Group(long key , ArrayList<User> users , String name) {
		this.setKey(key);
		this.Users = users;
		this.name = name;
	}
}
