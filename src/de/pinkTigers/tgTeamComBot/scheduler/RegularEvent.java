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
 * A repeating scheduler event
 * 
 * @author Tim Neumann, Fabian Hutzenlaub, Patrick Muerdter
 */
public class RegularEvent extends SchedulerEvent {

	/**
	 * generated serial version ID
	 */
	private static final long serialVersionUID = 1377816481254008117L;
	/** The next date to occur. */
	private Date nextDate;
	/** The interval to occur in. */
	private long interval;

	/**
	 * Empty default constructor for deserializing only.
	 */
	public RegularEvent() {

	}

	/**
	 * Creates a new Regular Event
	 * 
	 * @param p_nextDate
	 *            The next date
	 * @param p_interval
	 *            The interval
	 */
	public RegularEvent(Date p_nextDate, long p_interval) {
		this.nextDate = (Date) p_nextDate.clone();
		this.interval = p_interval;
	}

	/**
	 * Creates a new Regular Event from orig event
	 * 
	 * @param orig
	 *            The original Event
	 * @see de.pinkTigers.tgTeamComBot.scheduler.SchedulerEvent#SchedulerEvent(SchedulerEvent)
	 */
	public RegularEvent(RegularEvent orig) {
		super(orig);
		this.nextDate = (Date) orig.nextDate.clone();
		this.interval = orig.interval;
	}

	/**
	 * @see de.pinkTigers.tgTeamComBot.scheduler.SchedulerEvent#getNextOccurence()
	 */
	@Override
	public Date getNextOccurence() {
		while (this.nextDate.after(new Date())) {
			this.nextDate = new Date(this.nextDate.getTime() + this.interval);
		}
		return (Date) this.nextDate.clone();
	}

}
