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
import java.util.Timer;
import java.util.TimerTask;

import de.pinkTigers.tgTeamComBot.Main;
import de.pinkTigers.tgTeamComBot.bot.PossbileActions;

/**
 * TODO: Description
 * 
 * @author Tim Neumann, Fabian Hutzenlaub, Patrick Muerdter
 */
public class Scheduler {

	private static Timer timer;

	private static void startTimer(OneTimeEvent e) {
		Scheduler.timer.schedule(new TimerTask() {

			@Override
			public void run() {
				//TODO run method with e.getAction() and e.getChatId();
				Main.dm.removeSchedulerEvent(e);
			}
		}, e.getNextOccurence());

	}

	private static void startTimer(RegularEvent e) {
		Scheduler.timer.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				//TODO run method with e.getAction() and e.getChatId();
				Main.dm.removeSchedulerEvent(e);
				e.getNextOccurence();
				Main.dm.addSchedulerEvent(e);
			}
		}, e.getNextOccurence(), e.getInterval() * 1000);

	}

	/**
	 * Starts all the timers that are defined in data manager. Only data manager
	 * should call this after launch.
	 */
	public static void startTimers() {
		for (SchedulerEvent e : Main.dm.getSchedulerEvents()) {
			if (e instanceof OneTimeEvent) {
				Scheduler.startTimer((OneTimeEvent) e);
			}
			else if (e instanceof RegularEvent) {
				Scheduler.startTimer((RegularEvent) e);
			}
		}
	}

	/**
	 * Add a One Time scheduled Action
	 * 
	 * @param chatId
	 *            The chat id for whom this timer.
	 * @param action
	 *            The action to trigger.
	 * @param time
	 *            The date object when it should trigger.
	 */
	public static void addOneTimeAction(Long chatId, PossbileActions action, Date time) {
		OneTimeEvent e = new OneTimeEvent(chatId, action, time);
		Main.dm.addSchedulerEvent(e);
		Scheduler.startTimer(e);
	}

	/**
	 * Add a One Time scheduled Action
	 * 
	 * @param chatId
	 *            The chat id for whom this timer.
	 * @param action
	 *            The action to trigger.
	 * @param firstTime
	 *            The date object when it should trigger first.
	 * @param interval
	 *            The interval in seconds in that it should trigger.
	 */
	public static void addRegularAction(Long chatId, PossbileActions action, Date firstTime, long interval) {
		RegularEvent e = new RegularEvent(chatId, action, firstTime, interval);
		Main.dm.addSchedulerEvent(e);
		Scheduler.startTimer(e);
	}

}
