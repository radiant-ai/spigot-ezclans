package com.github.radiant.ezclans.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.github.radiant.ezclans.EzClans;
import com.github.radiant.ezclans.commands.player.AcceptCommand;
import com.github.radiant.ezclans.commands.player.BalanceCommand;
import com.github.radiant.ezclans.commands.player.ChatCommand;
import com.github.radiant.ezclans.commands.player.CreateCommand;
import com.github.radiant.ezclans.commands.player.DisbandCommand;
import com.github.radiant.ezclans.commands.player.HelpCommand;
import com.github.radiant.ezclans.commands.player.HomeCommand;
import com.github.radiant.ezclans.commands.player.InfoCommand;
import com.github.radiant.ezclans.commands.player.InviteCommand;
import com.github.radiant.ezclans.commands.player.KickCommand;
import com.github.radiant.ezclans.commands.player.LeaveCommand;
import com.github.radiant.ezclans.commands.player.ListCommand;
import com.github.radiant.ezclans.commands.player.PvpCommand;
import com.github.radiant.ezclans.commands.player.RemovemoderatorCommand;
import com.github.radiant.ezclans.commands.player.RenameCommand;
import com.github.radiant.ezclans.commands.player.SethomeCommand;
import com.github.radiant.ezclans.commands.player.SetleaderCommand;
import com.github.radiant.ezclans.commands.player.SetmoderatorCommand;
import com.github.radiant.ezclans.commands.player.SettagCommand;
import com.github.radiant.ezclans.commands.player.StorageCommand;

public class CommandManager {
	
	private static EzClans plugin = null;
	private static Map<String, Map<String, Class<? extends ACommand>>> commands = new HashMap<String, Map<String, Class<? extends ACommand>>>();
	
	public static void initialize(EzClans p) {
		plugin = p;
		Map<String, Class<? extends ACommand>> pcommands = new HashMap<String, Class<? extends ACommand>>();
		Map<String, Class<? extends ACommand>> acommands = new HashMap<String, Class<? extends ACommand>>();
		pcommands.put("create", CreateCommand.class);
		pcommands.put("info", InfoCommand.class);
		pcommands.put("disband", DisbandCommand.class);
		pcommands.put("sethome", SethomeCommand.class);
		pcommands.put("home", HomeCommand.class);
		pcommands.put("settag", SettagCommand.class);
		pcommands.put("invite", InviteCommand.class);
		pcommands.put("accept", AcceptCommand.class);
		pcommands.put("rename", RenameCommand.class);
		pcommands.put("leave", LeaveCommand.class);
		pcommands.put("kick", KickCommand.class);
		pcommands.put("chat", ChatCommand.class);
		pcommands.put("pvp", PvpCommand.class);
		pcommands.put("help", HelpCommand.class);
		pcommands.put("list", ListCommand.class);
		pcommands.put("promote", SetmoderatorCommand.class);
		pcommands.put("demote", RemovemoderatorCommand.class);
		pcommands.put("setleader", SetleaderCommand.class);
		pcommands.put("storage", StorageCommand.class);
		pcommands.put("balance", BalanceCommand.class);
		commands.put("clan", pcommands);
	}
	
	private static ACommand factory(String label, String cmdName, CommandSender sender, String[] args) {
		Map<String, Class<? extends ACommand>> mapSet = commands.get(label);
		if (mapSet==null)
			return null;
		Class<? extends ACommand> cmdClass = mapSet.get(cmdName);
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
	
	public static List<String> getCommandList(String command) {
		if (command.equals("c")) {
			command = "clan";
		}
		List<String> result = new ArrayList<String>();
		Map<String, Class<? extends ACommand>> mapSet = commands.get(command);
		if (mapSet!=null) {
			result.addAll(mapSet.keySet());
		}
		return result;
	}
	
	public static ACommand createCommand(String label, CommandSender sender, String[] args) throws Exception {
		if (args.length > 0) {
			String name = args[0];
			ACommand cmd = factory(label, name, sender, args);
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
			if (name.matches("[\\wà-ÿÀ-ß\"\'\\-¸¨ &\u0021-\uFFFC]+")) {
				return true;
			}
		}
		return false;
	}
	
	public static String stripColor(String str) {
		return ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', str));
	}
}
