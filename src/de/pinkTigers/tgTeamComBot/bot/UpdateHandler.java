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

import java.util.HashMap;

import org.telegram.telegrambots.api.objects.Update;

import de.pinkTigers.tgTeamComBot.Main;

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

	private String help() {
		String s = "This is a Team Communications Bot for Telegram. Further help will be added.";
		return s;
	}

	/**
	 * @param update
	 *            update
	 */
	public void newUpdate(Update update) {
		if (!update.hasMessage() || !update.getMessage().hasText() || update.getMessage().getText().length() < 1) return;

		Long chatId = update.getMessage().getChatId();

		if (this.handlerMap.get(chatId) == null) {

			if (Main.dm.getBodys().containsKey(chatId) || Main.isValidUser(chatId)) {
				this.handlerMap.put(chatId, PossibleSteps.DEFAULT);
			}
			else {
				this.handlerMap.put(chatId, PossibleSteps.UNKNOWN_USER);
			}

		}

		if (update.getMessage().getText().toLowerCase().equals("cancel") && this.handlerMap.get(chatId) != PossibleSteps.UNKNOWN_USER && this.handlerMap.get(chatId) != PossibleSteps.UU_JOIN_ASKED_NAME) {
			this.handlerMap.put(chatId, PossibleSteps.DEFAULT);
			BotUtilities.message(update, "Canceled.");
			return;
		}

		if (update.getMessage().getText().toLowerCase().equals("cancel") && (this.handlerMap.get(chatId) == PossibleSteps.UNKNOWN_USER || this.handlerMap.get(chatId) == PossibleSteps.UU_JOIN_ASKED_NAME)) {
			this.handlerMap.put(chatId, PossibleSteps.UNKNOWN_USER);
			BotUtilities.message(update, "Canceled.");
			return;
		}

		if (update.getMessage().getText().toLowerCase().equals("help") || update.getMessage().getText().toLowerCase().equals("/help")) {
			BotUtilities.message(help(), chatId.longValue());
		}

		if (update.getMessage().getText().toLowerCase().equals("/start")) {
			BotUtilities.message("Welcome!", chatId.longValue());
		}

		BotUtilities.doNext(update, this.handlerMap.get(chatId), chatId.longValue(), update.getMessage().getText());
	}
}
