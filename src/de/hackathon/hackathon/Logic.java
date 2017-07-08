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
		if(Main.dm.bodys.get(key) != null) {
			return false;
		} 
		Main.dm.bodys.put(key, new User(name , key));
		return true;
	}
	
	public boolean addGroup(String name , ArrayList<User> users) {
		short random = (short) Math.random();
		while(Main.dm.bodys.get(random) != null) {
		random = (short) Math.random();
		}
		for(Map.Entry<Long, Body> body : Main.dm.bodys.entrySet()) {
			if(body.getValue() instanceof Group) {
				if(((Group) body).getName() == name) {
					return false;
				}
			}
		}
		Main.dm.bodys.put((long) random, new Group(random , users , name));
		return true;
	}
	
	public boolean addEvent(Date date , String location , String name , String description ,int priority) {
		short random = (short) Math.random();
		while(Main.dm.bodys.get(random) == null) {
			random = (short) Math.random();
			}
		for(Map.Entry<Long, Body> body : Main.dm.bodys.entrySet()) {
			if(body.getValue() instanceof Event) {
				if(((Event) body).getName() == name) {
					return false;
				}
			}
		}
		Main.dm.bodys.put((long) random , new Event(random , date , location , name , description , priority));
		return true;
	}
	
	public boolean addToDo(Date date , String name , String description ,int priority) {
		short random = (short) Math.random();
		while(Main.dm.bodys.get(random) == null) {
			random = (short) Math.random();
			}
		for(Map.Entry<Long, Body> body : Main.dm.bodys.entrySet()) {
			if(body.getValue() instanceof ToDo) {
				if(((ToDo) body).getName() == name) {
					return false;
				}
			}
		}
		Main.dm.bodys.put((long) random , new ToDo(random , date , name , description , priority));
		return true;
	}
	
}
	
	
