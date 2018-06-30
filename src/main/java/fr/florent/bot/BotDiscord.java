package main.java.fr.florent.bot;

import java.io.IOException;
import java.util.Scanner;
import javax.security.auth.login.LoginException;
import main.java.fr.florent.bot.commands.CommandMap;
import main.java.fr.florent.bot.events.BotListener;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;

public class BotDiscord implements Runnable {
	
	private String TOKEN;
	private final JDA jda;
	private final CommandMap commandmap = new CommandMap(this);
	private boolean running;
	private final Scanner scanner = new Scanner(System.in);
	
	public BotDiscord(String token) throws LoginException, InterruptedException, IOException{
		this.TOKEN = token;
		jda = new JDABuilder(AccountType.BOT).setToken(TOKEN).setBulkDeleteSplittingEnabled(false).buildBlocking();
		getJda().addEventListener(new BotListener(commandmap));
		System.out.println("Nexabot est connecté !");
	}

	@Override
	public void run() {
		running = true;
		
		while(running){
			if(scanner.hasNextLine()){
				commandmap.commandConsole(scanner.nextLine());
			}
		}
		
		scanner.close();
		System.out.println("Bot stopped.");
		getJda().shutdown();
		System.exit(0);
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public JDA getJda() {	
		return jda;
	}
	
	public CommandMap getCommandMap(){
		return commandmap;
	}

}
