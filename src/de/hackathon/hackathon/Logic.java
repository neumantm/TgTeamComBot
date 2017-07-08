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

import de.hackathon.hackathon.data.Group;
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
		User u = new User(name , key);
		Main.dm.bodys.put(u.getKey(), u);
		return true;
	}
	
	public boolean addGroup(String name , ArrayList<User> users) {
		short random = (short) Math.random();
		while(Main.dm.bodys.get(random) == null) {
		random = (short) Math.random();
		}
		Main.dm.bodys.put((long) random, new Group())
	}
	
	public boolean addEvent() {
		
}
}
