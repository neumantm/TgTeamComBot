/*
 * hackathon
 *
 * TODO: Project Beschreibung
 *
 * @author Tim Neumann
 * @version 1.0.0
 *
 */
package de.hackathon.hackathon.bot;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import de.hackathon.hackathon.Main;
import de.tim.lib.Log;

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
	/** The Update Handler */
	public static UpdateHandler handler;

	/**
	 * @see org.telegram.telegrambots.generics.LongPollingBot#onUpdateReceived(org.telegram.telegrambots.api.objects.Update)
	 */
	@Override
	public void onUpdateReceived(Update update) {
		// We check if the update has a message and the message has text
		Long chatId = update.getMessage().getChatId();
		boolean found = false;
		String[] arr = Main.config.getConfigValue("AllowdUsers").split(",");
		for(String s : arr)
		{
			if(s.equals(chatId + ""))
				found = true;
		}
		
	    if (update.hasMessage() && update.getMessage().hasText()) {
	    	
	    	if(update.getMessage().getText().equals("Join") && Main.dm.getBodys().get(chatId) == null) {
	    		int tokenI = (int) Math.random();
	    		String tokenS = String.format("%04d", tokenI);
	    		//Main.pendingUsers.put(tokenI, new User(chatId)); TODO
	    	}
	    	
	    	else if(Main.dm.getBodys().get(chatId) != null || found) {
	    		
	            
	        
	        switch(update.getMessage().getText()) {
	        	case "Help":
	        		break;
	        	case "AddUser":
	        		BotUtilities.addUser(update);
	        		break;
				default:
					BotUtilities.noMessage(update);
					break;
	        }
	        
	        
	    	} else {
	    		SendMessage message = new SendMessage() // Create a SendMessage object with mandatory fields
		                .setChatId(update.getMessage().getChatId())
		                .setText("No Permission");
	    		Main.mainLog.log(update.getMessage().getChatId().toString(), Log.DEBUG);
	    		try {
		            sendMessage(message); // Call method to send the message
		        } catch (TelegramApiException e) {
		            e.printStackTrace();
		        }
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