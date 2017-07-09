/*
 * hackathon
 *
 * A telegram Bot that supports Groups, ToDos and events.
 *
 * @author Tim Neumann, Fabian Hutzenlaub, Patrick Muerdter
 * 
 * @copyright Copyright (c) Tim Neumann, Fabian Hutzenlaub, Patrick Muerdter
 * @license Licensed under CC BY SA 4.0
 * 
 * @version 1.0.0
 *
 */
package de.pinkTigers.tgTeamComBot;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import de.pinkTigers.tgTeamComBot.data.Body;
import de.pinkTigers.tgTeamComBot.data.Event;
import de.pinkTigers.tgTeamComBot.data.Group;
import de.pinkTigers.tgTeamComBot.data.ToDo;
import de.pinkTigers.tgTeamComBot.data.User;
import de.pinkTigers.tgTeamComBot.scheduler.OneTimeEvent;
import de.pinkTigers.tgTeamComBot.scheduler.RegularEvent;
import de.pinkTigers.tgTeamComBot.scheduler.Scheduler;
import de.pinkTigers.tgTeamComBot.scheduler.SchedulerEvent;
import de.tim.lib.Log;

/**
 * Data Manager. It has all the permanent data and is responsible for filesystem
 * io.
 * 
 * @author Tim Neumann, Fabian Hutzenlaub, Patrick Muerdter
 */
public class DataManager {

	private HashMap<Long, Body> bodys = new HashMap<>();
	private HashMap<Long, Event> events = new HashMap<>();
	private HashMap<Long, ToDo> toDos = new HashMap<>();
	private ArrayList<SchedulerEvent> schedulerEvents = new ArrayList<>();

	private File dataFolder;

	/**
	 * Creates a new data Manager which uses the specified folder to store it's
	 * data.
	 * 
	 * @param p_dataFolder
	 *            The folder for the data.
	 */
	public DataManager(File p_dataFolder) {
		if (!p_dataFolder.exists()) {
			Main.mainLog.log("DataManager is requested to open non existent folder:" + p_dataFolder.getAbsolutePath() + " Creating it.", Log.WARN);
			p_dataFolder.mkdirs();
		}

		if (!p_dataFolder.isDirectory()) {

			try {
				throw new IOException("DataManager is requested to open a file but should be a folder:" + p_dataFolder.getAbsolutePath());
			} catch (IOException e) {
				Main.mainLog.logException(e, Log.ERROR, true);
			}
		}

		this.dataFolder = p_dataFolder;

		loadBodys();
		loadEvents();
		loadToDos();
		loadSchedulerEvents();
		Scheduler.startTimers();
	}

	@SuppressWarnings("resource") //Eclipse warning bug. There should not be a warning.
	private boolean loadBodys() {
		this.bodys = new HashMap<>();
		try (ObjectInputStream oIS = new ObjectInputStream(new FileInputStream(new File(this.dataFolder, "bodys.dat")))) {
			while (true) {
				Object tmpO = oIS.readObject();
				if (!(tmpO instanceof Body)) {
					oIS.close();
					throw new ClassCastException("WTF! There is a incompatible Object in here.");
				}
				Body tmpB = (Body) tmpO;
				this.bodys.put(new Long(tmpB.getKey()), tmpB);
			}
		} catch (EOFException e) {
			//Ignore
			return true;
		} catch (FileNotFoundException e) {
			Main.mainLog.log("Data file missing. Assuming empty list", Log.WARN);
			return true;
		} catch (IOException e) {
			Main.mainLog.logException(e, Log.ERROR, true);
			return false;
		} catch (ClassNotFoundException e) {
			Main.mainLog.logException(e, Log.ERROR, true);
			return false;
		} catch (ClassCastException e) {
			Main.mainLog.logException(e, Log.ERROR, true);
			return false;
		}
	}

	@SuppressWarnings("resource") //Eclipse warning bug. There should not be a warning.
	private boolean loadEvents() {
		this.events = new HashMap<>();
		try (ObjectInputStream oIS = new ObjectInputStream(new FileInputStream(new File(this.dataFolder, "events.dat")))) {
			while (true) {
				Object tmpO = oIS.readObject();
				if (!(tmpO instanceof Event)) {
					oIS.close();
					throw new ClassCastException("WTF! There is a incompatible Object in here.");
				}
				Event tmpE = (Event) tmpO;
				this.events.put(new Long(tmpE.getKey()), tmpE);
			}
		} catch (EOFException e) {
			//Ignore
			return true;
		} catch (FileNotFoundException e) {
			Main.mainLog.log("Data file missing. Assuming empty list", Log.WARN);
			return true;
		} catch (IOException e) {
			Main.mainLog.logException(e, Log.ERROR, false);
			return false;
		} catch (ClassNotFoundException e) {
			Main.mainLog.logException(e, Log.ERROR, false);
			return false;
		} catch (ClassCastException e) {
			Main.mainLog.logException(e, Log.ERROR, false);
			return false;
		}
	}

	@SuppressWarnings("resource") //Eclipse warning bug. There should not be a warning.
	private boolean loadToDos() {
		this.toDos = new HashMap<>();
		try (ObjectInputStream oIS = new ObjectInputStream(new FileInputStream(new File(this.dataFolder, "todos.dat")))) {
			while (true) {
				Object tmpO = oIS.readObject();
				if (!(tmpO instanceof ToDo)) {
					oIS.close();
					throw new ClassCastException("WTF! There is a incompatible Object in here.");
				}
				ToDo tmpT = (ToDo) tmpO;
				this.toDos.put(new Long(tmpT.getKey()), tmpT);
			}
		} catch (EOFException e) {
			//Ignore
			return true;
		} catch (FileNotFoundException e) {
			Main.mainLog.log("Data file missing. Assuming empty list", Log.WARN);
			return true;
		} catch (IOException e) {
			Main.mainLog.logException(e, Log.ERROR, true);
			return false;
		} catch (ClassNotFoundException e) {
			Main.mainLog.logException(e, Log.ERROR, true);
			return false;
		} catch (ClassCastException e) {
			Main.mainLog.logException(e, Log.ERROR, true);
			return false;
		}
	}

	@SuppressWarnings("resource") //Eclipse warning bug. There should not be a warning.
	private boolean loadSchedulerEvents() {
		this.schedulerEvents = new ArrayList<>();
		try (ObjectInputStream oIS = new ObjectInputStream(new FileInputStream(new File(this.dataFolder, "schedulerEvents.dat")))) {
			while (true) {
				Object tmpO = oIS.readObject();
				if (!(tmpO instanceof SchedulerEvent)) {
					oIS.close();
					throw new ClassCastException("WTF! There is a incompatible Object in here.");
				}
				SchedulerEvent tmpT = (SchedulerEvent) tmpO;
				this.schedulerEvents.add(tmpT);
			}
		} catch (EOFException e) {
			//Ignore
			return true;
		} catch (FileNotFoundException e) {
			Main.mainLog.log("Data file missing. Assuming empty list", Log.WARN);
			return true;
		} catch (IOException e) {
			Main.mainLog.logException(e, Log.ERROR, true);
			return false;
		} catch (ClassNotFoundException e) {
			Main.mainLog.logException(e, Log.ERROR, true);
			return false;
		} catch (ClassCastException e) {
			Main.mainLog.logException(e, Log.ERROR, true);
			return false;
		}
	}

	private boolean saveBodys() {
		try (ObjectOutputStream oUS = new ObjectOutputStream(new FileOutputStream(new File(this.dataFolder, "bodys.dat")))) {
			for (Body obj : this.bodys.values()) {
				oUS.writeObject(obj);
			}
			return true;
		} catch (FileNotFoundException e) {
			Main.mainLog.logException(e, Log.WARN, false);
			return false;
		} catch (IOException e) {
			Main.mainLog.logException(e, Log.WARN, false);
			return false;
		}
	}

	private boolean saveEvents() {
		try (ObjectOutputStream oUS = new ObjectOutputStream(new FileOutputStream(new File(this.dataFolder, "events.dat")))) {
			for (Event obj : this.events.values()) {
				oUS.writeObject(obj);
			}
			return true;
		} catch (FileNotFoundException e) {
			Main.mainLog.logException(e, Log.WARN, false);
			return false;
		} catch (IOException e) {
			Main.mainLog.logException(e, Log.WARN, false);
			return false;
		}
	}

	private boolean saveToDos() {
		try (ObjectOutputStream oUS = new ObjectOutputStream(new FileOutputStream(new File(this.dataFolder, "todos.dat")))) {
			for (ToDo obj : this.toDos.values()) {
				oUS.writeObject(obj);
			}
			return true;
		} catch (FileNotFoundException e) {
			Main.mainLog.logException(e, Log.WARN, false);
			return false;
		} catch (IOException e) {
			Main.mainLog.logException(e, Log.WARN, false);
			return false;
		}
	}

	private boolean saveSchedulerEvents() {
		try (ObjectOutputStream oUS = new ObjectOutputStream(new FileOutputStream(new File(this.dataFolder, "schedulerEvents.dat")))) {
			for (SchedulerEvent obj : this.schedulerEvents) {
				oUS.writeObject(obj);
			}
			return true;
		} catch (FileNotFoundException e) {
			Main.mainLog.logException(e, Log.WARN, false);
			return false;
		} catch (IOException e) {
			Main.mainLog.logException(e, Log.WARN, false);
			return false;
		}
	}

	/**
	 * Get's a copy of {@link #bodys bodys}
	 * 
	 * @return bodys
	 */
	public HashMap<Long, Body> getBodys() {
		HashMap<Long, Body> ret = new HashMap<>();

		for (Entry<Long, Body> e : this.bodys.entrySet()) {
			if (e.getValue() instanceof User) {
				ret.put(e.getKey(), new User((User) e.getValue()));
			}
			else if (e.getValue() instanceof Group) {
				ret.put(e.getKey(), new Group((Group) e.getValue()));
			}
		}

		return ret;
	}

	/**
	 * Set's an object of{@link #bodys bodys}
	 * 
	 * @param body
	 *            body
	 * @return Whether it worked
	 */
	public boolean setBody(Body body) {
		Body tmp = null;
		if (body instanceof User) {
			tmp = new User((User) body);
		}
		else if (body instanceof Group) {
			tmp = new Group((Group) body);
		}
		if (tmp == null) return false;
		this.bodys.put(new Long(body.getKey()), tmp);
		return saveBodys();
	}

	/**
	 * Remove's an object of{@link #bodys bodys}
	 * 
	 * @param id
	 *            the id of the body to remove
	 * @return Whether it worked
	 */
	public boolean removeBody(Long id) {
		this.bodys.remove(id);
		return saveBodys();
	}

	/**
	 * Returns a User from the body list
	 * 
	 * @param id
	 *            The id of the entry to get
	 * @return The User with that ID or null.
	 */
	public User getUser(Long id) {
		if (!(this.bodys.get(id) instanceof User)) return null;
		return new User((User) this.bodys.get(id));
	}

	/**
	 * Returns a Group from the body list
	 * 
	 * @param id
	 *            The id of the entry to get
	 * @return The Group with that ID or null.
	 */
	public Group getGroup(Long id) {
		if (!(this.bodys.get(id) instanceof Group)) return null;
		return new Group((Group) this.bodys.get(id));
	}

	/**
	 * Get's a copy of {@link #events events}
	 * 
	 * @return events
	 */
	public HashMap<Long, Event> getEvents() {
		HashMap<Long, Event> ret = new HashMap<>();

		for (Entry<Long, Event> e : this.events.entrySet()) {
			ret.put(e.getKey(), new Event(e.getValue()));
		}

		return ret;
	}

	/**
	 * Set's an object of{@link #events events}
	 * 
	 * @param event
	 *            event
	 * @return Whether it worked
	 */
	public boolean setEvent(Event event) {
		this.events.put(new Long(event.getKey()), new Event(event));
		return saveEvents();
	}

	/**
	 * Remove's an object of{@link #events events}
	 * 
	 * @param id
	 *            the id of the event to remove
	 * @return Whether it worked
	 */
	public boolean removeEvent(Long id) {
		this.events.remove(id);
		return saveEvents();
	}

	/**
	 * Get's a copy of {@link #toDos toDos}
	 * 
	 * @return toDos
	 */
	public HashMap<Long, ToDo> getToDos() {
		HashMap<Long, ToDo> ret = new HashMap<>();

		for (Entry<Long, ToDo> e : this.toDos.entrySet()) {
			ret.put(e.getKey(), new ToDo(e.getValue()));
		}

		return ret;
	}

	/**
	 * Set's an object of {@link #toDos toDos}
	 * 
	 * @param toDo
	 *            toDo
	 * @return Whether it worked
	 */
	public boolean setToDo(ToDo toDo) {
		this.toDos.put(new Long(toDo.getKey()), new ToDo(toDo));
		return saveToDos();
	}

	/**
	 * Remove's an object of{@link #toDos toDos}
	 * 
	 * @param id
	 *            the id of the toDo to remove
	 * @return Whether it worked
	 */
	public boolean removeToDo(Long id) {
		this.toDos.remove(id);
		return saveToDos();
	}

	/**
	 * Get's a copy of {@link #schedulerEvents schedulerEvents}
	 * 
	 * @return schedulerEvents
	 */
	public ArrayList<SchedulerEvent> getSchedulerEvents() {

		ArrayList<SchedulerEvent> ret = new ArrayList<>();

		for (SchedulerEvent e : this.schedulerEvents) {
			if (e instanceof OneTimeEvent) {
				ret.add(new OneTimeEvent((OneTimeEvent) e));
			}
			else if (e instanceof RegularEvent) {
				ret.add(new RegularEvent((RegularEvent) e));
			}
		}

		return ret;
	}

	/**
	 * Add's an object to {@link #schedulerEvents schedulerEvents}
	 * 
	 * @param schedulerEvent
	 *            schedulerEvents
	 * @return Whether it worked
	 */
	public boolean addSchedulerEvent(SchedulerEvent schedulerEvent) {
		SchedulerEvent tmp = null;
		if (schedulerEvent instanceof OneTimeEvent) {
			tmp = new OneTimeEvent((OneTimeEvent) schedulerEvent);
		}
		else if (schedulerEvent instanceof RegularEvent) {
			tmp = new RegularEvent((RegularEvent) schedulerEvent);
		}
		if (tmp == null) return false;
		this.schedulerEvents.add(tmp);
		return saveSchedulerEvents();
	}

	/**
	 * Remove's an object of {@link #schedulerEvents schedulerEvents}
	 * 
	 * @param event
	 *            the event to remove
	 * @return Whether it worked
	 */
	public boolean removeSchedulerEvent(SchedulerEvent event) {
		this.schedulerEvents.remove(event);
		return saveSchedulerEvents();
	}

	/**
	 * Remove's all object of {@link #schedulerEvents schedulerEvents} where the
	 * chat_id is p_chat_id
	 * 
	 * @param p_chat_id
	 *            the chat /user id for which to remove all scheduler events
	 * @return Whether it worked
	 */
	public boolean removeSchedulerEvent(Long p_chat_id) {
		ArrayList<SchedulerEvent> toRemove = new ArrayList<>();
		for (SchedulerEvent ev : this.schedulerEvents) {
			if (ev.getChatId() == p_chat_id) {
				toRemove.add(ev);
			}
		}

		for (SchedulerEvent ev : toRemove) {
			this.removeSchedulerEvent(ev);
		}

		return saveSchedulerEvents();
	}
}
