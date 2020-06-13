package com.github.radiant.ezclans.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.radiant.ezclans.EzClans;
import com.github.radiant.ezclans.core.ClanMember;
import com.github.radiant.ezclans.core.Clans;
import com.github.radiant.ezclans.lang.Lang;

public class LeaveCommand extends ACommand {
	
	protected static final boolean consoleExecutable = false;

	public LeaveCommand(CommandSender sender, String[] args, EzClans plugin) {
		super(sender, args, plugin);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean execute() throws Exception {
		if (!consoleExecutable && !(sender instanceof Player)) {
			throw new CommandException(Lang.getLang("must_be_player"));
		}
		Player p = (Player) sender;
		ClanMember member = Clans.getMember(p.getUniqueId());
		if (member == null) {
			throw new CommandException(Lang.getLang("not_in_clan"));
		}
		if (member.isLeader()) {
			throw new CommandException(Lang.getLang("cant_be_leader"));
		}
		member.getClan().removeMember(member);
		p.sendMessage(Lang.getLang("leave_self_msg"));
		member.getClan().clanMessage(String.format(Lang.getLang("leave_msg"), member.getName()));
		
		return false;
	}

	@Override
	public boolean executeAsync() throws CommandException {
		// TODO Auto-generated method stub
		return false;
	}
}
