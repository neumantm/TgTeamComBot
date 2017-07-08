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
package de.hackathon.hackathon.bot;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import de.hackathon.hackathon.Main;
import de.hackathon.hackathon.data.User;

/**
 * TODO: Description
 * 
 * @author Tim Neumann, Fabian Hutzenlaub, Patrick Muerdter
 */
public class BotUtilities {

	/**
	 * @param update
	 *            update message
	 * @param nextStep
	 *            next Step
	 * @param chatId
	 *            user ID
	 * @param message
	 *            User message
	 */
	public static void doNext(Update update, PossibleSteps nextStep, long chatId, String message) {

		switch (nextStep) {
			case DEFAULT:
				if (!Main.isUserInConf(new Long(chatId))) {
					Main.mainBot.handler.handlerMap.put(new Long(chatId), PossibleSteps.UNKNOWN_USER);
					BotUtilities.doNext(update, nextStep, chatId, message);
					break;
				}

			break;
			case UNKNOWN_USER:
				if (message.equals("Join")) {
					BotUtilities.message(update, "What's your name?");
					Main.mainBot.handler.handlerMap.put(new Long(chatId), PossibleSteps.UU_JOIN_ASKED_NAME);
				}
			break;
			case UU_JOIN_ASKED_NAME:
				String tokenS;
				int tokenI;
				do {
					tokenI = (int) (Math.random() * 10000);
					tokenS = String.format("%04d", new Integer(tokenI));
				} while (Main.pendingUsers.containsKey(tokenS));

				Main.pendingUsers.put(tokenS, new User(message, chatId));
				Main.mainBot.handler.handlerMap.put(new Long(chatId), PossibleSteps.UNKNOWN_USER);
				BotUtilities.message(update, tokenS);
			break;

			default:
			break;
		}
	}

	/**
	 * @param update
	 *            update
	 */
	public static void addUser(Update update) {

		SendMessage message = new SendMessage() // Create a SendMessage object with mandatory fields
				.setChatId(update.getMessage().getChatId())
				.setText("Please enter the Token");
		try {
			Main.mainBot.sendMessage(message); // Call method to send the message
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param update
	 *            update
	 */
	public static void noMessage(Update update) {
		SendMessage message = new SendMessage() // Create a SendMessage object with mandatory fields
				.setChatId(update.getMessage().getChatId())
				.setText("No Command");
		try {
			Main.mainBot.sendMessage(message); // Call method to send the message
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param update
	 *            update
	 * @param bot_message
	 *            question from the bot
	 */
	public static void message(Update update, String bot_message) {
		SendMessage message = new SendMessage() // Create a SendMessage object with mandatory fields
				.setChatId(update.getMessage().getChatId())
				.setText(bot_message);
		try {
			Main.mainBot.sendMessage(message); // Call method to send the message
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}

	}

}
