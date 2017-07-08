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

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import de.hackathon.hackathon.bot.Bot;
import de.hackathon.hackathon.data.User;
import de.tim.lib.Config;
import de.tim.lib.Log;

/**
 * Main class
 * 
 * @author Tim Neumann, Fabian Hutzenlaub, Patrick Muerdter
 */
public class Main {

	private static final String configLocation = "./config.conf";

	/** The main config */
	public static Config config;
	/** The main log */
	public static Log mainLog;
	/** The main data Manager */
	public static DataManager dm;
	/** The main Bot */
	public static Bot mainBot;

	public static HashMap<String , User> pendingUsers = new HashMap<>();
	
	/**
	 * Loads the config from disk.
	 */
	private static void loadConfig() {
		HashMap<String, String> configFields = new HashMap<>();

		configFields.put("LogLevel", "INFO");
		configFields.put("DataFolder", "data");
		configFields.put("AllowdUsers", "");

		try {
			Main.config = new Config(Main.configLocation, "Main Configuartion file for Hackathon.", configFields);
		} catch (IOException e) {
			Main.mainLog.logException(e, Log.ERROR, true);
		}
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

		//DM
		Main.dm = new DataManager(new File(Main.config.getConfigValue("DataFolder")));

		//Telegram Bot
		ApiContextInitializer.init();

		TelegramBotsApi botsApi = new TelegramBotsApi();

		try {
			mainBot = new Bot();
			botsApi.registerBot(mainBot);
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}

	}

}
