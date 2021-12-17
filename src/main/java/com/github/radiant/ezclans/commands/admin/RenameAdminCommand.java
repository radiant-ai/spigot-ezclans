package com.github.radiant.ezclans.commands.admin;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.radiant.ezclans.EzClans;
import com.github.radiant.ezclans.commands.ACommand;
import com.github.radiant.ezclans.commands.CommandException;
import com.github.radiant.ezclans.commands.CommandManager;
import com.github.radiant.ezclans.core.ClanMember;
import com.github.radiant.ezclans.core.Clans;
import com.github.radiant.ezclans.lang.Lang;

public class RenameAdminCommand extends ACommand {
	
	protected static final String requirePermission = "ezclans.admin";

	public RenameAdminCommand(CommandSender sender, String[] args, EzClans plugin) {
		super(sender, args, plugin);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean execute() throws Exception {
		if (sender instanceof Player && !((Player) sender).hasPermission(requirePermission)) {
			throw new CommandException(Lang.getLang("not_enough_perms"));
		}
		if (args.length < 3) {
			throw new CommandException(Lang.getLang("not_enough_args"));
		}
		String toRename = args[1];
		ClanMember retagMember = Clans.getMemberByName(toRename);
		if (retagMember == null) {
			throw new CommandException(Lang.getLang("not_in_clan_other"));
		}
		String clanName = "";
		for (int i = 2; i < args.length; i++) {
			clanName += args[i]+" ";
		}
		clanName = clanName.trim();
		if (!CommandManager.legalTag(clanName)) {
			throw new CommandException(Lang.getLang("illegal_tag"));
		}
		retagMember.getClan().setName(clanName);
		sender.sendMessage(String.format(Lang.getLang("clan_rename"), Lang.colorString(clanName)));
		
		return false;
	}

	@Override
	public boolean executeAsync() throws CommandException {
		// TODO Auto-generated method stub
		return false;
	}

}
