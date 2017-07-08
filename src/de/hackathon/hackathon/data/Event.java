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
import java.util.Date;
import java.util.List;

/**
 * TODO: Description
 * @author Henne
 */
public class Event extends Body{
	private static final long serialVersionUID = 4845151409020395848L;
	
	private Date date;
	private String location;
	private String name;
	private String description;
	private int priority;
	
	/**
	 * Get's {@link #date date}
	 * @return  date
	 */
	public Date getDate() {
		return this.date;
	}
	/**
	 * Set's {@link #date date}
	 * @param date  date
	 */
	public void setDate(Date date) {
		this.date = date;
	}
	/**
	 * Get's {@link #location location}
	 * @return  location
	 */
	public String getLocation() {
		return this.location;
	}
	/**
	 * Set's {@link #location location}
	 * @param location  location
	 */
	public void setLocation(String location) {
		this.location = location;
	}
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
	 * Get's {@link #description description}
	 * @return  description
	 */
	public String getDescription() {
		return this.description;
	}
	/**
	 * Set's {@link #description description}
	 * @param description  description
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * Get's {@link #priority priority}
	 * @return  priority
	 */
	public int getPriority() {
		return this.priority;
	}
	/**
	 * Set's {@link #priority priority}
	 * @param priority  priority
	 */
	public void setPriority(int priority) {
		this.priority = priority;
	}
	
	/**
	 *  constructor
	 * @param key key
	 * @param date date 
	 * @param location location
	 * @param name name 
	 * @param description description
	 * @param priority priority
	 */
	public Event(long key , Date date , String location , String name , String description ,int priority) {
		this.setKey(key);
		this.date = date;
		this.location = location;
		this.name = name;
		this.description = description;
		this.priority = priority;
	}
	
}
