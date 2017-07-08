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
	EDIT_GROUP, ADD_USER_TO_GROUP, CONFIRM_REMOVE_GROUP, NEW_GROUP_NAME
}
