/**
 * 
 */
package de.hackathon.hackathon.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 
 * @author Henne
 *
 */
public abstract class Body implements Serializable{
	
	private long key;
	/**
	 * Get's {@link #key key}
	 * @return  key
	 */
	public long getKey() {
		return this.key;
	}
	private static final long serialVersionUID = -8966320631762198755L;
	private ArrayList<Event> events;
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
	
	
}
