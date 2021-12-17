package com.github.radiant.ezclans.integrations;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.github.radiant.ezclans.commands.CommandManager;
import com.github.radiant.ezclans.core.Clans;

public class ClanTabCompleter implements TabCompleter {
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
		if (args.length > 0 && sender instanceof Player) {
			List<String> list = CommandManager.getCommandList(cmd.getName());
			if (list.size()>0) {
				if (args.length == 1) { //handle subcommand
					if (args[0].isEmpty()) {
						return list;
					}
					else {
						List<String> newList = new LinkedList<String>();
						for (String s : list) {
							if (s.startsWith(args[0])) {
								newList.add(s);
							}
						}
						return newList;
					}
				}
				else if (args.length == 2) {
					if (args[0].equals("info")) { //handle info
						if (args[1].isEmpty()) {
							return Clans.getClanNames("");
						}
						else {
							return Clans.getClanNames(args[1]);
						}
					}
					else if (args[0].equals("balance")) {
						List<String> ll = new LinkedList<String>();
						ll.add("add");
						ll.add("take");
						return ll;
					}
					else if (cmd.getName().equals("adminclan") && (args[0].equals("storage") || args[0].equals("home"))) {
						if (args[1].isEmpty()) {
							return Clans.getClanNames("");
						}
						else {
							return Clans.getClanNames(args[1]);
						}
					}
				}
			}
		}
		return null;
	}

}
