package com.github.radiant.ezclans.commands.player;

import org.bukkit.Bukkit;
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

public class InviteCommand extends ACommand {
	
	protected static final boolean consoleExecutable = false;

	public InviteCommand(CommandSender sender, String[] args, EzClans plugin) {
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
		String toInvite = args[1];
		Player invitedPlayer = Bukkit.getServer().getPlayer(toInvite);
		if (invitedPlayer == null || !invitedPlayer.isOnline()) {
			throw new CommandException(Lang.getLang("player_not_found"));
		}
		if (p.getUniqueId().equals(invitedPlayer.getUniqueId())) {
			throw new CommandException(Lang.getLang("invite_yourself"));
		}
		Clan currentClan = member.getClan();
		ClanMember invitedMember = Clans.getMember(invitedPlayer.getUniqueId());
		if (invitedMember != null) {
			throw new CommandException(Lang.getLang("already_in_clan_other"));
		}
		Pendings.setInvitePending(invitedPlayer.getUniqueId(), currentClan.getId());
		p.sendMessage(String.format(Lang.getLang("ivitation_sent"), Lang.colorString(toInvite)));
		invitedPlayer.sendMessage(String.format(Lang.getLang("invited_to_clan"), Lang.colorString(currentClan.getName())));
		return false;
	}

	@Override
	public boolean executeAsync() throws CommandException {
		// TODO Auto-generated method stub
		return false;
	}

}
