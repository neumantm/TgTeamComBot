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

import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;

/**
 * TODO: Description
 * 
 * @author Tim Neumann, Fabian Hutzenlaub, Patrick Muerdter
 */
public enum PossibleSteps {
	DEFAULT(KeyLayouts.DEFAULT),
	UNKNOWN_USER(KeyLayouts.UNKNOWN_USER),
	UU_JOIN_ASKED_NAME(KeyLayouts.TEXT_INPUT),
	WAITING_FOR_TOKEN(KeyLayouts.TEXT_INPUT),
	CONFIRM_REMOVE_USER(KeyLayouts.YES_NO),
	MANAGE_GROUP(KeyLayouts.NEW_EDIT),
	WAITING_FOR_GROUPNAME(KeyLayouts.TEXT_INPUT),
	GET_GROUP_NAME(KeyLayouts.TEXT_INPUT),
	EDIT_GROUP(KeyLayouts.EDIT_GROUP),
	ADD_USER_TO_GROUP(KeyLayouts.TEXT_INPUT),
	CONFIRM_REMOVE_GROUP(KeyLayouts.YES_NO),
	NEW_GROUP_NAME(KeyLayouts.TEXT_INPUT),
	REMOVE_USER_FROM_GROUP(KeyLayouts.TEXT_INPUT),
	MANAGE_EVENT(KeyLayouts.NEW_EDIT_INFO),
	WAITING_FOR_EVENT_NAME(KeyLayouts.TEXT_INPUT),
	ADD_INFO_TO_EVENT(KeyLayouts.TEXT_INPUT),
	EVENT_ADD_LOCATION(KeyLayouts.TEXT_INPUT),
	EVENT_ADD_DATE(KeyLayouts.TEXT_INPUT),
	WAITING_FOR_EVENT_NAME2(KeyLayouts.TEXT_INPUT),
	WAITING_FOR_EVENT_NAME3(KeyLayouts.TEXT_INPUT),
	EVENT_EDIT_NAME(KeyLayouts.TEXT_INPUT),
	MANAGE_TODO(KeyLayouts.NEW_EDIT_INFO),
	WAITING_FOR_TODO_NAME(KeyLayouts.TEXT_INPUT),
	WAITING_FOR_TODO_NAME2(KeyLayouts.TEXT_INPUT),
	WAITING_FOR_TODO_NAME3(KeyLayouts.TEXT_INPUT),
	ADD_INFO_TO_TODO(KeyLayouts.TEXT_INPUT),
	TODO_ADD_DESCRIPTION(KeyLayouts.TEXT_INPUT),
	TODO_ADD_PRIORITY(KeyLayouts.TEXT_INPUT),
	TODO_ADD_DATE(KeyLayouts.TEXT_INPUT),
	TODO_EDIT_NAME(KeyLayouts.TEXT_INPUT),
	EVENT_ADD_DESCRIPTION(KeyLayouts.TEXT_INPUT),
	ADD_EVENT_TO_GROUP(KeyLayouts.TEXT_INPUT),
	REMOVE_EVENT_FROM_GROUP(KeyLayouts.TEXT_INPUT),
	ADD_EVENT_TO_USER(KeyLayouts.TEXT_INPUT),
	REMOVE_EVENT_FROM_USER(KeyLayouts.TEXT_INPUT),
	ADD_TODO_TO_GROUP(KeyLayouts.TEXT_INPUT),
	REMOVE_TODO_FROM_GROUP(KeyLayouts.TEXT_INPUT),
	REMOVE_TODO_FROM_USER(KeyLayouts.TEXT_INPUT),
	ADD_TODO_TO_USER(KeyLayouts.TEXT_INPUT);

	private KeyLayouts keyboardLayout;

	private PossibleSteps(KeyLayouts kl) {
		this.keyboardLayout = kl;
	}

	/**
	 * @return The Reply Keyboard Markup
	 */
	public ReplyKeyboardMarkup getKeyBoard() {
		return this.keyboardLayout.getKeyboard();
	}

}
