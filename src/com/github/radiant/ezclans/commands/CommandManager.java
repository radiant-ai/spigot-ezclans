package com.github.radiant.ezclans.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.github.radiant.ezclans.EzClans;

public class CommandManager {
	
	private static EzClans plugin = null;
	private static Map<String, Class<? extends ACommand>> commands = new HashMap<String, Class<? extends ACommand>>();
	
	public static void initialize(EzClans p) {
		plugin = p;
		commands.put("create", CreateCommand.class);
		commands.put("info", InfoCommand.class);
		commands.put("disband", DisbandCommand.class);
		commands.put("sethome", SethomeCommand.class);
		commands.put("home", HomeCommand.class);
		commands.put("settag", SettagCommand.class);
		commands.put("invite", InviteCommand.class);
		commands.put("accept", AcceptCommand.class);
		commands.put("rename", RenameCommand.class);
		commands.put("leave", LeaveCommand.class);
		commands.put("kick", KickCommand.class);
		commands.put("chat", ChatCommand.class);
		commands.put("pvp", PvpCommand.class);
		commands.put("help", HelpCommand.class);
		commands.put("list", ListCommand.class);
	}
	
	private static ACommand factory(String label, CommandSender sender, String[] args) {
		Class<? extends ACommand> cmdClass = commands.get(label);
		if (cmdClass==null)
			return null;
		try {
			ACommand command = cmdClass.getDeclaredConstructor(CommandSender.class, String[].class, EzClans.class).newInstance(sender, args, plugin);
			return command;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static List<String> getCommandList() {
		List<String> result = new ArrayList<String>();
		result.addAll(commands.keySet());
		return result;
	}
	
	public static ACommand createCommand(CommandSender sender, String[] args) throws Exception {
		if (args.length > 0) {
			String name = args[0];
			ACommand cmd = factory(name, sender, args);
			return cmd;
		}
		else {
			return null;
		}
	}
	public static boolean legalName(String name) {
		if (stripColor(name).length() <= 32) {
			if (name.matches("[\\wà-ÿÀ-ß\"\'\\-¸¨ &]+")) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean legalTag(String name) {
		if (stripColor(name).length() <= 20) {
			if (name.matches("[\\wà-ÿÀ-ß\"\'\\-¸¨ &]+")) {
				return true;
			}
		}
		return false;
	}
	
	public static String stripColor(String str) {
		return ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', str));
	}
}
