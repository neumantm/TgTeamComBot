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
import java.util.List;

import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;

/**
 * TODO: Description
 * 
 * @author Tim Neumann, Fabian Hutzenlaub, Patrick Muerdter
 */
public enum KeyLayouts {
	TEXT_INPUT(""),
	DEFAULT("addUser,removeUser,editGroup", "showGroups, manageEvents, manageToDos", "addEvent, removeEvent, addToDo", "removeToDo, getEvents, getToDos", "cancel"),
	UNKNOWN_USER("join, help", "cancel"),
	YES_NO("yes, no", "cancel"),
	NEW_EDIT("new, edit", "cancel"),
	EDIT_GROUP("rename, addUser, delete", "removeUser, getUser, addEvent", "removeEvent, addToDo, removeToDo", "cancel"),
	NEW_EDIT_INFO("new, edit, showinfo", "cancel"),
	ADD_INFO_EVENT("editDescription, editLocation, editDate", "editName, done", "cancel"),
	ADD_INFO_TODO("editDescription, editPriority, editDeadline", "editName, done", "cancel");

	private List<KeyboardRow> keyboardRows = new ArrayList<>();

	private KeyLayouts(String... rows) {
		if (rows.length == 1 && rows[0].length() == 0) {
			this.keyboardRows = null;
		}
		else {
			for (String row : rows) {
				KeyboardRow kbRow = new KeyboardRow();
				for (String cell : row.split(",")) {
					kbRow.add(cell);
				}
				this.keyboardRows.add(kbRow);
			}
		}
	}

	/**
	 * @return The keyboard list.
	 */
	public ReplyKeyboardMarkup getKeyboard() {
		ReplyKeyboardMarkup rep = new ReplyKeyboardMarkup();
		if (this.keyboardRows != null) {
			rep.setKeyboard(this.keyboardRows);
		}
		return rep;
	}

}
