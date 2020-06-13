package com.github.radiant.ezclans.commands;

import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.radiant.ezclans.EzClans;
import com.github.radiant.ezclans.core.ClanMember;
import com.github.radiant.ezclans.core.Clans;
import com.github.radiant.ezclans.core.Pendings;
import com.github.radiant.ezclans.lang.Lang;

public class DisbandCommand extends ACommand {
	
	protected static final boolean consoleExecutable = false;

	public DisbandCommand(CommandSender sender, String[] args, EzClans plugin) {
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
		if (!member.isLeader()) {
			throw new CommandException(Lang.getLang("not_leader"));
		}
		UUID pending = Pendings.getDisbandPending(p.getUniqueId());
		if (pending == null) {
			Pendings.setDisbandPending(p.getUniqueId(), member.getClan().getId());
			p.sendMessage(String.format(Lang.getLang("disband_confirm"), Lang.colorString(member.getClan().getName())));
		}
		else {
			if (!pending.equals(member.getClan().getId()) || args.length < 2 || !args[1].equals("confirm")) {
				Pendings.removeDisbandPending(p.getUniqueId());
				p.sendMessage(Lang.getLang("disband_canceled"));
			}
			else {
				Clans.removeClan(pending);
				Pendings.removeDisbandPending(p.getUniqueId());
				p.sendMessage(String.format(Lang.getLang("disband"), Lang.colorString(member.getClan().getName())));
			}
		}
		return false;
	}

	@Override
	public boolean executeAsync() throws CommandException {
		// TODO Auto-generated method stub
		return false;
	}

}
