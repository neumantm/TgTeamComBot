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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import de.pinkTigers.tgTeamComBot.Logic;
import de.pinkTigers.tgTeamComBot.Main;
import de.pinkTigers.tgTeamComBot.data.Body;
import de.pinkTigers.tgTeamComBot.data.Group;
import de.pinkTigers.tgTeamComBot.data.User;

/**
 * TODO: Description
 * 
 * @author Tim Neumann, Fabian Hutzenlaub, Patrick Muerdter
 */
public class BotUtilities {

	private static Group currentlyEditing = null;

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
		HashMap<Long, PossibleSteps> handlerMap = Main.mainBot.handler.handlerMap;

		s: switch (nextStep) {
			case DEFAULT:
				if (!Main.isValidUser(new Long(chatId))) {
					handlerMap.put(new Long(chatId), PossibleSteps.UNKNOWN_USER);
					//BotUtilities.doNext(update, nextStep, chatId, message);
					Main.mainBot.handler.newUpdate(update);
					break;
				}
				if (message.toLowerCase().equals("adduser")) {
					BotUtilities.message(update, "Enter Token:");
					handlerMap.put(new Long(chatId), PossibleSteps.WAITING_FOR_TOKEN);
					break;
				}
				if (message.toLowerCase().equals("removeuser")) {
					BotUtilities.message(update,
							"Do You really want to remove yourself? Type \"Yes\" to proceed");
					handlerMap.put(new Long(chatId), PossibleSteps.CONFIRM_REMOVE_USER);
					break;
				}
				if (message.toLowerCase().equals("editgroup")) {
					BotUtilities.message(update, "Type in: \"new\" , \"edit\"");
					handlerMap.put(new Long(chatId), PossibleSteps.MANAGE_GROUP);
					break;
				}
				BotUtilities.message(update, "Invalid Command");
			break;
			case UNKNOWN_USER:
				if (Main.isValidUser(new Long(chatId))) {
					handlerMap.put(new Long(chatId), PossibleSteps.DEFAULT);
					//BotUtilities.doNext(update, nextStep, chatId, message);
					Main.mainBot.handler.newUpdate(update);
					break;
				}
				if (message.toLowerCase().equals("join")) {
					BotUtilities.message(update, "What's your name?");
					handlerMap.put(new Long(chatId), PossibleSteps.UU_JOIN_ASKED_NAME);
					break;
				}
				BotUtilities.message(update, "No Permission");
			break;
			case UU_JOIN_ASKED_NAME:

				for (Map.Entry<Long, Body> body : Main.dm.getBodys().entrySet()) {
					if (body.getValue() instanceof User) {
						if (((User) body.getValue()).getName().equals(message)) {
							BotUtilities.message(update,
									"This name is already in use. Please chose another one:");
							break s;
						}
					}
				}
				String tokenS;
				int tokenI;
				do {
					tokenI = (int) (Math.random() * 10000);
					tokenS = String.format("%04d", new Integer(tokenI));
				} while (Main.pendingUsers.containsKey(tokenS));

				Main.pendingUsers.put(tokenS, new User(message, chatId));
				handlerMap.put(new Long(chatId), PossibleSteps.UNKNOWN_USER);
				BotUtilities.message(update, tokenS);
			break;
			case WAITING_FOR_TOKEN:
				if (Main.pendingUsers.get(message) != null) {
					Main.dm.setBody(Main.pendingUsers.get(message));
					handlerMap.put(new Long(chatId), PossibleSteps.DEFAULT);
					handlerMap.put(new Long(Main.pendingUsers.get(message).getKey()),
							PossibleSteps.DEFAULT);
					BotUtilities.message(update, "successfull");
					BotUtilities.message(update, "Welcome to tgTeamComBot!",
							Main.pendingUsers.get(message).getKey());
					Main.pendingUsers.remove(message);
				}
				else {
					handlerMap.put(new Long(chatId), PossibleSteps.DEFAULT);
					BotUtilities.message(update, "failed");
				}

			break;
			case CONFIRM_REMOVE_USER:
				if (message.toLowerCase().equals("yes")) {
					Main.dm.removeBody(new Long(chatId));
					handlerMap.remove(new Long(chatId));
					BotUtilities.message(update, "You've been successfully removed.");
				}
				handlerMap.put(new Long(chatId), PossibleSteps.DEFAULT);
			break;
			case MANAGE_GROUP:
				if (message.toLowerCase().equals("new")) {
					BotUtilities.message(update, "Please enter the name:");
					handlerMap.put(new Long(chatId), PossibleSteps.WAITING_FOR_GROUPNAME);
				}
				if (message.toLowerCase().equals("edit")) {
					BotUtilities.message(update, "Please enter the name:");
					handlerMap.put(new Long(chatId), PossibleSteps.GET_GROUP_NAME);
				}
			break;
			case WAITING_FOR_GROUPNAME:
				for (Map.Entry<Long, Body> body : Main.dm.getBodys().entrySet()) {
					if (body.getValue() instanceof Group) {
						if (((Group) body.getValue()).getName().equals(message)) {
							BotUtilities.message(update,
									"This name is already in use. Please chose another one:");
							break s;
						}
					}
				}
				ArrayList<User> temp = new ArrayList<>();
				temp.add((User) Main.dm.getBodys().get(new Long(chatId)));
				Logic.addGroup(message, temp);
				handlerMap.put(new Long(chatId), PossibleSteps.DEFAULT);
				BotUtilities.message(update, "Your Group " + message + "has been added.");
			break;
			case GET_GROUP_NAME:
				for (Map.Entry<Long, Body> body : Main.dm.getBodys().entrySet()) {
					if (body.getValue() instanceof Group) {
						if (((Group) body.getValue()).getName().equals(message)) {
							BotUtilities.currentlyEditing = (Group) body.getValue();
							BotUtilities.message(update, "Now editing the Group " + message
									+ "! Type in: \"rename\" , \"addUser\" , \"delete\" ");
							handlerMap.put(new Long(chatId), PossibleSteps.EDIT_GROUP);
							break s;
						}
					}
				}
				BotUtilities.message(update, "Didn't find the Group " + message + ". Please enter another Groupname:");
			break;
			case EDIT_GROUP:
				if (message.toLowerCase().equals("rename")) {
					BotUtilities.currentlyEditing.setName(message);
					handlerMap.put(new Long(chatId), PossibleSteps.DEFAULT);
					BotUtilities.currentlyEditing = null;
					BotUtilities.message(update, "rename successfull");
					break;
				}
				if (message.toLowerCase().equals("addUser")) {
					BotUtilities.message(update, "Please enter the user to add:");
					handlerMap.put(new Long(chatId), PossibleSteps.ADD_USER_TO_GROUP);
					break;
				}
				if (message.toLowerCase().equals("delete")) {
					BotUtilities.message(update,
							"Do You really want to remove the Group " + BotUtilities.currentlyEditing.getName() + " ? Type \"Yes\" to proceed");
					handlerMap.put(new Long(chatId), PossibleSteps.CONFIRM_REMOVE_GROUP);
					break;
				}
				BotUtilities.message(update, "Please Type in: \"rename\" , \"addUser\" , \"delete\"");
			break;
			case ADD_USER_TO_GROUP:
				for (Map.Entry<Long, Body> body : Main.dm.getBodys().entrySet()) {
					if (body.getValue() instanceof User) {
						if (((User) body.getValue()).getName().equals(message)) {
							Logic.addUserToGroup((User) body.getValue(),
									BotUtilities.currentlyEditing.getKey());
							handlerMap.put(new Long(chatId), PossibleSteps.EDIT_GROUP);
							BotUtilities.message(update, "Now editing the Group " + message
									+ "! Type in: \"rename\" , \"addUser\" , \"delete\" ");
							break s;
						}
					}
				}
			break;
			case CONFIRM_REMOVE_GROUP:
				if (message.toLowerCase().equals("yes")) {
					Main.dm.getBodys().remove(new Long(BotUtilities.currentlyEditing.getKey()));
					BotUtilities.message(update, "The Group been successfully removed.");
					BotUtilities.currentlyEditing = null;
					handlerMap.put(new Long(chatId), PossibleSteps.DEFAULT);
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
				.setChatId(update.getMessage()
						.getChatId())
				.setText(
						"Please enter the Token");
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
				.setChatId(update.getMessage()
						.getChatId())
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
	public static void message(Update update,
			String bot_message) {
		SendMessage message = new SendMessage() // Create a SendMessage object with mandatory fields
				.setChatId(update.getMessage()
						.getChatId())
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
	public static void message(Update update,
			String bot_message, long userId) {
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