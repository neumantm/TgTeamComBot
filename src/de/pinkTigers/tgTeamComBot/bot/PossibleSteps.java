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

/**
 * TODO: Description
 * 
 * @author Tim Neumann, Fabian Hutzenlaub, Patrick Muerdter
 */
public enum PossibleSteps {
	DEFAULT, UNKNOWN_USER, UU_JOIN_ASKED_NAME, WAITING_FOR_TOKEN, CONFIRM_REMOVE_USER, MANAGE_GROUP, WAITING_FOR_GROUPNAME, GET_GROUP_NAME,
	EDIT_GROUP, ADD_USER_TO_GROUP, CONFIRM_REMOVE_GROUP, NEW_GROUP_NAME, REMOVE_USER_FROM_GROUP, GET_USERS_FROM_GROUP, MANAGE_EVENT,
	WAITING_FOR_EVENT_NAME, ADD_INFO_TO_EVENT, TODO, EVENT_ADD_LOCATION, EVENT_ADD_DATE, WAITING_FOR_EVENT_NAME2,
	WAITING_FOR_EVENT_NAME3, EVENT_EDIT_NAME, MANAGE_TODO, WAITING_FOR_TODO_NAME, WAITING_FOR_TODO_NAME2, WAITING_FOR_TODO_NAME3, ADD_INFO_TO_TODO, 
	TODO_ADD_DESCRIPTION, TODO_ADD_PRIORITY, TODO_ADD_DATE, TODO_EDIT_NAME, EVENT_ADD_DESCRIPTION, TODO_SHOW_SORTED, 
}
