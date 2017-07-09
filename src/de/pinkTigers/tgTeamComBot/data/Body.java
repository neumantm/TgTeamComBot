/**
 * 
 */
package de.pinkTigers.tgTeamComBot.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 
 * 
 * @author Henne
 *
 */
public abstract class Body implements Serializable {

	private static final long serialVersionUID = -8966320631762198755L;
	private long key;
	private ArrayList<Event> events = new ArrayList<>();
	private ArrayList<ToDo> toDos = new ArrayList<>();
	private String name;

	/**
	 * Default constructor only used for deserializing
	 */
	public Body() {

	}

	/**
	 * Creates a Body with given values.
	 * 
	 * @param p_key
	 *            Key
	 * @param p_name
	 *            Name
	 */
	public Body(long p_key, String p_name) {
		this.key = p_key;
		this.name = p_name;
	}

	/**
	 * Creates a new Body with the values of a orig body
	 * 
	 * @param orig
	 *            The original body to get the values from.
	 */
	protected Body(Body orig) {
		this.key = orig.key;
		this.name = orig.name;

		for (Event e : orig.events) {
			this.events.add(new Event(e));
		}

		for (ToDo t : orig.toDos) {
			this.toDos.add(new ToDo(t));
		}
	}

	/**
	 * @return A list of all users in this body.
	 */
	public abstract Set<User> getUsers();

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
	 * Get's {@link #events events}
	 * 
	 * @return events
	 */
	public List<Event> getEvents() {
		return this.events;
	}

	/**
	 * Adds to {@link #events events}
	 * 
	 * @param p_event
	 *            event
	 */
	public void addEvent(Event p_event) {
		this.events.add(p_event);
	}

	/**
	 * Remove from {@link #events events}
	 * 
	 * @param p_events
	 *            events
	 */
	public void removeEvent(Event p_events) {
		this.events.remove(new Long(p_events.getKey()));
	}

	/**
	 * Get's {@link #toDos toDos}
	 * 
	 * @return toDos
	 */
	public ArrayList<ToDo> getToDos() {
		return this.toDos;
	}

	/**
	 * Adds one to {@link #toDos toDos}
	 * 
	 * @param p_toDo
	 *            toDos
	 */
	public void addToDo(ToDo p_toDo) {
		this.toDos.add(p_toDo);
	}

	/**
	 * Removes from {@link #toDos toDos}
	 * 
	 * @param p_toDo
	 *            toDos
	 */
	public void removeToDo(ToDo p_toDo) {
		this.toDos.remove(new Long(p_toDo.getKey()));
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Body)) return false;
		return this.key == ((Body) obj).key;
	}

}
