package de.hackathon.hackathon.data;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO: Description
 * @author Henne
 */
public class User extends Body{
	private static final long serialVersionUID = 1947250923485077010L;
	
	private String name;
	private List<Group> groups;
	private ArrayList<User> user = new ArrayList<>();
	
	/**
	 * Get's the name
	 * @return  name
	 */
	public String getName() {
		return this.name;
	}


	/**
	 * Set's the name
	 * @param name  name
	 */
	public void setName(String name) {
		this.name = name;
	}


	/**
	 * Get's the groups
	 * @return  groups
	 */
	public List<Group> getGroups() {
		return this.groups;
	}


	/**
	 * Set's the groups
	 * @param groups  groups
	 */
	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}

	
	public List<User> getUser() {
		return this.user;
	}
	
	
	/**
	 * Default Constructor
	 */
	public User(){
	}
	/**
	 * Normal Constructor
	 * @param name name
	 * @param id id
	 */
	public User(String name , long key){
		this.name = name;
		this.setKey(key);
	}

}
