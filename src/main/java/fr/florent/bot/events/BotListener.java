package main.java.fr.florent.bot.events;

import main.java.fr.florent.bot.commands.CommandMap;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.EventListener;

public class BotListener implements EventListener {

	private final CommandMap commandmap;

	public BotListener(CommandMap commandmap) {
		this.commandmap = commandmap;
	}

	@Override
	public void onEvent(Event e) {

		if (e instanceof MessageReceivedEvent) {
			onMessage((MessageReceivedEvent) e);
		}
	}

	private void onMessage(MessageReceivedEvent e) {
		if (!e.getAuthor().equals(e.getJDA().getSelfUser())) {

			String message = e.getMessage().getContentDisplay();

			if (message.startsWith(commandmap.getTag())) {
				message = message.substring(1);

				if (commandmap.commandUser(e.getAuthor(), message, e.getMessage())) {
					if (e.getTextChannel() != null
							&& e.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_MANAGE)) {
						e.getMessage().delete().queue();
					}
				}
			}

		}
	}

}
