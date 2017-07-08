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

import java.util.ArrayList;
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
		Main.dm.setBody(new User(name, key));
		return true;
	}

	/**
	 * @param name
	 *            name of the Group
	 * @param users
	 *            users to add
	 * @return success
	 */
	public static boolean addGroup(String name, ArrayList<User> users) {
		short random = (short) Main.randomInt(0, Short.MAX_VALUE);
		while (Main.dm.getBodys().get(new Long(random)) != null) {
			random = (short) Math.random();
		}
		for (Map.Entry<Long, Body> body : Main.dm.getBodys().entrySet()) {
			if (body.getValue() instanceof Group) {
				if (((Group) body).getName() == name) return false;
			}
		}
		Main.dm.setBody(new Group(random, users, name));
		return true;
	}

	/**
	 * @param date
	 *            date of the Event
	 * @param location
	 *            location of the Event
	 * @param name
	 *            name of the Event
	 * @param description
	 *            description of the Event
	 * @return success
	 */
	public static boolean addEvent(Date date, String location, String name, String description) {
		short random = (short) Main.randomInt(0, Short.MAX_VALUE);
		while (Main.dm.getEvents().get(new Long(random)) == null) {
			random = (short) Math.random();
		}
		for (Map.Entry<Long, Event> event : Main.dm.getEvents().entrySet()) {
			if (event.getValue().getName() == name) return false;
		}
		Main.dm.setEvent(new Event(random, date, location, name, description));
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
		while (Main.dm.getToDos().get(new Long(random)) == null) {
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
	public static boolean addUserToGroup(User u, long id) {
		Group p;
		if (Main.dm.getBodys().get(new Long(id)) != null) {
			p = (Group) Main.dm.getBodys().get(new Long(id));
			p.setUsers(u);
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
			b.setEvent(e);
			return true;
		}
		return false;
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
			b.setToDos(t);
			return true;
		}
		return false;
	}

}