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

import java.util.Date;

import de.pinkTigers.tgTeamComBot.bot.PossbileActions;

/**
 * A event that is only triggered once.
 * 
 * @author Tim Neumann, Fabian Hutzenlaub, Patrick Muerdter
 */
public class OneTimeEvent extends SchedulerEvent {

	/**
	 * generated serial version ID
	 */
	private static final long serialVersionUID = -4579888160413257137L;

	/** The Date to trigger at. */
	private Date theDate;

	/**
	 * Empty default constructor for deserializing only.
	 */
	public OneTimeEvent() {

	}

	/**
	 * @param p_theDate
	 *            The date that this event should be triggered.
	 * @param p_chatId
	 *            The chat Id (User ID) for whom this event is.
	 * @param p_action
	 *            The action to be triggered.
	 * @see de.pinkTigers.tgTeamComBot.scheduler.SchedulerEvent#SchedulerEvent(Long,
	 *      de.pinkTigers.tgTeamComBot.bot.PossbileActions)
	 */
	public OneTimeEvent(Long p_chatId, PossbileActions p_action, Date p_theDate) {
		super(p_chatId, p_action);
		this.theDate = (Date) p_theDate.clone();
	}

	/**
	 * @param orig
	 *            The original Event
	 * @see de.pinkTigers.tgTeamComBot.scheduler.SchedulerEvent#SchedulerEvent(SchedulerEvent)
	 */
	public OneTimeEvent(OneTimeEvent orig) {
		super(orig);
		this.theDate = (Date) orig.theDate.clone();
	}

	/**
	 * @see de.pinkTigers.tgTeamComBot.scheduler.SchedulerEvent#getNextOccurence()
	 */
	@Override
	public Date getNextOccurence() {
		return (Date) this.theDate.clone();
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof OneTimeEvent)) return super.equals(obj);
		OneTimeEvent e = (OneTimeEvent) obj;
		return e.theDate.equals(this.theDate) && super.equals(obj);
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return this.theDate.hashCode() + super.hashCode();
	}

}
