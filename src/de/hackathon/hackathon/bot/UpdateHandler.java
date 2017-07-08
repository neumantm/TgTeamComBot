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
package de.hackathon.hackathon.bot;

import java.util.HashMap;

import org.telegram.telegrambots.api.objects.Update;

import de.hackathon.hackathon.Main;

/**
 * TODO: Description
 * 
 * @author Tim Neumann, Fabian Hutzenlaub, Patrick Muerdter
 */
public class UpdateHandler {

	/**
	 * handlermap
	 */
	protected HashMap<Long, PossibleSteps> handlerMap = new HashMap<>();

	/**
	 * @param update
	 *            update
	 */
	public void newUpdate(Update update) {
		Long chatId = update.getMessage().getChatId();

		if (this.handlerMap.get(chatId) == null) {

			if (Main.dm.getBodys().containsKey(chatId) || Main.isValidUser(chatId)) {
				this.handlerMap.put(chatId, PossibleSteps.DEFAULT);
			}
			else {
				this.handlerMap.put(chatId, PossibleSteps.UNKNOWN_USER);
			}

		}

		BotUtilities.doNext(update, this.handlerMap.get(chatId), chatId.longValue(), update.getMessage().getText());
	}
}
