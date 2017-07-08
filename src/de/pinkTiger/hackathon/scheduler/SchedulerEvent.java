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
package de.pinkTiger.hackathon.scheduler;

import java.util.Date;

import de.pinkTiger.hackathon.bot.PossbileActions;

/**
 * TODO: Description
 * 
 * @author Tim Neumann, Fabian Hutzenlaub, Patrick Muerdter
 */
public abstract class SchedulerEvent {
	private Long chatId;
	private PossbileActions action;

	/**
	 * Get's {@link #chatId chatId}
	 * 
	 * @return chatId
	 */
	public Long getChatId() {
		return this.chatId;
	}

	/**
	 * Get's {@link #action action}
	 * 
	 * @return action
	 */
	public PossbileActions getAction() {
		return this.action;
	}

	public abstract Date getNextOccurence();

}
