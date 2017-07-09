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
	 * @see de.pinkTigers.tgTeamComBot.scheduler.SchedulerEvent#SchedulerEvent(Long,
	 *      de.pinkTigers.tgTeamComBot.bot.PossbileActions)
	 */
	public OneTimeEvent(Date p_theDate) {
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

}
