package com.github.radiant.ezclans.commands.player;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.radiant.ezclans.EzClans;
import com.github.radiant.ezclans.commands.ACommand;
import com.github.radiant.ezclans.commands.CommandException;
import com.github.radiant.ezclans.commands.CommandManager;
import com.github.radiant.ezclans.core.Clan;
import com.github.radiant.ezclans.core.ClanMember;
import com.github.radiant.ezclans.core.Clans;
import com.github.radiant.ezclans.lang.Lang;

	public class RenameCommand extends ACommand {
	
	protected static final boolean consoleExecutable = false;

	public RenameCommand(CommandSender sender, String[] args, EzClans plugin) {
		super(sender, args, plugin);
	}

	@Override
	public boolean execute() throws Exception {
		if (!consoleExecutable && !(sender instanceof Player)) {
			throw new CommandException(Lang.getLang("must_be_player"));
		}
		Player p = (Player) sender;
		double cost = Clans.clanCost;
		if (args.length < 2) {
			throw new CommandException(Lang.getLang("not_enough_args"));
		}
		ClanMember currentMember = Clans.getMember(p.getUniqueId());
		if (currentMember == null) {
			throw new CommandException(Lang.getLang("not_in_clan"));
		}
		if (!currentMember.isLeader()) {
			throw new CommandException(Lang.getLang("not_leader"));
		}
		String clanName = "";
		for (int i = 1; i < args.length; i++) {
			clanName += args[i]+" ";
		}
		clanName = clanName.trim();
		if (!CommandManager.legalName(clanName)) {
			throw new CommandException(Lang.getLang("illegal_name"));
		}
		Clan byName = Clans.getClanByName(clanName);
		if (byName != null) {
			throw new CommandException(Lang.getLang("already_exists"));
		}
		
		currentMember.getClan().setName(clanName);
		
		return false;
	}

	@Override
	public boolean executeAsync() throws CommandException {
		// TODO Auto-generated method stub
		return false;
	}

}
