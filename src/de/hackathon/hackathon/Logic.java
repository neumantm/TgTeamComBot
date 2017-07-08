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

import de.hackathon.hackathon.data.User;

/**
 * TODO: Description
 * @author Tim Neumann, Fabian Hutzenlaub, Patrick Muerdter
 */
public class Logic {
		
	public boolean addUser(String name , long id) {
		if(Main.dm.bodys.get(id) != null) {
			return false;
		}
		User u = new User(name , id);
		Main.dm.bodys.put(u.getId(), u);
		return true;
	}
	
	public boolean addGroup(String name , ArrayList<User> users) {
		short random = (short) Math.random();
		while(Main.dm.bodys.get(random) == null) {
		random = (short) Math.random();
		}
	}
	
	public boolean addEvent() {
		
}
}
