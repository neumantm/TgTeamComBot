/*
 * hackathon
 *
 * TODO: Project Beschreibung
 *
 * @author Tim Neumann
 * @version 1.0.0
 *
 */
package de.hackathon.hackathon.data;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO: Description
 * @author Henne
 */
public class Group extends Body{
	private static final long serialVersionUID = -4234018617918494805L;
	
	private String name;
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
	private ArrayList<User> Users;
	
	
	/**
	 * Set's {@link #Users users}
	 * @param users  users
	 */
	public void setUsers(ArrayList<User> users) {
		this.Users = users;
	}
	/**
	 * @return the user
	 */
	public List<User> getUsers() {
		return this.Users;
	}
	
	
}
