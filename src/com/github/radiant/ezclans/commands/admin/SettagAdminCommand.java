package com.github.radiant.ezclans.commands.admin;

import org.bukkit.Bukkit;
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

public class SettagAdminCommand extends ACommand {
	
	protected static final String requirePermission = "ezclans.admin";

	public SettagAdminCommand(CommandSender sender, String[] args, EzClans plugin) {
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
		String toRetag = args[1];
		ClanMember retagMember = Clans.getMemberByName(toRetag);
		if (retagMember == null) {
			throw new CommandException(Lang.getLang("not_in_clan_other"));
		}
		String clanTag = "";
		for (int i = 2; i < args.length; i++) {
			clanTag += args[i]+" ";
		}
		clanTag = clanTag.trim();
		if (!CommandManager.legalTag(clanTag)) {
			throw new CommandException(Lang.getLang("illegal_tag"));
		}
		retagMember.getClan().setTag(clanTag);
		retagMember.getClan().clanMessage(String.format(Lang.getLang("tag_changed"), Lang.colorString(clanTag)));
		
		return false;
	}

	@Override
	public boolean executeAsync() throws CommandException {
		// TODO Auto-generated method stub
		return false;
	}

}
