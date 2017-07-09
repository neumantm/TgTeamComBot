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
package de.pinkTigers.tgTeamComBot.scheduler;

import java.io.Serializable;
import java.util.Date;

import de.pinkTigers.tgTeamComBot.bot.PossbileActions;

/**
 * A generic scheduler event.
 * 
 * @author Tim Neumann, Fabian Hutzenlaub, Patrick Muerdter
 */
public abstract class SchedulerEvent implements Serializable {
	/**
	 * generated serial version ID
	 */
	private static final long serialVersionUID = -7487141532684022855L;

	private Long chatId;
	private PossbileActions action;

	/**
	 * Empty default constructor for deserializing only.
	 */
	public SchedulerEvent() {

	}

	/**
	 * Creates a new SchedulerEvent
	 * 
	 * @param p_chatId
	 *            The chat Id (User ID) for whom this event is.
	 * @param p_action
	 *            The action to be triggered.
	 */
	public SchedulerEvent(Long p_chatId, PossbileActions p_action) {
		this.chatId = p_chatId;
		this.action = p_action;

	}

	/**
	 * Constructor that copy's a original SchedulerEvent
	 * 
	 * @param orig
	 *            The SchedularEvent that all values should be copied from.
	 */
	protected SchedulerEvent(SchedulerEvent orig) {
		this.chatId = orig.chatId;
		this.action = orig.action;
	}

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

	/**
	 * Returns the next time this event should be triggered.
	 * 
	 * @return The Date, the event should be triggerd at.
	 */
	public abstract Date getNextOccurence();
}
