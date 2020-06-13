package com.github.radiant.ezclans.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.radiant.ezclans.EzClans;
import com.github.radiant.ezclans.core.ClanMember;
import com.github.radiant.ezclans.core.Clans;
import com.github.radiant.ezclans.lang.Lang;

public class ListCommand extends ACommand {
	protected static final boolean consoleExecutable = false;

	public ListCommand(CommandSender sender, String[] args, EzClans plugin) {
		super(sender, args, plugin);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean execute() throws Exception {
		int page = 1;
		if (args.length > 1) {
			try {
				page = Integer.parseInt(args[1]);
			}
			catch (Exception e) {}
		}
		Clans.sendClanList(sender, page);
		return false;
	}

	@Override
	public boolean executeAsync() throws CommandException {
		// TODO Auto-generated method stub
		return false;
	}
}
