package com.github.radiant.ezclans.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.radiant.ezclans.lang.Lang;

public class ClanCommandExecutor implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		try {
			if (label.equals("c")) {
				label = "clan";
			}
			ACommand command = CommandManager.createCommand(label, sender, args);
			if (command != null) {
				command.execute();
			}
			else {
				throw new CommandException(Lang.getLang("no_such_cmd"));
			}
		}
		catch (CommandException e) {
			sender.sendMessage(e.getMessage());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean isPlayer(CommandSender sender) {
		return sender instanceof Player;
	}
	
}
