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

import java.io.Serializable;
import java.util.Date;

/**
 * TODO: Description
 * 
 * @author Henne
 */
public class Event implements Serializable {
	private static final long serialVersionUID = 4845151409020395848L;

	private Date date;
	private String location;
	private String name;
	private String description;
	private long key;

	/**
	 * Default constructor for deserializing only
	 */
	public Event() {

	}

	/**
	 * Creates a new Event with the given values
	 * 
	 * @param p_key
	 *            key
	 * @param p_date
	 *            date
	 * @param p_location
	 *            location
	 * @param p_name
	 *            name
	 * @param p_description
	 *            description
	 */
	public Event(long p_key, Date p_date, String p_location, String p_name, String p_description) {
		this.key = p_key;
		this.date = p_date;
		this.location = p_location;
		this.name = p_name;
		this.description = p_description;
	}

	/**
	 * Creates a new Event with the same values as the orig Event
	 * 
	 * @param orig
	 *            The original event to take the values from.
	 */
	public Event(Event orig) {
		this.key = orig.key;
		this.date = (Date) orig.date.clone();
		this.location = orig.location;
		this.name = orig.name;
		this.description = orig.description;
	}

	/**
	 * Get's {@link #key key}
	 * 
	 * @return key
	 */
	public long getKey() {
		return this.key;
	}

	/**
	 * Set's {@link #key key}
	 * 
	 * @param key
	 *            key
	 */
	public void setKey(long key) {
		this.key = key;
	}

	/**
	 * Get's {@link #date date}
	 * 
	 * @return date
	 */
	public Date getDate() {
		return this.date;
	}

	/**
	 * Set's {@link #date date}
	 * 
	 * @param date
	 *            date
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * Get's {@link #location location}
	 * 
	 * @return location
	 */
	public String getLocation() {
		return this.location;
	}

	/**
	 * Set's {@link #location location}
	 * 
	 * @param location
	 *            location
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * Get's {@link #name name}
	 * 
	 * @return name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Set's {@link #name name}
	 * 
	 * @param name
	 *            name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Get's {@link #description description}
	 * 
	 * @return description
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * Set's {@link #description description}
	 * 
	 * @param description
	 *            description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Event)) return false;
		return this.key == ((Event) obj).key;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return (int) this.key;
	}

}
