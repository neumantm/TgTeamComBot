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


import java.io.Serializable;
import java.util.Date;


/**
 * TODO: Description
 * @author Henne
 */
public class Event implements Serializable{
	private static final long serialVersionUID = 4845151409020395848L;
	
	private Date date;
	private String location;
	private String name;
	private String description;
	private long key;
	
	/**
	 * Get's {@link #key key}
	 * @return  key
	 */
	public long getKey() {
		return this.key;
	}
	/**
	 * Set's {@link #key key}
	 * @param key  key
	 */
	public void setKey(long key) {
		this.key = key;
	}
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
	 *  constructor
	 * @param key key
	 * @param date date 
	 * @param location location
	 * @param name name 
	 * @param description description
	 */
	public Event(long key , Date date , String location , String name , String description) {
		this.key = key;
		this.date = date;
		this.location = location;
		this.name = name;
		this.description = description;
	}
	
}
