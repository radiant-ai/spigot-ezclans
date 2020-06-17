package com.github.radiant.ezclans.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.radiant.ezclans.EzClans;
import com.github.radiant.ezclans.core.ClanMember;
import com.github.radiant.ezclans.core.Clans;
import com.github.radiant.ezclans.lang.Lang;

public class RemovemoderatorCommand extends ACommand {
	protected static final boolean consoleExecutable = false;

	public RemovemoderatorCommand(CommandSender sender, String[] args, EzClans plugin) {
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
		String toDemote = args[1];
		ClanMember demoted = member.getClan().getMemberByName(toDemote);
		if (demoted == null) {
			throw new CommandException(Lang.getLang("demote_not_in_clan"));
		}
		if (p.getUniqueId().equals(demoted.getUuid())) {
			throw new CommandException(Lang.getLang("demote_yourself"));
		}
		if (!demoted.isModerator()) {
			throw new CommandException(Lang.getLang("already_demoted"));
		}
		demoted.setMember();
		demoted.getClan().clanMessage(String.format(Lang.getLang("demote_broadcast"), demoted.getName()));
		return false;
	}

	@Override
	public boolean executeAsync() throws CommandException {
		// TODO Auto-generated method stub
		return false;
	}

}
