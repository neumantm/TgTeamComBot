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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import de.pinkTigers.tgTeamComBot.Logic;
import de.pinkTigers.tgTeamComBot.Main;
import de.pinkTigers.tgTeamComBot.data.Body;
import de.pinkTigers.tgTeamComBot.data.Event;
import de.pinkTigers.tgTeamComBot.data.Group;
import de.pinkTigers.tgTeamComBot.data.User;
import de.tim.lib.Log;

/**
 * TODO: Description
 * 
 * @author Tim Neumann, Fabian Hutzenlaub, Patrick Muerdter
 */
public class BotUtilities {

	private static Long currentlyEditing = null;
	private static Event currentEvent = null;

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
				BotUtilities.currentlyEditing = null;
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
				if (message.toLowerCase().equals("showgroups")) {
					String allGroups = "";
					for (Map.Entry<Long, Body> body : Main.dm.getBodys().entrySet()) {
						if (body.getValue() instanceof Group) {
							allGroups += "\n" + ((Group) body.getValue()).getName();
						}
					}
					if (allGroups.equals("")) {
						allGroups = "No Groups";
					}
					BotUtilities.message(update, allGroups);
					handlerMap.put(new Long(chatId), PossibleSteps.DEFAULT);
					break;
				}
				if (message.toLowerCase().equals("manageevents")) {
					BotUtilities.message(update, "Type in: \"new\" , \"edit\" , \"showinfo\" ");
					handlerMap.put(new Long(chatId), PossibleSteps.MANAGE_EVENT);
					break;
				}
				if (message.toLowerCase().equals("managetodos")) {
					BotUtilities.message(update, "Type in: \"new\" , \"edit\" , \"showinfo\" ");
					handlerMap.put(new Long(chatId), PossibleSteps.MANAGE_TODO);
					break;
				}
				BotUtilities.message(update, "Invalid Command");
			break;
			case UNKNOWN_USER:
				if (Main.isValidUser(new Long(chatId))) {
					handlerMap.put(new Long(chatId), PossibleSteps.DEFAULT);
					Main.mainBot.handler.newUpdate(update);
					break;
				}
				if (message.toLowerCase().equals("join")) {
					for (Map.Entry<String, User> user : Main.pendingUsers.entrySet()) {
						if (user.getValue().getKey() == chatId) {
							BotUtilities.message(update, "User already pending! Your Token: " + user.getKey());
							break s;
						}
					}
					BotUtilities.message(update, "What's your name?");
					handlerMap.put(new Long(chatId), PossibleSteps.UU_JOIN_ASKED_NAME);
					break;
				}
				BotUtilities.message(update, "No Permission");
			break;
			case UU_JOIN_ASKED_NAME:

				for (Map.Entry<Long, Body> body : Main.dm.getBodys().entrySet()) {
					if (body.getValue().getName().equals(message)) {
						BotUtilities.message(update,
								"This name is already in use. Please chose another one:");
						break s;
					}
				}
				String tokenS;
				int tokenI;
				do {
					tokenI = (int) (Math.random() * 10000);
					tokenS = String.format("%04d", new Integer(tokenI));
				} while (Main.pendingUsers.containsKey(tokenS));

				Main.pendingUsers.put(tokenS, new User(chatId, message));
				handlerMap.put(new Long(chatId), PossibleSteps.UNKNOWN_USER);
				BotUtilities.message(update, "Here is your token: " + tokenS);
			break;
			case WAITING_FOR_TOKEN:
				if (Main.pendingUsers.get(message) != null) {
					Main.dm.setBody(Main.pendingUsers.get(message));
					handlerMap.put(new Long(chatId), PossibleSteps.DEFAULT);
					handlerMap.put(new Long(Main.pendingUsers.get(message).getKey()),
							PossibleSteps.DEFAULT);
					BotUtilities.message(update, "successfull");
					BotUtilities.message("Welcome to tgTeamComBot!", Main.pendingUsers.get(message).getKey());
					Main.pendingUsers.remove(message);
				}
				else {
					handlerMap.put(new Long(chatId), PossibleSteps.DEFAULT);
					BotUtilities.message(update, "failed");
				}

			break;
			case CONFIRM_REMOVE_USER:
				if (message.toLowerCase().equals("yes")) {
					Logic.removeBody(new Long(chatId));
					handlerMap.remove(new Long(chatId));
					BotUtilities.message(update, "You've been successfully removed.");
				}
				handlerMap.put(new Long(chatId), PossibleSteps.DEFAULT);
			break;
			case MANAGE_GROUP:
				if (message.toLowerCase().equals("new")) {
					BotUtilities.message(update, "Please enter the name for the new Group:");
					handlerMap.put(new Long(chatId), PossibleSteps.WAITING_FOR_GROUPNAME);
				}
				if (message.toLowerCase().equals("edit")) {
					BotUtilities.message(update, "Please enter the groupname you want to edit:");
					handlerMap.put(new Long(chatId), PossibleSteps.GET_GROUP_NAME);
				}
			break;
			case WAITING_FOR_GROUPNAME:
				for (Map.Entry<Long, Body> body : Main.dm.getBodys().entrySet()) {
					if (body.getValue().getName().equals(message)) {
						BotUtilities.message(update,
								"This name is already in use. Please chose another one:");
						break s;
					}
				}
				Logic.addGroup(message, Main.dm.getBodys().get(new Long(chatId)));
				handlerMap.put(new Long(chatId), PossibleSteps.DEFAULT);
				BotUtilities.message(update, "Your Group " + message + " has been added.");
			break;
			case GET_GROUP_NAME:
				for (Map.Entry<Long, Body> body : Main.dm.getBodys().entrySet()) {
					if (body.getValue().getName().equals(message)) {
						BotUtilities.currentlyEditing = body.getKey();
						BotUtilities.message(update, "Now editing the Group " + message
								+ "! Type in: \"rename\" , \"addUser\", \"getUsers\", \"removeUser\" , \"delete\" ");
						handlerMap.put(new Long(chatId), PossibleSteps.EDIT_GROUP);
						break s;

					}
				}
				BotUtilities.message(update, "Didn't find the Group " + message + ". Please enter another Groupname:");
			break;
			case EDIT_GROUP:
				if (message.toLowerCase().equals("rename")) {
					BotUtilities.message(update, "Please enter the new groupname:");
					handlerMap.put(new Long(chatId), PossibleSteps.NEW_GROUP_NAME);
					break;
				}
				if (message.toLowerCase().equals("adduser")) {
					BotUtilities.message(update, "Please enter the user to add:");
					handlerMap.put(new Long(chatId), PossibleSteps.ADD_USER_TO_GROUP);
					break;
				}
				if (message.toLowerCase().equals("delete")) {
					BotUtilities.message(update,
							"Do You really want to remove the Group " + ((Group) Main.dm.getBodys().get(BotUtilities.currentlyEditing)).getName()
									+ " ? Type \"Yes\" to proceed");
					handlerMap.put(new Long(chatId), PossibleSteps.CONFIRM_REMOVE_GROUP);
					break;
				}
				if (message.toLowerCase().equals("removeuser")) {
					BotUtilities.message(update, "Please enter the name of the user you want to remove:");
					handlerMap.put(new Long(chatId), PossibleSteps.REMOVE_USER_FROM_GROUP);
					break;
				}
				if (message.toLowerCase().equals("getusers")) {
					String usersToReturn = "";
					for (User user : Main.dm.getBodys().get(BotUtilities.currentlyEditing).getUsers()) {
						usersToReturn += "\n" + user.getName();
					}
					if (usersToReturn.equals("")) {
						usersToReturn = "No Users in Group";
					}
					BotUtilities.message(update, usersToReturn);
					break;
				}
				BotUtilities.message(update, "Please Type in: \"rename\" , \"addUser\", \"getUsers\", \"removeUser\" , \"delete\"");
			break;
			case ADD_USER_TO_GROUP:
				for (Map.Entry<Long, Body> body : Main.dm.getBodys().entrySet()) {
					if (body.getValue().getName().equals(message)) {
						if (!Main.dm.getBodys().get(BotUtilities.currentlyEditing).getUsers().contains(body.getValue())) {
							Logic.addUserToGroup(body.getValue(),
									BotUtilities.currentlyEditing.longValue());
							handlerMap.put(new Long(chatId), PossibleSteps.EDIT_GROUP);
							BotUtilities.message(update, "User " + message
									+ " has been added to the group! \n \"Please Type in: \"rename\" , \"addUser\" , \"getUsers\", \"removeUser\" , \"delete\" ");
							break s;
						}

					}
				}
				BotUtilities.message(update, "Couldn't add user!");
			break;
			case CONFIRM_REMOVE_GROUP:
				if (message.toLowerCase().equals("yes")) {
					Logic.removeBody(BotUtilities.currentlyEditing);
					Main.mainLog.log((BotUtilities.currentlyEditing.toString()), Log.DEBUG);
					BotUtilities.message(update, "The Group has been successfully removed.");
					BotUtilities.currentlyEditing = null;
					handlerMap.put(new Long(chatId), PossibleSteps.DEFAULT);
				}
				else {
					BotUtilities.message(update, "Remove cancelled.");
					handlerMap.put(new Long(chatId), PossibleSteps.EDIT_GROUP);
				}
			break;
			case NEW_GROUP_NAME:
				Body groupToEdit = Main.dm.getBodys().get(BotUtilities.currentlyEditing);
				groupToEdit.setName(message);
				Logic.setBody(groupToEdit);
				BotUtilities.currentlyEditing = null;
				BotUtilities.message(update, "rename successfull");
				handlerMap.put(new Long(chatId), PossibleSteps.DEFAULT);
			break;
			case REMOVE_USER_FROM_GROUP:
				for (Map.Entry<Long, Body> body : Main.dm.getBodys().entrySet()) {
					if (body.getValue().getName().equals(message)) {
						Logic.removeBodyFromGroup(body.getValue(), BotUtilities.currentlyEditing.longValue());
						BotUtilities.message(update, "Remove successfull.");
						handlerMap.put(new Long(chatId), PossibleSteps.EDIT_GROUP);
						break s;
					}
				}
				BotUtilities.message(update, "Remove failed.");
			break;

			//EVENTS

			case MANAGE_EVENT:
				if (message.toLowerCase().equals("new")) {
					BotUtilities.message(update, "Please enter a name for the new Event:");
					handlerMap.put(new Long(chatId), PossibleSteps.WAITING_FOR_EVENT_NAME);
					break;
				}
				if (message.toLowerCase().equals("showinfo")) {
					BotUtilities.message(update, "Please enter the eventname:");
					handlerMap.put(new Long(chatId), PossibleSteps.WAITING_FOR_EVENT_NAME2);
					break;
				}
				if (message.toLowerCase().equals("edit")) {
					BotUtilities.message(update, "Please enter the eventname:");
					handlerMap.put(new Long(chatId), PossibleSteps.WAITING_FOR_EVENT_NAME3);
					break;
				}
			break;

			case WAITING_FOR_EVENT_NAME:
				for (Map.Entry<Long, Event> event : Main.dm.getEvents().entrySet()) {
					if (event.getValue().getName().equals(message)) {
						BotUtilities.message(update,
								"This name is already in use. Please chose another one:");
						handlerMap.put(new Long(chatId), PossibleSteps.WAITING_FOR_EVENT_NAME);
						break s;
					}
				}
				BotUtilities.currentEvent = Logic.createEvent(message);
				BotUtilities.message(update, "The Event " + message + " has been added.");
				handlerMap.put(new Long(chatId), PossibleSteps.ADD_INFO_TO_EVENT);
			break;
			case WAITING_FOR_EVENT_NAME2:
				String info = "No info availible";
				for (Map.Entry<Long, Event> event : Main.dm.getEvents().entrySet()) {
					if (event.getValue().getName().equals(message)) {
						Event currentEvent = event.getValue();
						info = "Name: " + currentEvent.getName() + "\nDate: " + currentEvent.getDate().toString() + "\nLocation: "
								+ currentEvent.getLocation() + "\nDescription: " + currentEvent.getDescription();
						BotUtilities.message(update, info);
						handlerMap.put(new Long(chatId), PossibleSteps.DEFAULT);
						break s;
					}
				}
				BotUtilities.message(update, info);
				handlerMap.put(new Long(chatId), PossibleSteps.DEFAULT);
			break;
			case WAITING_FOR_EVENT_NAME3:
				for (Map.Entry<Long, Event> event : Main.dm.getEvents().entrySet()) {
					if (event.getValue().getName().equals(message)) {
						BotUtilities.currentEvent = event.getValue();
						handlerMap.put(new Long(chatId), PossibleSteps.ADD_INFO_TO_EVENT);
						break s;
					}
				}
			break;
			case ADD_INFO_TO_EVENT:
				if (message.toLowerCase().equals("editdescription")) {
					BotUtilities.message(update, "Please enter your description:");
					handlerMap.put(new Long(chatId), PossibleSteps.EVENT_ADD_DESCRIPTION);
					break;
				}
				if (message.toLowerCase().equals("editlocation")) {
					BotUtilities.message(update, "Please enter your location:");
					handlerMap.put(new Long(chatId), PossibleSteps.EVENT_ADD_LOCATION);
					break;
				}
				if (message.toLowerCase().equals("editdate")) {
					BotUtilities.message(update, "Please enter your date(dd/MM/yyyy):");
					handlerMap.put(new Long(chatId), PossibleSteps.EVENT_ADD_DATE);
					break;
				}
				if (message.toLowerCase().equals("editname")) {
					BotUtilities.message(update, "Please enter your new Name:");
					handlerMap.put(new Long(chatId), PossibleSteps.EVENT_EDIT_NAME);
					break;
				}
				if (message.toLowerCase().equals("done")) {
					Logic.addEvent(BotUtilities.currentEvent);
					BotUtilities.currentEvent = null;
					BotUtilities.message(update, "Added Event");
					handlerMap.put(new Long(chatId), PossibleSteps.DEFAULT);
					break;
				}
			break;
			case EVENT_ADD_DESCRIPTION:
				BotUtilities.currentEvent.setDescription(message);
				BotUtilities.message(update, "Added Description");
				handlerMap.put(new Long(chatId), PossibleSteps.ADD_INFO_TO_EVENT);
			break;
			case EVENT_ADD_LOCATION:
				BotUtilities.currentEvent.setLocation(message);
				BotUtilities.message(update, "Added location");
				handlerMap.put(new Long(chatId), PossibleSteps.ADD_INFO_TO_EVENT);
			break;
			case EVENT_ADD_DATE:
				try {
					BotUtilities.currentEvent.setDate(new SimpleDateFormat("dd/MM/yyyy").parse(message));
				} catch (ParseException e) {
					e.printStackTrace();
					BotUtilities.message(update, "Failed! Try Again!");
					handlerMap.put(new Long(chatId), PossibleSteps.ADD_INFO_TO_EVENT);
					break;
				}
				BotUtilities.message(update, "Added Date");
				handlerMap.put(new Long(chatId), PossibleSteps.ADD_INFO_TO_EVENT);
			break;
			case EVENT_EDIT_NAME:
				for (Map.Entry<Long, Event> event : Main.dm.getEvents().entrySet()) {
					if (event.getValue().getName().equals(message)) {
						BotUtilities.message(update,
								"This name is already in use. Please chose another one:");
						handlerMap.put(new Long(chatId), PossibleSteps.EVENT_EDIT_NAME);
						break s;
					}
				}
				BotUtilities.currentEvent.setName(message);
				handlerMap.put(new Long(chatId), PossibleSteps.ADD_INFO_TO_EVENT);
				BotUtilities.message(update, "Name set");
			break;
			
			//ToDo
			
			case MANAGE_TODO:
				if (message.toLowerCase().equals("new")) {
					BotUtilities.message(update, "Please enter a name for the new ToDo:");
					handlerMap.put(new Long(chatId), PossibleSteps.WAITING_FOR_TODO_NAME);
					break;
				}
				if (message.toLowerCase().equals("showinfo")) {
					BotUtilities.message(update, "Please enter the todoname:");
					handlerMap.put(new Long(chatId), PossibleSteps.WAITING_FOR_TODO_NAME2);
					break;
				}
				if (message.toLowerCase().equals("edit")) {
					BotUtilities.message(update, "Please enter the todoname:");
					handlerMap.put(new Long(chatId), PossibleSteps.WAITING_FOR_TODO_NAME3);
					break;
				}
			break;

			case WAITING_FOR_TODO_NAME:
				for (Map.Entry<Long, Event> event : Main.dm.getEvents().entrySet()) {
					if (event.getValue().getName().equals(message)) {
						BotUtilities.message(update,
								"This name is already in use. Please chose another one:");
						handlerMap.put(new Long(chatId), PossibleSteps.WAITING_FOR_TODO_NAME);
						break s;
					}
				}
				BotUtilities.currentEvent = Logic.createEvent(message);
				BotUtilities.message(update, "The Event " + message + " has been added.");
				handlerMap.put(new Long(chatId), PossibleSteps.ADD_INFO_TO_EVENT);
			break;
			case WAITING_FOR_TODO_NAME2:
				String info2 = "No info availible";
				for (Map.Entry<Long, Event> event : Main.dm.getEvents().entrySet()) {
					if (event.getValue().getName().equals(message)) {
						Event currentEvent = event.getValue();
						info2 = "Name: " + currentEvent.getName() + "\nDate: " + currentEvent.getDate().toString() + "\nLocation: "
								+ currentEvent.getLocation() + "\nDescription: " + currentEvent.getDescription();
						BotUtilities.message(update, info2);
						handlerMap.put(new Long(chatId), PossibleSteps.DEFAULT);
						break s;
					}
				}
				BotUtilities.message(update, info2);
				handlerMap.put(new Long(chatId), PossibleSteps.DEFAULT);
			break;
			case WAITING_FOR_TODO_NAME3:
				for (Map.Entry<Long, Event> event : Main.dm.getEvents().entrySet()) {
					if (event.getValue().getName().equals(message)) {
						BotUtilities.currentEvent = event.getValue();
						handlerMap.put(new Long(chatId), PossibleSteps.ADD_INFO_TO_TODO);
						break s;
					}
				}
			break;
			case ADD_INFO_TO_TODO:
				if (message.toLowerCase().equals("editdescription")) {
					BotUtilities.message(update, "Please enter your description:");
					handlerMap.put(new Long(chatId), PossibleSteps.TODO_ADD_DESCRIPTION);
					break;
				}
				if (message.toLowerCase().equals("editlocation")) {
					BotUtilities.message(update, "Please enter your location:");
					handlerMap.put(new Long(chatId), PossibleSteps.TODO_ADD_PRIORITY);
					break;
				}
				if (message.toLowerCase().equals("editdate")) {
					BotUtilities.message(update, "Please enter your date(dd/MM/yyyy):");
					handlerMap.put(new Long(chatId), PossibleSteps.TODO_ADD_DATE);
					break;
				}
				if (message.toLowerCase().equals("editname")) {
					BotUtilities.message(update, "Please enter your new Name:");
					handlerMap.put(new Long(chatId), PossibleSteps.TODO_EDIT_NAME);
					break;
				}
				if (message.toLowerCase().equals("done")) {
					Logic.addEvent(BotUtilities.currentEvent);
					BotUtilities.currentEvent = null;
					BotUtilities.message(update, "Added Event");
					handlerMap.put(new Long(chatId), PossibleSteps.DEFAULT);
					break;
				}
			break;
			case TODO_ADD_DESCRIPTION:
				BotUtilities.currentEvent.setDescription(message);
				BotUtilities.message(update, "Added Description");
				handlerMap.put(new Long(chatId), PossibleSteps.ADD_INFO_TO_TODO);
			break;
			case TODO_ADD_PRIORITY:
				BotUtilities.currentEvent.setLocation(message);
				BotUtilities.message(update, "Added location");
				handlerMap.put(new Long(chatId), PossibleSteps.ADD_INFO_TO_TODO);
			break;
			case TODO_ADD_DATE:
				try {
					BotUtilities.currentEvent.setDate(new SimpleDateFormat("dd/MM/yyyy").parse(message));
				} catch (ParseException e) {
					e.printStackTrace();
					BotUtilities.message(update, "Failed! Try Again!");
					handlerMap.put(new Long(chatId), PossibleSteps.ADD_INFO_TO_TODO);
					break;
				}
				BotUtilities.message(update, "Added Date");
				handlerMap.put(new Long(chatId), PossibleSteps.ADD_INFO_TO_TODO);
			break;
			case TODO_EDIT_NAME:
				for (Map.Entry<Long, Event> event : Main.dm.getEvents().entrySet()) {
					if (event.getValue().getName().equals(message)) {
						BotUtilities.message(update,
								"This name is already in use. Please chose another one:");
						handlerMap.put(new Long(chatId), PossibleSteps.TODO_EDIT_NAME);
						break s;
					}
				}
				BotUtilities.currentEvent.setName(message);
				handlerMap.put(new Long(chatId), PossibleSteps.ADD_INFO_TO_TODO);
				BotUtilities.message(update, "Name set");
			break;
			
			default:
			break;
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
	 * @param bot_message
	 *            message from bot
	 * @param userId
	 *            recipient id
	 */
	public static void message(String bot_message,
			long userId) {
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
