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

import java.io.IOException;
import java.util.HashMap;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import de.tim.lib.Config;
import de.tim.lib.Log;

/**
 * Main class
 * 
 * @author Tim Neumann, Fabian Hutzenlaub, Patrick Muerdter
 */
public class Main {

	private static final String configLocation = "/home/pi/hackathon/config.conf";

	/** The main config */
	public static Config config;
	/** The main log */
	public static Log mainLog;
	/** The main data Manager */
	public static DataManager dm;

	/**
	 * Loads the config from disk.
	 */
	private static void loadConfig() {
		HashMap<String, String> configFields = new HashMap<>();

		configFields.put("LogLevel", "INFO");
		configFields.put("DataFolder", "data");

		try {
			Main.config = new Config(Main.configLocation, "Main Configuartion file for SteWoDB.", configFields);
		} catch (IOException e) {
			Main.mainLog.logException(e, Log.ERROR, true);
		}
	}

	/**
	 * Closes {@link #dm dm}
	 */
	public static void closeData() {
		Main.dm = null;
	}

	/**
	 * The main entry point
	 * 
	 * @param args
	 *            The command line arguments (Not used.)
	 */
	public static void main(String[] args) {
		///Log loading
		try {
			Main.mainLog = new Log(new String[] { "stdout", "main.log" }, Log.INFO);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		Main.mainLog.redirectSTDErr();

		//Config
		Main.loadConfig();

		Main.mainLog.setLogLevel(Log.getLogLevel(Main.config.getConfigValue("LogLevel")));

		Main.mainLog.log("Done loading configs.", Log.INFO);
		Main.mainLog.log("LogLevel DEBUG enabeled.", Log.DEBUG);

		
		
		
		
		 ApiContextInitializer.init();

	        TelegramBotsApi botsApi = new TelegramBotsApi();

	        try {
	            botsApi.registerBot(new Bot());
	        } catch (TelegramApiException e) {
	            e.printStackTrace();
	        }
		
		
	}

}
