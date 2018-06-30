package main.java.fr.florent.bot;

import java.io.File;
import java.io.IOException;
import javax.security.auth.login.LoginException;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import net.md_5.bungee.api.ChatColor;

public class Plugin extends JavaPlugin {
	
	public static File file;
	public static FileConfiguration config;
	
	@Override
	public void onEnable() {
		
		if (!this.getDataFolder().exists()) { 
			 this.getDataFolder().mkdir();
		}
		
		file = Plugin.getFile("token");
		config = Plugin.getFileConfig(file);
		
		if(!config.contains("token")){
			try {
				setupConfiguration();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}
		
		String token = config.getString("token");
		
		if(token == null || token.equals("YOUR_TOKEN_KEY") || token.equals("")){
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Please change the token key in the configuration file.");
			return;
		}
		
		try {
			BotDiscord botdiscord = new BotDiscord(token);
			new Thread(botdiscord, "bot").start();
		} catch (LoginException | InterruptedException | IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void onDisable() {

	}
	
	public void setupConfiguration() throws IOException {
		config.set("token", "YOUR_TOKEN_KEY");
		config.save(file);
		Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Please change the token key in the configuration file.");
	}
	
	public static Plugin plugin() {
		return Plugin.getPlugin(Plugin.class);
	}
	
	public static File getFile(String name) {
		name = name + ".yml";
		File file = new File(Plugin.plugin().getDataFolder(), name);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return file;
	}
	
	public static FileConfiguration getFileConfig(File file) {
		return YamlConfiguration.loadConfiguration(file);
	}
}

