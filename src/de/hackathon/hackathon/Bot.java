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

import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

/**
 * The bot. It handles the communication with the telegram servers.
 * 
 * @author Tim Neumann, Fabian Hutzenlaub, Patrick Muerdter
 */
public class Bot extends TelegramLongPollingBot {

	public static final String NAME = "hackathon";
	public static final String TOKEN = "448728416:AAF4AkOiDUc-zx13pxhAUTH_2kEGsdhrQto";

	/**
	 * @see org.telegram.telegrambots.generics.LongPollingBot#onUpdateReceived(org.telegram.telegrambots.api.objects.Update)
	 */
	@Override
	public void onUpdateReceived(Update arg0) {

	}

	/**
	 * @see org.telegram.telegrambots.generics.LongPollingBot#getBotUsername()
	 */
	@Override
	public String getBotUsername() {
		return Bot.NAME;
	}

	/**
	 * @see org.telegram.telegrambots.bots.DefaultAbsSender#getBotToken()
	 */
	@Override
	public String getBotToken() {
		return Bot.TOKEN;
	}

}
