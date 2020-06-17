package com.github.radiant.ezclans.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.radiant.ezclans.EzClans;
import com.github.radiant.ezclans.core.ClanMember;
import com.github.radiant.ezclans.core.Clans;
import com.github.radiant.ezclans.lang.Lang;

public class SettagCommand extends ACommand {
	
	protected static final boolean consoleExecutable = false;

	public SettagCommand(CommandSender sender, String[] args, EzClans plugin) {
		super(sender, args, plugin);
	}

	@Override
	public boolean execute() throws Exception {
		
		if (!consoleExecutable && !(sender instanceof Player)) {
			throw new CommandException(Lang.getLang("must_be_player"));
		}
		if (args.length < 2) {
			throw new CommandException(Lang.getLang("not_enough_args"));
		}
		Player p = (Player) sender;
		ClanMember member = Clans.getMember(p.getUniqueId());
		if (member == null) {
			throw new CommandException(Lang.getLang("not_in_clan"));
		}
		if (!member.isLeader() && !member.isModerator()) {
			throw new CommandException(Lang.getLang("not_leader_or_moderator"));
		}
		String clanTag = "";
		for (int i = 1; i < args.length; i++) {
			clanTag += args[i]+" ";
		}
		clanTag = clanTag.trim();
		if (!CommandManager.legalTag(clanTag)) {
			throw new CommandException(Lang.getLang("illegal_tag"));
		}
		member.getClan().setTag(clanTag);
		member.getClan().clanMessage(String.format(Lang.getLang("tag_changed"), Lang.colorString(clanTag)));
		
		return false;
	}

	@Override
	public boolean executeAsync() throws CommandException {
		// TODO Auto-generated method stub
		return false;
	}
}
