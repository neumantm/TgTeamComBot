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

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;

import de.hackathon.hackathon.data.*;
import de.tim.lib.Log;

/**
 * TODO: Description
 * 
 * @author Henne
 */
public class DataManager {

	HashMap<Long, Body> bodys = new HashMap<>();
	HashMap<Long, Event> event = new HashMap<>();
	HashMap<Long, ToDo> toDos = new HashMap<>();

	private File dataFolder;

	public DataManager(File p_dataFolder) {
		if (!p_dataFolder.exists()) {
			Main.mainLog.log("DataManager is requested to open non existent folder:" + p_dataFolder.getAbsolutePath(), Log.WARN);
			Main.closeData();
		}

		if (!p_dataFolder.isDirectory()) {
			Main.mainLog.log("DataManager is requested to open a file but should be a folder:" + p_dataFolder.getAbsolutePath(), Log.WARN);
			Main.closeData();
		}

		this.dataFolder = p_dataFolder;

		load();
	}

	private void load() {
		try (ObjectInputStream oIS = new ObjectInputStream(new FileInputStream(new File(this.dataFolder, "bodys.dat")))) {
			while (true) {
				Object tmpO = oIS.readObject();
				if (!(tmpO instanceof Body)) {
					oIS.close();
					throw new ClassCastException("WTF! There is a incompatible Object in here.");
				}
				Body tmpB = (Body) tmpO;
				this.bodys.put(tmpB., tmpD);
			}
		} catch (EOFException e) {
			//Ignore
			return true;
		} catch (FileNotFoundException e) {
			Main.getLog().logException(e, Log.ERROR, false);
			return false;
		} catch (IOException e) {
			Main.getLog().logException(e, Log.ERROR, false);
			return false;
		} catch (ClassNotFoundException e) {
			Main.getLog().logException(e, Log.ERROR, false);
			return false;
		}
	}

	public void save(String fileName) {
		try {
			
		}
	}
}
