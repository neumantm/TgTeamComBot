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

import java.util.Date;

/**
 * TODO: Description
 * 
 * @author Tim Neumann, Fabian Hutzenlaub, Patrick Muerdter
 */
public class ToDo {

	private int priority;
	private String name;
	private String description;
	private Date deadline;
	private long key;

	/**
	 * Empty constructor for deserializing only.
	 */
	public ToDo() {

	}

	/**
	 * Creates a new ToDo with specified values.
	 * 
	 * @param p_key
	 *            key
	 * @param p_date
	 *            deadline
	 * @param p_name
	 *            name
	 * @param p_description
	 *            description
	 * @param p_priority
	 *            priority
	 */
	public ToDo(long p_key, Date p_date, String p_name, String p_description, int p_priority) {
		this.key = p_key;
		this.deadline = p_date;
		this.name = p_name;
		this.description = p_description;
		this.priority = p_priority;
	}
	
	/**
	 * @param p_key key
	 * @param p_name name
	 */
	public ToDo(long p_key, String p_name) {
		this.key = p_key;
		this.deadline = new Date(0);
		this.name = p_name;
		this.description = null;
		this.priority = 0;
	}

	/**
	 * Creates a new ToDo with the same values as the orig ToDo
	 * 
	 * @param orig
	 *            The original ToDo to take the values from.
	 */
	public ToDo(ToDo orig) {
		this.key = orig.key;
		this.deadline = (Date) orig.deadline.clone();
		this.name = orig.name;
		this.description = orig.description;
		this.priority = orig.priority;
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
	 * Get's {@link #priority priority}
	 * 
	 * @return priority
	 */
	public int getPriority() {
		return this.priority;
	}

	/**
	 * Set's {@link #priority priority}
	 * 
	 * @param priority
	 *            priority
	 */
	public void setPriority(int priority) {
		this.priority = priority;
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
	 * Get's {@link #deadline deadline}
	 * 
	 * @return deadline
	 */
	public Date getDeadline() {
		return this.deadline;
	}

	/**
	 * Set's {@link #deadline deadline}
	 * 
	 * @param deadline
	 *            deadline
	 */
	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof ToDo)) return false;
		return this.key == ((ToDo) obj).key;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return (int) this.key;
	}
}
