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

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO: Description
 * @author Tim Neumann, Fabian Hutzenlaub, Patrick Muerdter
 */
public class ToDo extends Body{
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
	private int priority;
	private String description;
	private Date deadline;

}
