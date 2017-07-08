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
package de.pinkTigers.tgTeamComBot.bot;

import java.util.Map;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import de.pinkTigers.tgTeamComBot.Main;
import de.pinkTigers.tgTeamComBot.data.Body;
import de.pinkTigers.tgTeamComBot.data.User;

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
				if (!Main.isValidUser(new Long(chatId))) {
					Main.mainBot.handler.handlerMap.put(new Long(chatId), PossibleSteps.UNKNOWN_USER);
					//BotUtilities.doNext(update, nextStep, chatId, message);
					Main.mainBot.handler.newUpdate(update);
					break;
				}
				if (message.toLowerCase().equals("adduser")) {
					BotUtilities.message(update, "Enter Token:");
					Main.mainBot.handler.handlerMap.put(new Long(chatId), PossibleSteps.WAITING_FOR_TOKEN);
					break;
				}
				if (message.toLowerCase().equals("removeuser")) {
					BotUtilities.message(update, "Do You really want to remove yourself? Type \"Yes\" to proceed");
					Main.mainBot.handler.handlerMap.put(new Long(chatId), PossibleSteps.CONFIRM_REMOVE);
					break;
				}
				if (message.toLowerCase().equals("editgroup")) {
					BotUtilities.message(update, "Type in: \"new\" , \"delete\" , \"rename\" ");
					Main.mainBot.handler.handlerMap.put(new Long(chatId), PossibleSteps.CONFIRM_REMOVE);
					break;
				}
				BotUtilities.message(update, "Invalid Command");
			break;
			case UNKNOWN_USER:
				if (Main.isValidUser(new Long(chatId))) {
					Main.mainBot.handler.handlerMap.put(new Long(chatId), PossibleSteps.DEFAULT);
					//BotUtilities.doNext(update, nextStep, chatId, message);
					Main.mainBot.handler.newUpdate(update);
					break;
				}
				if (message.toLowerCase().equals("Join")) {
					BotUtilities.message(update, "What's your name?");
					Main.mainBot.handler.handlerMap.put(new Long(chatId), PossibleSteps.UU_JOIN_ASKED_NAME);
					break;
				}
				BotUtilities.message(update, "No Permission");
			break;
			case UU_JOIN_ASKED_NAME:

				for (Map.Entry<Long, Body> body : Main.dm.getBodys().entrySet()) {
					if (body.getValue() instanceof User && ((User) body).getName().equals(message)) {
						BotUtilities.message(update, "This name is already in use. Please chose another one");
						break;
					}
				}
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
			case WAITING_FOR_TOKEN:
				if (Main.pendingUsers.get(message) != null) {
					Main.dm.setBody(Main.pendingUsers.get(message));
					Main.mainBot.handler.handlerMap.put(new Long(chatId), PossibleSteps.DEFAULT);
					Main.mainBot.handler.handlerMap.put(new Long(Main.pendingUsers.get(message).getKey()), PossibleSteps.DEFAULT);
					BotUtilities.message(update, "successfull");
					BotUtilities.message(update, "Welcome to tgTeamComBot", Main.pendingUsers.get(message).getKey());
					Main.pendingUsers.remove(message);
				}
				else {
					Main.mainBot.handler.handlerMap.put(new Long(chatId), PossibleSteps.DEFAULT);
					BotUtilities.message(update, "failed");
				}

			break;
			case CONFIRM_REMOVE:
				if (message.toLowerCase().equals("yes")) {
					Main.dm.removeBody(new Long(chatId));
					Main.mainBot.handler.handlerMap.remove(new Long(chatId));
					BotUtilities.message(update, "You've been successfully removed");
				}
				Main.mainBot.handler.handlerMap.put(new Long(chatId), PossibleSteps.DEFAULT);
			break;
			case EDIT_GROUP:
				if (message.toLowerCase().equals("adduser")) {
					BotUtilities.message(update, "failed");
				}
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
	 *            messsage from the bot
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

	/**
	 * @param update
	 *            update
	 * @param bot_message
	 *            message from bot
	 * @param userId
	 *            recipient id
	 */
	public static void message(Update update, String bot_message, long userId) {
		SendMessage message = new SendMessage() // Create a SendMessage object with mandatory fields
				.setChatId(new Long(userId))
				.setText(bot_message);
		try {
			Main.mainBot.sendMessage(message); // Call method to send the message
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}
}
