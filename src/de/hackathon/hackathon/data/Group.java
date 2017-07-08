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
	
	private ArrayList<User> Users;
	private ArrayList<Event> events;
	
	
	/**
	 * Set's {@link #Users users}
	 * @param users  users
	 */
	public void setUsers(ArrayList<User> users) {
		this.Users = users;
	}
	/**
	 * Get's {@link #events events}
	 * @return  events
	 */
	public ArrayList<Event> getEvents() {
		return this.events;
	}
	/**
	 * Set's {@link #events events}
	 * @param events  events
	 */
	public void setEvents(ArrayList<Event> events) {
		this.events = events;
	}
	
	public List<User> getUsers() {
		return this.Users;
	}
}
