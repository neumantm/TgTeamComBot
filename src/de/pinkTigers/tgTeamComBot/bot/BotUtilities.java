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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import de.pinkTigers.tgTeamComBot.Logic;
import de.pinkTigers.tgTeamComBot.Main;
import de.pinkTigers.tgTeamComBot.data.Body;
import de.pinkTigers.tgTeamComBot.data.Event;
import de.pinkTigers.tgTeamComBot.data.Group;
import de.pinkTigers.tgTeamComBot.data.ToDo;
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
	private static ToDo currentTodo = null;

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
					handlerMap.put(new Long(chatId), PossibleSteps.WAITING_FOR_TOKEN);
					BotUtilities.message(update, "Enter Token:");
					break;
				}
				if (message.toLowerCase().equals("removeuser")) {
					handlerMap.put(new Long(chatId), PossibleSteps.CONFIRM_REMOVE_USER);
					BotUtilities.message(update,
							"Do You really want to remove yourself? Type \"Yes\" to proceed");
					break;
				}
				if (message.toLowerCase().equals("editgroup")) {
					handlerMap.put(new Long(chatId), PossibleSteps.MANAGE_GROUP);
					BotUtilities.message(update, "Type in: \"new\" , \"edit\"");
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
					handlerMap.put(new Long(chatId), PossibleSteps.DEFAULT);
					BotUtilities.message(update, allGroups);
					break;
				}
				if (message.toLowerCase().equals("manageevents")) {
					handlerMap.put(new Long(chatId), PossibleSteps.MANAGE_EVENT);
					BotUtilities.message(update, "Type in: \"new\" , \"edit\" , \"showinfo\" ");
					break;
				}
				if (message.toLowerCase().equals("managetodos")) {
					handlerMap.put(new Long(chatId), PossibleSteps.MANAGE_TODO);
					BotUtilities.message(update, "Type in: \"new\" , \"edit\" , \"showinfo\" ");
					break;
				}
				if (message.toLowerCase().equals("joinevent")) {
					handlerMap.put(new Long(chatId), PossibleSteps.ADD_EVENT_TO_USER);
					BotUtilities.message(update, "Name of the Event:");
					break;
				}
				if (message.toLowerCase().equals("removeevent")) {
					handlerMap.put(new Long(chatId), PossibleSteps.REMOVE_EVENT_FROM_USER);
					BotUtilities.message(update, "Name of the Event:");
					break;
				}
				if (message.toLowerCase().equals("jointodo")) {
					handlerMap.put(new Long(chatId), PossibleSteps.ADD_TODO_TO_USER);
					BotUtilities.message(update, "Name of the ToDo:");
					break;
				}
				if (message.toLowerCase().equals("removetodo")) {
					handlerMap.put(new Long(chatId), PossibleSteps.REMOVE_TODO_FROM_USER);
					BotUtilities.message(update, "Name of the ToDo:");
					break;
				}
				if (message.toLowerCase().equals("gettodos")) {
					Map<Long, ToDo> priority = new HashMap<>();
					Map<Long, ToDo> priority2 = new HashMap<>();
					for (Entry<Long, Body> e : Main.dm.getBodys().entrySet()) {
						if (e.getKey() == chatId || e.getValue().getUsers().contains(Main.dm.getUser(chatId))) {
							//My self ||Group that i'm in
							for (ToDo td : e.getValue().getToDos()) {
								if (!priority.containsKey(td.getKey())) {
									priority.put(td.getKey(), td);
								}
							}
						}
					}
					priority2 = new HashMap<>(priority);

					int highestPriority = Integer.MIN_VALUE;
					long id = 0;
					String output = "ToDos: ";
					ArrayList<Long> priorities = new ArrayList<>();

					while (!priority.isEmpty()) {
						highestPriority = Integer.MIN_VALUE;
						id = 0;
						for (Map.Entry<Long, ToDo> entry : priority.entrySet())
							if (entry.getValue().getPriority() >= highestPriority) {
								highestPriority = entry.getValue().getPriority();
								id = entry.getValue().getKey();
							}
						priorities.add(id);
						priority.remove(id);
					}
					for (Long Id : priorities) {
						output += "\nToDoName: " + priority2.get(Id).getName() + "	Priority: " + priority2.get(Id).getPriority();
					}
					handlerMap.put(new Long(chatId), PossibleSteps.DEFAULT);
					BotUtilities.message(update, output);
					break;
				}
				if (message.toLowerCase().equals("getevents")) {
					Map<Long, Event> nextEvent = new HashMap<>();
					Map<Long, Event> nextEvent2 = new HashMap<>();
					for (Entry<Long, Body> e : Main.dm.getBodys().entrySet()) {
						if (e.getKey() == chatId || e.getValue().getUsers().contains(Main.dm.getUser(chatId))) {
							//My self ||Group that i'm in
							for (Event event : e.getValue().getEvents()) {
								if (!nextEvent.containsKey(event.getKey())) {
									nextEvent.put(event.getKey(), event);
								}
							}
						}
					}
					nextEvent2 = new HashMap<>(nextEvent);

					long closestDate = Long.MAX_VALUE;
					long id = 0;
					String output = "Events: ";
					ArrayList<Long> priorities = new ArrayList<>();

					while (!nextEvent.isEmpty()) {
						closestDate = Long.MAX_VALUE;
						id = 0;
						for (Map.Entry<Long, Event> entry : nextEvent.entrySet())
							if (entry.getValue().getDate().getTime() <= closestDate) {
								closestDate = entry.getValue().getDate().getTime();
								id = entry.getValue().getKey();
							}
						priorities.add(id);
						nextEvent.remove(id);
					}
					for (Long Id : priorities) {
						output += "\nEventName: " + nextEvent2.get(Id).getName() + "	Date: " + nextEvent2.get(Id).getDate();
					}
					handlerMap.put(new Long(chatId), PossibleSteps.DEFAULT);
					BotUtilities.message(update, output);
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
					handlerMap.put(new Long(chatId), PossibleSteps.UU_JOIN_ASKED_NAME);
					BotUtilities.message(update, "What's your name?");
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
					handlerMap.put(new Long(chatId), PossibleSteps.UNKNOWN_USER);
					BotUtilities.message(update, "You've been successfully removed.");
					break;
				}
				handlerMap.put(new Long(chatId), PossibleSteps.DEFAULT);
				BotUtilities.message(update, "Cancelled remove.");
			break;
			case MANAGE_GROUP:
				if (message.toLowerCase().equals("new")) {
					handlerMap.put(new Long(chatId), PossibleSteps.WAITING_FOR_GROUPNAME);
					BotUtilities.message(update, "Please enter the name for the new Group:");
					break;
				}
				if (message.toLowerCase().equals("edit")) {
					handlerMap.put(new Long(chatId), PossibleSteps.GET_GROUP_NAME);
					BotUtilities.message(update, "Please enter the groupname you want to edit:");
					break;
				}
				handlerMap.put(new Long(chatId), PossibleSteps.MANAGE_GROUP);
				BotUtilities.message(update, "Unknown Command");
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
						handlerMap.put(new Long(chatId), PossibleSteps.EDIT_GROUP);
						BotUtilities.currentlyEditing = body.getKey();
						BotUtilities.message(update, "Now editing the Group " + message
								+ "! Type in: \"rename\" , \"addUser\", \"getUsers\", \"removeUser\" , \"delete\" ");
						break s;

					}
				}
				handlerMap.put(new Long(chatId), PossibleSteps.GET_GROUP_NAME);
				BotUtilities.message(update, "Didn't find the Group " + message + ". Please enter another Groupname:");
			break;
			case EDIT_GROUP:
				if (message.toLowerCase().equals("rename")) {
					handlerMap.put(new Long(chatId), PossibleSteps.NEW_GROUP_NAME);
					BotUtilities.message(update, "Please enter the new groupname:");
					break;
				}
				if (message.toLowerCase().equals("adduser")) {
					handlerMap.put(new Long(chatId), PossibleSteps.ADD_USER_TO_GROUP);
					BotUtilities.message(update, "Please enter the user to add:");
					break;
				}
				if (message.toLowerCase().equals("delete")) {
					handlerMap.put(new Long(chatId), PossibleSteps.CONFIRM_REMOVE_GROUP);
					BotUtilities.message(update,
							"Do You really want to remove the Group " + ((Group) Main.dm.getBodys().get(BotUtilities.currentlyEditing)).getName()
									+ " ? Type \"Yes\" to proceed");
					break;
				}
				if (message.toLowerCase().equals("removeuser")) {
					handlerMap.put(new Long(chatId), PossibleSteps.REMOVE_USER_FROM_GROUP);
					BotUtilities.message(update, "Please enter the name of the user you want to remove:");
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
					handlerMap.put(new Long(chatId), PossibleSteps.EDIT_GROUP);
					BotUtilities.message(update, usersToReturn);
					break;
				}
				if (message.toLowerCase().equals("addevent")) {
					handlerMap.put(new Long(chatId), PossibleSteps.ADD_EVENT_TO_GROUP);
					BotUtilities.message(update, "Name of the Event:");
					break;
				}
				if (message.toLowerCase().equals("removeevent")) {
					handlerMap.put(new Long(chatId), PossibleSteps.REMOVE_EVENT_FROM_GROUP);
					BotUtilities.message(update, "Name of the Event:");
					break;
				}
				if (message.toLowerCase().equals("addtodo")) {
					handlerMap.put(new Long(chatId), PossibleSteps.ADD_TODO_TO_GROUP);
					BotUtilities.message(update, "Name of the ToDo:");
					break;
				}
				if (message.toLowerCase().equals("removetodo")) {
					handlerMap.put(new Long(chatId), PossibleSteps.REMOVE_TODO_FROM_GROUP);
					BotUtilities.message(update, "Name of the ToDo:");
					break;
				}
				handlerMap.put(new Long(chatId), PossibleSteps.EDIT_GROUP);
				BotUtilities.message(update, "Please Type in: \"rename\" , \"addUser\", \"getUsers\", \"removeUser\" , \"delete\"");
			break;
			case ADD_EVENT_TO_GROUP:
				for (Map.Entry<Long, Event> event : Main.dm.getEvents().entrySet()) {
					if (event.getValue().getName().equals(message)) {
						Logic.addEventToBody(Main.dm.getGroup(BotUtilities.currentlyEditing), event.getKey());
						handlerMap.put(new Long(chatId), PossibleSteps.EDIT_GROUP);
						BotUtilities.message(update, "Event added successfully");
						break s;
					}
				}
				handlerMap.put(new Long(chatId), PossibleSteps.EDIT_GROUP);
				BotUtilities.message(update, "Event couldn't be added");
			break;
			case REMOVE_EVENT_FROM_GROUP:
				for (Map.Entry<Long, Event> event : Main.dm.getEvents().entrySet()) {
					if (event.getValue().getName().equals(message)) {
						Logic.removeEventfromBody(Main.dm.getGroup(BotUtilities.currentlyEditing), event.getKey());
						handlerMap.put(new Long(chatId), PossibleSteps.EDIT_GROUP);
						BotUtilities.message(update, "Event removed successfully");
						break s;
					}
				}
				handlerMap.put(new Long(chatId), PossibleSteps.EDIT_GROUP);
				BotUtilities.message(update, "Event couln't be removed");
			break;
			case ADD_EVENT_TO_USER:
				for (Map.Entry<Long, Event> event : Main.dm.getEvents().entrySet()) {
					if (event.getValue().getName().equals(message)) {
						Logic.addEventToBody(Main.dm.getUser(chatId), event.getKey());
						handlerMap.put(new Long(chatId), PossibleSteps.DEFAULT);
						BotUtilities.message(update, "Event added successfully");
						break s;
					}
				}
				handlerMap.put(new Long(chatId), PossibleSteps.DEFAULT);
				BotUtilities.message(update, "Event wasen't added successfully");
			break;
			case REMOVE_EVENT_FROM_USER:
				for (Map.Entry<Long, Event> event : Main.dm.getEvents().entrySet()) {
					if (event.getValue().getName().equals(message)) {
						Logic.removeEventfromBody(Main.dm.getUser(chatId), event.getKey());
						handlerMap.put(new Long(chatId), PossibleSteps.DEFAULT);
						BotUtilities.message(update, "Event removed successfully");
						break s;
					}
				}
				handlerMap.put(new Long(chatId), PossibleSteps.DEFAULT);
				BotUtilities.message(update, "Event couln't be removed");
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
				handlerMap.put(new Long(chatId), PossibleSteps.ADD_USER_TO_GROUP);
				BotUtilities.message(update, "Couldn't add user!");
			break;
			case ADD_TODO_TO_GROUP:
				for (Map.Entry<Long, ToDo> todo : Main.dm.getToDos().entrySet()) {
					if (todo.getValue().getName().equals(message)) {
						Logic.addToDoToBody(Main.dm.getGroup(BotUtilities.currentlyEditing), todo.getKey());
						handlerMap.put(new Long(chatId), PossibleSteps.EDIT_GROUP);
						BotUtilities.message(update, "ToDo added successfully");
						break s;
					}
				}
				handlerMap.put(new Long(chatId), PossibleSteps.EDIT_GROUP);
				BotUtilities.message(update, "ToDo wasen't added!");
			break;
			case REMOVE_TODO_FROM_GROUP:
				for (Map.Entry<Long, ToDo> todo : Main.dm.getToDos().entrySet()) {
					if (todo.getValue().getName().equals(message)) {
						Logic.removeToDofromBody(Main.dm.getGroup(BotUtilities.currentlyEditing), todo.getKey());
						handlerMap.put(new Long(chatId), PossibleSteps.EDIT_GROUP);
						BotUtilities.message(update, "ToDo removed successfully");
						break s;
					}
				}
				handlerMap.put(new Long(chatId), PossibleSteps.EDIT_GROUP);
				BotUtilities.message(update, "remove Failed");
			break;
			case ADD_TODO_TO_USER:
				for (Map.Entry<Long, ToDo> todo : Main.dm.getToDos().entrySet()) {
					if (todo.getValue().getName().equals(message)) {
						Logic.addToDoToBody(Main.dm.getUser(chatId), todo.getKey());
						handlerMap.put(new Long(chatId), PossibleSteps.DEFAULT);
						BotUtilities.message(update, "ToDo added successfully");
						break s;
					}
				}
				handlerMap.put(new Long(chatId), PossibleSteps.DEFAULT);
				BotUtilities.message(update, "ToDo wasen't added!");
			break;
			case REMOVE_TODO_FROM_USER:
				for (Map.Entry<Long, ToDo> todo : Main.dm.getToDos().entrySet()) {
					if (todo.getValue().getName().equals(message)) {
						Logic.removeToDofromBody(Main.dm.getUser(chatId), todo.getKey());
						handlerMap.put(new Long(chatId), PossibleSteps.DEFAULT);
						BotUtilities.message(update, "ToDo removed successfully");
						break s;
					}
				}
				handlerMap.put(new Long(chatId), PossibleSteps.DEFAULT);
				BotUtilities.message(update, "Remove failed!");
			break;
			case CONFIRM_REMOVE_GROUP:
				if (message.toLowerCase().equals("yes")) {
					Logic.removeBody(BotUtilities.currentlyEditing);
					Main.mainLog.log((BotUtilities.currentlyEditing.toString()), Log.DEBUG);
					handlerMap.put(new Long(chatId), PossibleSteps.DEFAULT);
					BotUtilities.message(update, "The Group has been successfully removed.");
					BotUtilities.currentlyEditing = null;
				}
				else {
					handlerMap.put(new Long(chatId), PossibleSteps.EDIT_GROUP);
					BotUtilities.message(update, "Remove cancelled.");
				}
			break;
			case NEW_GROUP_NAME:
				Body groupToEdit = Main.dm.getBodys().get(BotUtilities.currentlyEditing);
				groupToEdit.setName(message);
				Logic.setBody(groupToEdit);
				BotUtilities.currentlyEditing = null;
				handlerMap.put(new Long(chatId), PossibleSteps.DEFAULT);
				BotUtilities.message(update, "rename successfull");
			break;
			case REMOVE_USER_FROM_GROUP:
				for (Map.Entry<Long, Body> body : Main.dm.getBodys().entrySet()) {
					if (body.getValue().getName().equals(message)) {
						Logic.removeBodyFromGroup(body.getValue(), BotUtilities.currentlyEditing.longValue());
						handlerMap.put(new Long(chatId), PossibleSteps.EDIT_GROUP);
						BotUtilities.message(update, "Remove successfull.");
						break s;
					}
				}
				handlerMap.put(new Long(chatId), PossibleSteps.REMOVE_USER_FROM_GROUP);
				BotUtilities.message(update, "Remove failed.");
			break;

			//EVENTS

			case MANAGE_EVENT:
				if (message.toLowerCase().equals("new")) {
					handlerMap.put(new Long(chatId), PossibleSteps.WAITING_FOR_EVENT_NAME);
					BotUtilities.message(update, "Please enter a name for the new Event:");
					break;
				}
				if (message.toLowerCase().equals("showinfo")) {
					handlerMap.put(new Long(chatId), PossibleSteps.WAITING_FOR_EVENT_NAME2);
					BotUtilities.message(update, "Please enter the eventname:");
					break;
				}
				if (message.toLowerCase().equals("edit")) {
					handlerMap.put(new Long(chatId), PossibleSteps.WAITING_FOR_EVENT_NAME3);
					BotUtilities.message(update, "Please enter the eventname:");
					break;
				}
			break;

			case WAITING_FOR_EVENT_NAME:
				for (Map.Entry<Long, Event> event : Main.dm.getEvents().entrySet()) {
					if (event.getValue().getName().equals(message)) {
						handlerMap.put(new Long(chatId), PossibleSteps.WAITING_FOR_EVENT_NAME);
						BotUtilities.message(update,
								"This name is already in use. Please chose another one:");
						break s;
					}
				}
				BotUtilities.currentEvent = Logic.createEvent(message);
				handlerMap.put(new Long(chatId), PossibleSteps.ADD_INFO_TO_EVENT);
				BotUtilities.message(update, "The Event " + message + " has been added.");
			break;
			case WAITING_FOR_EVENT_NAME2:
				String info = "No info availible";
				for (Map.Entry<Long, Event> event : Main.dm.getEvents().entrySet()) {
					if (event.getValue().getName().equals(message)) {
						Event tmp_currentEvent = event.getValue();
						info = "Name: " + tmp_currentEvent.getName() + "\nDate: " + tmp_currentEvent.getDate().toString() + "\nLocation: "
								+ tmp_currentEvent.getLocation() + "\nDescription: " + tmp_currentEvent.getDescription();
						handlerMap.put(new Long(chatId), PossibleSteps.DEFAULT);
						BotUtilities.message(update, info);
						break s;
					}
				}
				handlerMap.put(new Long(chatId), PossibleSteps.DEFAULT);
				BotUtilities.message(update, info);
			break;
			case WAITING_FOR_EVENT_NAME3:
				for (Map.Entry<Long, Event> event : Main.dm.getEvents().entrySet()) {
					if (event.getValue().getName().equals(message)) {
						handlerMap.put(new Long(chatId), PossibleSteps.ADD_INFO_TO_EVENT);
						BotUtilities.currentEvent = event.getValue();
						break s;
					}
				}
			break;
			case ADD_INFO_TO_EVENT:
				if (message.toLowerCase().equals("editdescription")) {
					handlerMap.put(new Long(chatId), PossibleSteps.EVENT_ADD_DESCRIPTION);
					BotUtilities.message(update, "Please enter your description:");
					break;
				}
				if (message.toLowerCase().equals("editlocation")) {
					handlerMap.put(new Long(chatId), PossibleSteps.EVENT_ADD_LOCATION);
					BotUtilities.message(update, "Please enter your location:");
					break;
				}
				if (message.toLowerCase().equals("editdate")) {
					handlerMap.put(new Long(chatId), PossibleSteps.EVENT_ADD_DATE);
					BotUtilities.message(update, "Please enter your date(dd/MM/yyyy):");
					break;
				}
				if (message.toLowerCase().equals("editname")) {
					handlerMap.put(new Long(chatId), PossibleSteps.EVENT_EDIT_NAME);
					BotUtilities.message(update, "Please enter your new Name:");
					break;
				}
				if (message.toLowerCase().equals("done")) {
					Logic.addEvent(BotUtilities.currentEvent);
					handlerMap.put(new Long(chatId), PossibleSteps.DEFAULT);
					BotUtilities.currentEvent = null;
					BotUtilities.message(update, "Added Event");
					break;
				}
			break;
			case EVENT_ADD_DESCRIPTION:
				handlerMap.put(new Long(chatId), PossibleSteps.ADD_INFO_TO_EVENT);
				BotUtilities.currentEvent.setDescription(message);
				BotUtilities.message(update, "Added Description");
			break;
			case EVENT_ADD_LOCATION:
				handlerMap.put(new Long(chatId), PossibleSteps.ADD_INFO_TO_EVENT);
				BotUtilities.currentEvent.setLocation(message);
				BotUtilities.message(update, "Added location");
			break;
			case EVENT_ADD_DATE:
				try {
					BotUtilities.currentEvent.setDate(new SimpleDateFormat("dd/MM/yyyy").parse(message));
				} catch (ParseException e) {
					e.printStackTrace();
					handlerMap.put(new Long(chatId), PossibleSteps.ADD_INFO_TO_EVENT);
					BotUtilities.message(update, "Failed! Try Again!");
					break;
				}

				handlerMap.put(new Long(chatId), PossibleSteps.ADD_INFO_TO_EVENT);
				BotUtilities.message(update, "Added Date");
			break;
			case EVENT_EDIT_NAME:
				for (Map.Entry<Long, Event> event : Main.dm.getEvents().entrySet()) {
					if (event.getValue().getName().equals(message)) {
						handlerMap.put(new Long(chatId), PossibleSteps.EVENT_EDIT_NAME);
						BotUtilities.message(update,
								"This name is already in use. Please chose another one:");
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
					handlerMap.put(new Long(chatId), PossibleSteps.WAITING_FOR_TODO_NAME);
					BotUtilities.message(update, "Please enter a name for the new ToDo:");
					break;
				}
				if (message.toLowerCase().equals("showinfo")) {
					handlerMap.put(new Long(chatId), PossibleSteps.WAITING_FOR_TODO_NAME2);
					BotUtilities.message(update, "Please enter the todoname:");
					break;
				}
				if (message.toLowerCase().equals("edit")) {
					handlerMap.put(new Long(chatId), PossibleSteps.WAITING_FOR_TODO_NAME3);
					BotUtilities.message(update, "Please enter the todoname:");
					break;
				}
			break;

			case WAITING_FOR_TODO_NAME:
				for (Map.Entry<Long, ToDo> todo : Main.dm.getToDos().entrySet()) {
					if (todo.getValue().getName().equals(message)) {
						handlerMap.put(new Long(chatId), PossibleSteps.WAITING_FOR_TODO_NAME);
						BotUtilities.message(update,
								"This name is already in use. Please chose another one:");
						break s;
					}
				}
				BotUtilities.currentTodo = Logic.createToDo(message);
				handlerMap.put(new Long(chatId), PossibleSteps.ADD_INFO_TO_TODO);
				BotUtilities.message(update, "The ToDo " + message + " has been added.");
			break;
			case WAITING_FOR_TODO_NAME2:
				String info2 = "No info availible";
				for (Map.Entry<Long, ToDo> todos : Main.dm.getToDos().entrySet()) {
					if (todos.getValue().getName().equals(message)) {
						ToDo tmp_currentTodo = todos.getValue();
						info2 = "Name: " + tmp_currentTodo.getName() + "\nDate: " + tmp_currentTodo.getDeadline() + "\nPriority: "
								+ tmp_currentTodo.getPriority() + "\nDescription: " + tmp_currentTodo.getDescription();
						handlerMap.put(new Long(chatId), PossibleSteps.DEFAULT);
						BotUtilities.message(update, info2);
						break s;
					}
				}
				handlerMap.put(new Long(chatId), PossibleSteps.DEFAULT);
				BotUtilities.message(update, info2);
			break;
			case WAITING_FOR_TODO_NAME3:
				for (Map.Entry<Long, ToDo> todo : Main.dm.getToDos().entrySet()) {
					if (todo.getValue().getName().equals(message)) {
						handlerMap.put(new Long(chatId), PossibleSteps.ADD_INFO_TO_TODO);
						BotUtilities.currentTodo = todo.getValue();
						break s;
					}
				}
			break;
			case ADD_INFO_TO_TODO:
				if (message.toLowerCase().equals("editdescription")) {
					handlerMap.put(new Long(chatId), PossibleSteps.TODO_ADD_DESCRIPTION);
					BotUtilities.message(update, "Please enter your description:");
					break;
				}
				if (message.toLowerCase().equals("editpriority")) {
					handlerMap.put(new Long(chatId), PossibleSteps.TODO_ADD_PRIORITY);
					BotUtilities.message(update, "Please enter your priority:");
					break;
				}
				if (message.toLowerCase().equals("editdeadline")) {
					handlerMap.put(new Long(chatId), PossibleSteps.TODO_ADD_DATE);
					BotUtilities.message(update, "Please enter the deadline(dd/MM/yyyy):");
					break;
				}
				if (message.toLowerCase().equals("editname")) {
					handlerMap.put(new Long(chatId), PossibleSteps.TODO_EDIT_NAME);
					BotUtilities.message(update, "Please enter your new Name:");
					break;
				}
				if (message.toLowerCase().equals("done")) {
					Logic.addToDo(BotUtilities.currentTodo);
					BotUtilities.currentTodo = null;
					handlerMap.put(new Long(chatId), PossibleSteps.DEFAULT);
					BotUtilities.message(update, "Added ToDo");
					break;
				}
			break;
			case TODO_ADD_DESCRIPTION:
				BotUtilities.currentTodo.setDescription(message);
				handlerMap.put(new Long(chatId), PossibleSteps.ADD_INFO_TO_TODO);
				BotUtilities.message(update, "Added Description");
			break;
			case TODO_ADD_PRIORITY:
				try {
					BotUtilities.currentTodo.setPriority(Integer.parseInt(message));
				} catch (NumberFormatException e1) {
					handlerMap.put(new Long(chatId), PossibleSteps.TODO_ADD_PRIORITY);
					BotUtilities.message(update, "Please enter a number!");
					e1.printStackTrace();
					break;
				}
				handlerMap.put(new Long(chatId), PossibleSteps.ADD_INFO_TO_TODO);
				BotUtilities.message(update, "Added priority");
			break;
			case TODO_ADD_DATE:
				try {
					BotUtilities.currentTodo.setDeadline(new SimpleDateFormat("dd/MM/yyyy").parse(message));
				} catch (ParseException e) {
					e.printStackTrace();
					handlerMap.put(new Long(chatId), PossibleSteps.ADD_INFO_TO_TODO);
					BotUtilities.message(update, "Failed! Try Again!");
					break;
				}
				handlerMap.put(new Long(chatId), PossibleSteps.ADD_INFO_TO_TODO);
				BotUtilities.message(update, "Added Deadline");
			break;
			case TODO_EDIT_NAME:
				for (Map.Entry<Long, ToDo> todo : Main.dm.getToDos().entrySet()) {
					if (todo.getValue().getName().equals(message)) {
						handlerMap.put(new Long(chatId), PossibleSteps.TODO_EDIT_NAME);
						BotUtilities.message(update,
								"This name is already in use. Please chose another one:");
						break s;
					}
				}
				BotUtilities.currentTodo.setName(message);
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
				.setText(bot_message)
				.setReplyMarkup(Main.mainBot.handler.handlerMap.get(update.getMessage().getChatId()).getKeyBoard());

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
				.setText(bot_message)
				.setReplyMarkup(Main.mainBot.handler.handlerMap.get(userId).getKeyBoard());

		try {
			Main.mainBot.sendMessage(message); // Call method to send the message
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}
}
