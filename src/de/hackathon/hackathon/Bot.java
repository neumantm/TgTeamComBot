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

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

/**
 * The bot. It handles the communication with the telegram servers.
 * 
 * @author Tim Neumann, Fabian Hutzenlaub, Patrick Muerdter
 */
public class Bot extends TelegramLongPollingBot {

	/** The name of the Bot */
	public static final String NAME = "hackathon";
	/** The Token of the Telegram Bot API */
	public static final String TOKEN = "448728416:AAF4AkOiDUc-zx13pxhAUTH_2kEGsdhrQto";

	/**
	 * @see org.telegram.telegrambots.generics.LongPollingBot#onUpdateReceived(org.telegram.telegrambots.api.objects.Update)
	 */
	@Override
	public void onUpdateReceived(Update update) {
		// We check if the update has a message and the message has text
	    if (update.hasMessage() && update.getMessage().hasText()) {
	    	if(Main.dm.getBodys().get(update.getMessage().getChatId()) != null) {
	    		
	        SendMessage message = new SendMessage() // Create a SendMessage object with mandatory fields
	                .setChatId(update.getMessage().getChatId())
	                .setText("HI");
	        try {
	            sendMessage(message); // Call method to send the message
	        } catch (TelegramApiException e) {
	            e.printStackTrace();
	        }
	        
	    	} else {
	    		SendMessage message = new SendMessage() // Create a SendMessage object with mandatory fields
		                .setChatId(update.getMessage().getChatId())
		                .setText("No Permission");
	    	}
	    }
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
