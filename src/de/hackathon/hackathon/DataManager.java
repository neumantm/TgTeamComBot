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
package de.hackathon.hackathon;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import de.hackathon.hackathon.data.Body;
import de.hackathon.hackathon.data.Event;
import de.hackathon.hackathon.data.ToDo;
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
			Main.mainLog.log("DataManager is requested to open non existent folder:" + p_dataFolder.getAbsolutePath() + "Creating it.", Log.WARN);
			p_dataFolder.mkdirs();
		}

		if (!p_dataFolder.isDirectory()) {
			Main.mainLog.log("DataManager is requested to open a file but should be a folder:" + p_dataFolder.getAbsolutePath(), Log.WARN);
			Main.closeData();
			return;
		}

		this.dataFolder = p_dataFolder;

		loadBodys();
		loadEvents();
		loadToDos();
	}

	@SuppressWarnings("resource") //Eclipse warning bug. There should not be a warning.
	private boolean loadBodys() {
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
			Main.mainLog.logException(e, Log.ERROR, true);
			return false;
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
		try (ObjectInputStream oIS = new ObjectInputStream(new FileInputStream(new File(this.dataFolder, "events.dat")))) {
			while (true) {
				Object tmpO = oIS.readObject();
				if (!(tmpO instanceof Event)) {
					oIS.close();
					throw new ClassCastException("WTF! There is a incompatible Object in here.");
				}
				Body tmpE = (Body) tmpO;
				this.bodys.put(new Long(tmpE.getKey()), tmpE);
			}
		} catch (EOFException e) {
			//Ignore
			return true;
		} catch (FileNotFoundException e) {
			Main.mainLog.logException(e, Log.ERROR, false);
			return false;
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
		try (ObjectInputStream oIS = new ObjectInputStream(new FileInputStream(new File(this.dataFolder, "todos.dat")))) {
			while (true) {
				Object tmpO = oIS.readObject();
				if (!(tmpO instanceof ToDo)) {
					oIS.close();
					throw new ClassCastException("WTF! There is a incompatible Object in here.");
				}
				Body tmpT = (Body) tmpO;
				this.bodys.put(new Long(tmpT.getKey()), tmpT);
			}
		} catch (EOFException e) {
			//Ignore
			return true;
		} catch (FileNotFoundException e) {
			Main.mainLog.logException(e, Log.ERROR, true);
			return false;
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

	/**
	 * Get's a copy of {@link #bodys bodys}
	 * 
	 * @return bodys
	 */
	public HashMap<Long, Body> getBodys() {
		return new HashMap<>(this.bodys);
	}

	/**
	 * Set's an object of{@link #bodys bodys}
	 * 
	 * @param body
	 *            body
	 * @return Whether it worked
	 */
	public boolean setBody(Body body) {
		this.bodys.put(new Long(body.getKey()), body);
		return saveBodys();
	}

	/**
	 * Get's a copy of {@link #events events}
	 * 
	 * @return events
	 */
	public HashMap<Long, Event> getEvents() {
		return new HashMap<>(this.events);
	}

	/**
	 * Set's an object of{@link #events events}
	 * 
	 * @param event
	 *            event
	 * @return Whether it worked
	 */
	public boolean setEvent(Event event) {
		this.events.put(new Long(event.getKey()), event);
		return saveEvents();
	}

	/**
	 * Get's a copy of {@link #toDos toDos}
	 * 
	 * @return toDos
	 */
	public HashMap<Long, ToDo> getToDos() {
		return new HashMap<>(this.toDos);
	}

	/**
	 * Set's an object of {@link #toDos toDos}
	 * 
	 * @param toDo
	 *            toDo
	 * @return Whether it worked
	 */
	public boolean setToDo(ToDo toDo) {
		this.toDos.put(new Long(toDo.getKey()), toDo);
		return saveToDos();
	}
}
