/*
 * hackathon
 *
 * TODO: Project Beschreibung
 *
 * @author Tim Neumann
 * @version 1.0.0
 *
 */
package de.hackathon.hackathon;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import de.hackathon.hackathon.data.Body;
import de.hackathon.hackathon.data.Event;
import de.hackathon.hackathon.data.Group;
import de.hackathon.hackathon.data.ToDo;
import de.hackathon.hackathon.data.User;

/**
 * TODO: Description
 * @author Tim Neumann, Fabian Hutzenlaub, Patrick Muerdter
 */
public class Logic {
		
	public boolean addUser(String name , long key) {
		if(Main.dm.getBodys().get(key) != null) {
			return false;
		} 
		Main.dm.setBody(new User(name , key));
		return true;
	}
	
	public boolean addGroup(String name , ArrayList<User> users) {
		short random = (short) Math.random();
		while(Main.dm.getBodys().get(new Long(random)) != null) {
		random = (short) Math.random();
		}
		for(Map.Entry<Long, Body> body : Main.dm.getBodys().entrySet()) {
			if(body.getValue() instanceof Group) {
				if(((Group) body).getName() == name) {
					return false;
				}
			}
		}
		Main.dm.setBody(new Group(random , users , name));
		return true;
	}
	
	public boolean addEvent(Date date , String location , String name , String description ,int priority) {
		short random = (short) Math.random();
		while(Main.dm.getEvents().get(new Long(random)) == null) {
			random = (short) Math.random();
			}
		for(Map.Entry<Long, Event> event : Main.dm.getEvents().entrySet()) {
				if(event.getValue().getName() == name) {
					return false;
			}
		}
		Main.dm.setEvent( new Event(random , date , location , name , description , priority));
		return true;
	}
	
	public boolean addToDo(Date date , String name , String description ,int priority) {
		short random = (short) Math.random();
		while(Main.dm.getToDos().get(new Long(random)) == null) {
			random = (short) Math.random();
			}
		for(Map.Entry<Long, ToDo> toDo : Main.dm.getToDos().entrySet()) {
				if(toDo.getValue().getName() == name) {
					return false;
			}
		}
		Main.dm.setToDo(new ToDo(random , date , name , description , priority));
		return true;
	}
	
}
	
	
