package main.java.fr.florent.bot.commands;

import main.java.fr.florent.bot.BotDiscord;
import main.java.fr.florent.bot.commands.Command.ExecutorType;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.managers.GuildController;

public class Commands {

	private BotDiscord botdiscord;
	public Role[] roles = new Role[3];

	public Commands(BotDiscord botdiscord) {
		this.botdiscord = botdiscord;
		
	}
	
	@Command(name = "test1", description = "Pour rejoindre le grade test1.", type = ExecutorType.USER)
	public void onTest1(User user, TextChannel channel) {
		executeRankCommand(user, channel, 1);
	}
	
	@Command(name = "test2", description = "Pour rejoindre le grade test2.", type = ExecutorType.USER)
	public void onTest2(User user, TextChannel channel) {
		executeRankCommand(user, channel, 2);
	}
	
	@Command(name = "hameau", description = "Pour rejoindre le grade membre du hameau.", type = ExecutorType.USER)
	public void onHameau(User user, TextChannel channel) {
		executeRankCommand(user, channel, 0);
	}
	
	public void executeRankCommand(User user, TextChannel channel, int i){
		Guild guild = channel.getGuild();
		roles[0] = guild.getRolesByName("Membres du hameau", true).get(0);
		roles[1] = guild.getRolesByName("Test1", true).get(0);
		roles[2] = guild.getRolesByName("Test2", true).get(0);
		Member member = guild.getMember(user);
		
		if(member.getRoles().contains(roles[0]) || member.getRoles().contains(roles[1]) || member.getRoles().contains(roles[2])){
			this.sendPrivateMessage(user, user.getAsMention() + " Vous possédez déjà un rôle. "
					+ "Impossible de le modifier vous-même. Contactez quelqu'un qui peut le faire pour vous.");
		} else {
			GuildController gc = new GuildController(guild);
			gc.addSingleRoleToMember(member, roles[i]).queue();
			channel.sendMessage(
					user.getAsMention() + " a désormais le rôle suivant : " + roles[i].getName() + ".").queue();
		}
	}

	@Command(name = "vote", description = "Permet de voter pour un ou plusieurs candidats.", type = ExecutorType.USER)
	public void onVote(User user, String[] args) {

		if (args.length < 1) {
			this.sendPrivateMessage(user, user.getAsMention() + " Vous devez renseigner au moins un candidat.");
		} else {
			MessageChannel voteChannel = botdiscord.getJda().getTextChannelsByName("vote", true).get(0);

			if (voteChannel != null) {
				voteChannel
						.sendMessage(
								user.getName() + " approuve le(s) candidat(s) suivant(s) : " + this.toStringArgs(args) + ".")
						.queue();
				this.sendPrivateMessage(user, user.getAsMention() + " Votre vote a bien été enregistré.");
			} else {
				this.sendPrivateMessage(user, user.getAsMention()
						+ " Il n'y a pas de channel vote sur ce serveur, vous ne pouvez pas voter.");
			}
		}

	}

	public String toStringArgs(String[] args) {

		String result = new String("");

		for (String arg : args) {
			result = new String(result + arg + ", ");
		}
		if (result.length()-2 >= 1) {
			result = result.substring(0, result.length() - 2);
			return result;
		} else {
			return "";
		}

	}

	@Command(name = "help", description = "Montre les commandes du bot.", type = ExecutorType.ALL)
	public void onHelp(User user, MessageChannel channel) {
		String output = new String(user.getAsMention() + " Les commandes disponibles sont : \n");

		for (SimpleCommand cmd : botdiscord.getCommandMap().getCommands()) {
			output = new String(output + "!" + cmd.getName() + " : " + cmd.getDescription() + "\n");
		}

		this.sendPrivateMessage(user, output);
	}

	@Command(name = "info", description = "Donne votre nom et votre channel.", type = ExecutorType.ALL)
	public void onInfo(User user, MessageChannel channel) {
		channel.sendMessage(
				user.getAsMention() + " a tapé la commande !info dans le channel " + channel.getName() + ".").queue();
	}

	@Command(name = "stop", description = "Arrête le bot.", type = ExecutorType.CONSOLE)
	public void onStop() {
		botdiscord.setRunning(false);
	}

	public void sendPrivateMessage(User user, String content) {
		// openPrivateChannel provides a RestAction<PrivateChannel>
		// which means it supplies you with the resulting channel
		user.openPrivateChannel().queue((channel) -> {
			channel.sendMessage(content).queue();
		});
	}

}
