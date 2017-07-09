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
	 * @param p_chatId
	 *            The chat Id (User ID) for whom this event is.
	 * @param p_action
	 *            The action to be triggered.
	 */
	public RegularEvent(Long p_chatId, PossbileActions p_action, Date p_nextDate, long p_interval) {
		super(p_chatId, p_action);
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
			long dist = new Date().getTime() - this.nextDate.getTime();
			long times = (long) ((double) dist / (double) this.interval);
			if (times < 1) {
				times = 1;
			}
			this.nextDate = new Date(this.nextDate.getTime() + (this.interval * times));
		}
		return (Date) this.nextDate.clone();
	}

	/**
	 * Get's {@link #interval interval}
	 * 
	 * @return interval
	 */
	public long getInterval() {
		return this.interval;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof RegularEvent)) return super.equals(obj);
		RegularEvent e = (RegularEvent) obj;
		return e.interval == this.interval && e.nextDate.equals(this.nextDate) && super.equals(obj);
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return (int) (this.interval + this.nextDate.hashCode() + super.hashCode());
	}

}
