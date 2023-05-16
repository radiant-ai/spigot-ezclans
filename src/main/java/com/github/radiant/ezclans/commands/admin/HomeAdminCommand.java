package com.github.radiant.ezclans.commands.admin;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.radiant.ezclans.EzClans;
import com.github.radiant.ezclans.commands.ACommand;
import com.github.radiant.ezclans.commands.CommandException;
import com.github.radiant.ezclans.core.Clan;
import com.github.radiant.ezclans.core.Clans;
import com.github.radiant.ezclans.lang.Lang;

public class HomeAdminCommand extends ACommand {
	
	protected static final boolean consoleExecutable = false;
	protected static final String requirePermission = "ezclans.admin";

	public HomeAdminCommand(CommandSender sender, String[] args, EzClans plugin) {
		super(sender, args, plugin);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean execute() throws Exception {
		if (!consoleExecutable && !(sender instanceof Player)) {
			throw new CommandException(Lang.getLang("must_be_player"));
		}
		Player p = (Player) sender;
		if (!p.hasPermission(requirePermission)) {
			throw new CommandException(Lang.getLang("not_enough_perms"));
		}
		if (args.length < 2) {
			throw new CommandException(Lang.getLang("not_enough_args"));
		}
		String clanName = "";
		for (int i = 1; i < args.length; i++) {
			clanName += args[i]+" ";
		}
		clanName = clanName.trim();
		Clan clan = Clans.getClanByName(clanName);
		if (clan != null) {
			Location home = clan.getHome();
			if (home != null) {
				p.teleportAsync(home);
			}
			else {
				throw new CommandException(Lang.getLang("no_clanhome"));
			}
		}
		else {
			throw new CommandException(Lang.getLang("no_such_clan"));
		}
		return false;
	}

	@Override
	public boolean executeAsync() throws CommandException {
		// TODO Auto-generated method stub
		return false;
	}
	
}
