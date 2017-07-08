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


import java.util.Date;

/**
 * TODO: Description
 * @author Tim Neumann, Fabian Hutzenlaub, Patrick Muerdter
 */
public class ToDo {
	
	private int priority;
	private String name;
	private String description;
	private Date deadline;
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
	 * Get's {@link #deadline deadline}
	 * @return  deadline
	 */
	public Date getDeadline() {
		return this.deadline;
	}
	/**
	 * Set's {@link #deadline deadline}
	 * @param deadline  deadline
	 */
	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}

	
	/** 
	 * Construktor
	 * @param key key
	 * @param date deadline
	 * @param name name
	 * @param description description
	 * @param priority priority
	 */
	public ToDo(long key , Date date , String name , String description ,int priority) {
		this.key = key;
		this.deadline = date;
		this.name = name;
		this.description = description;
		this.priority = priority;
	}

}
