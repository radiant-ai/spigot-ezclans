package com.github.radiant.ezclans.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.radiant.ezclans.EzClans;
import com.github.radiant.ezclans.core.ClanMember;
import com.github.radiant.ezclans.core.Clans;
import com.github.radiant.ezclans.lang.Lang;

public class SetmoderatorCommand extends ACommand {
	protected static final boolean consoleExecutable = false;

	public SetmoderatorCommand(CommandSender sender, String[] args, EzClans plugin) {
		super(sender, args, plugin);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean execute() throws Exception {
		if (!consoleExecutable && !(sender instanceof Player)) {
			throw new CommandException(Lang.getLang("must_be_player"));
		}
		Player p = (Player) sender;
		if (args.length < 2) {
			throw new CommandException(Lang.getLang("not_enough_args"));
		}
		ClanMember member = Clans.getMember(p.getUniqueId());
		if (member == null) {
			throw new CommandException(Lang.getLang("not_in_clan"));
		}
		if (!member.isLeader()) {
			throw new CommandException(Lang.getLang("not_leader"));
		}
		String toModerator = args[1];
		ClanMember moderatored = member.getClan().getMemberByName(toModerator);
		if (moderatored == null) {
			throw new CommandException(Lang.getLang("moderator_not_in_clan"));
		}
		if (moderatored.isModerator()) {
			throw new CommandException(Lang.getLang("already_moderator"));
		}
		if (p.getUniqueId().equals(moderatored.getUuid())) {
			throw new CommandException(Lang.getLang("moderator_yourself"));
		}
		moderatored.setModerator();
		moderatored.getClan().clanMessage(String.format(Lang.getLang("moderator_broadcast"), moderatored.getName()));
		return false;
	}

	@Override
	public boolean executeAsync() throws CommandException {
		// TODO Auto-generated method stub
		return false;
	}

}
