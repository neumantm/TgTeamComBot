/*
 * hackathon
 *
 * TODO: Project Beschreibung
 *
 * @author Tim Neumann
 * @version 1.0.0
 *
 */
package de.pinkTigers.tgTeamComBot;

import java.util.Date;
import java.util.Map;

import de.pinkTigers.tgTeamComBot.data.Body;
import de.pinkTigers.tgTeamComBot.data.Event;
import de.pinkTigers.tgTeamComBot.data.Group;
import de.pinkTigers.tgTeamComBot.data.ToDo;
import de.pinkTigers.tgTeamComBot.data.User;

/**
 * TODO: Description
 * 
 * @author Tim Neumann, Fabian Hutzenlaub, Patrick Muerdter
 */
public class Logic {

	/**
	 * @param name
	 *            username
	 * @param key
	 *            user ID
	 * @return success
	 */
	public static boolean addUser(String name, long key) {
		if (Main.dm.getBodys().get(new Long(key)) != null) return false;
		Main.dm.setBody(new User(key, name));
		return true;
	}

	/**
	 * @param name
	 *            name of the Group
	 * @param initialMember
	 *            initial membets
	 * @return success
	 */
	public static boolean addGroup(String name, Body initialMember) {
		short random = (short) Main.randomInt(0, Short.MAX_VALUE);
		while (Main.dm.getBodys().get(new Long(random)) != null) {
			random = (short) Math.random();
		}
		for (Map.Entry<Long, Body> body : Main.dm.getBodys().entrySet()) {
			if (body.getValue() instanceof Group) {
				if (((Group) body.getValue()).getName() == name) return false;
			}
		}
		Main.dm.setBody(new Group(random, name, initialMember));
		return true;
	}

	/**
	 * @param name
	 *            name of the Event
	 * @return success
	 */
	public static Event createEvent(String name) {
		short random = (short) Main.randomInt(0, Short.MAX_VALUE);
		while (Main.dm.getEvents().get(new Long(random)) != null) {
			random = (short) Math.random();
		}
		return new Event(random, name);
	}

	/**
	 * @param event
	 *            event
	 * @return success
	 */
	public static boolean addEvent(Event event) {
		Main.dm.setEvent(event);
		return true;
	}
	
	/**
	 * @param todo ToDo
	 * @return success
	 */
	public static boolean addToDo(ToDo todo) {
		Main.dm.setToDo(todo);
		return true;
	}
	/**
	 * @param date
	 *            date of ToDo
	 * @param name
	 *            name of ToDo
	 * @param description
	 *            description of ToDo
	 * @param priority
	 *            priority of ToDo
	 * @return success
	 */
	public static boolean addToDo(Date date, String name, String description, int priority) {
		short random = (short) Main.randomInt(0, Short.MAX_VALUE);
		while (Main.dm.getToDos().get(new Long(random)) != null) {
			random = (short) Math.random();
		}
		for (Map.Entry<Long, ToDo> toDo : Main.dm.getToDos().entrySet()) {
			if (toDo.getValue().getName() == name) return false;
		}
		Main.dm.setToDo(new ToDo(random, date, name, description, priority));
		return true;
	}

	/**
	 * @param u
	 *            user to add
	 * @param id
	 *            group ID
	 * @return success
	 */
	public static boolean addUserToGroup(Body u, long id) {
		Group p;
		if (Main.dm.getBodys().get(new Long(id)) != null) {
			p = Main.dm.getGroup(new Long(id));
			p.addBody(u);
			Main.dm.setBody(p);
			return true;
		}
		return false;
	}

	/**
	 * @param b
	 *            body
	 * @param id
	 *            event ID
	 * @return success
	 */
	public static boolean addEventToBody(Body b, long id) {
		Event e;
		if (Main.dm.getEvents().get(new Long(id)) != null) {
			e = Main.dm.getEvents().get(new Long(id));
			b.addEvent(e);
			Main.dm.setBody(b);
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @param body body
	 * @param id event id
	 */
	public static void removeEventfromBody(Body body , long id) {
		body.removeEvent(Main.dm.getEvents().get(id));
		Main.dm.setBody(body);
	}

	/**
	 * @param b
	 *            body
	 * @param id
	 *            TODO ID
	 * @return success
	 */
	public static boolean addToDoToBody(Body b, long id) {
		ToDo t;
		if (Main.dm.getToDos().get(new Long(id)) != null) {
			t = Main.dm.getToDos().get(new Long(id));
			b.addToDo(t);
			return true;
		}
		return false;
	}

	/**
	 * Removes a user
	 * 
	 * @param bodyId
	 *            The id of the body to remove
	 * @return success
	 */
	public static boolean removeBody(Long bodyId) { //TODO
		return Main.dm.removeBody(bodyId);
	}

	/**
	 * Sets a body
	 * 
	 * @param body
	 *            The body to set
	 * @return success
	 */
	public static boolean setBody(Body body) {
		return Main.dm.setBody(body);
	}

	/**
	 * removes a body from a group
	 * 
	 * @param body
	 *            body to remove
	 * @param id
	 *            id of the group
	 * @return success
	 */
	public static boolean removeBodyFromGroup(Body body, long id) {
		Group temp = Main.dm.getGroup(new Long(id));
		temp.removeBody(body);
		Main.dm.setBody(temp);
		return true;
	}

	/**
	 * @param message
	 * @return
	 */
	public static ToDo createToDo(String message) {
		short random = (short) Main.randomInt(0, Short.MAX_VALUE);
		while (Main.dm.getEvents().get(new Long(random)) != null) {
			random = (short) Math.random();
		}
		return new ToDo(random, message);
	}

	/**
	 * @param group
	 * @param key
	 */
	public static void removeToDofromBody(Body group, Long key) {
		group.removeToDo(Main.dm.getToDos().get(key));
		Main.dm.setBody(group);
	}


}