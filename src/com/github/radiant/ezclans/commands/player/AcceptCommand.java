package com.github.radiant.ezclans.commands.player;

import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.radiant.ezclans.EzClans;
import com.github.radiant.ezclans.commands.ACommand;
import com.github.radiant.ezclans.commands.CommandException;
import com.github.radiant.ezclans.core.Clan;
import com.github.radiant.ezclans.core.ClanMember;
import com.github.radiant.ezclans.core.Clans;
import com.github.radiant.ezclans.core.Pendings;
import com.github.radiant.ezclans.lang.Lang;

public class AcceptCommand extends ACommand {
	
	protected static final boolean consoleExecutable = false;

	public AcceptCommand(CommandSender sender, String[] args, EzClans plugin) {
		super(sender, args, plugin);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean execute() throws Exception {
		if (!consoleExecutable && !(sender instanceof Player)) {
			throw new CommandException(Lang.getLang("must_be_player"));
		}
		Player p = (Player) sender;
		UUID toAcceptId = Pendings.getInvitePending(p.getUniqueId());
		if (toAcceptId == null) {
			throw new CommandException(Lang.getLang("no_invites"));
		}
		Pendings.removeInvitePending(p.getUniqueId());
		Clan toAccept = Clans.getClan(toAcceptId);
		if (toAccept == null || toAccept.isDisbanded()) {
			throw new CommandException(Lang.getLang("accept_disbanded"));
		}
		if (toAccept.clanSize()+1>Clans.getMaxMembers(toAccept.getLevel())) {
			throw new CommandException(Lang.getLang("invite_maxsize")+Clans.getMaxMembers(toAccept.getLevel()));
		}
		ClanMember member = Clans.getMember(p.getUniqueId());
		if (member != null) {
			throw new CommandException(Lang.getLang("already_in_clan"));
		}
		ClanMember newMember = toAccept.addPlayer(p);
		if (newMember != null) {
			p.sendMessage(String.format(Lang.getLang("accepted_invite"), Lang.colorString(toAccept.getName())));
			newMember.getClan().clanMessage(String.format(Lang.getLang("enter_msg"), newMember.getName()));
		}
		return false;
	}

	@Override
	public boolean executeAsync() throws CommandException {
		// TODO Auto-generated method stub
		return false;
	}

}
