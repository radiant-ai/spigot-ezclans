package com.github.radiant.ezclans.commands.player;

import com.github.radiant.ezclans.core.Clan;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.radiant.ezclans.EzClans;
import com.github.radiant.ezclans.commands.ACommand;
import com.github.radiant.ezclans.commands.CommandException;
import com.github.radiant.ezclans.core.ClanMember;
import com.github.radiant.ezclans.core.Clans;
import com.github.radiant.ezclans.lang.Lang;

public class OnlineCommand extends ACommand {
	
	public static final boolean consoleExecutable = false;

	public OnlineCommand(CommandSender sender, String[] args, EzClans plugin) {
		super(sender, args, plugin);
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
		Clan clan = member.getClan();
		p.sendMessage(Lang.getLang("online_list")
				+ " (" + clan.getOnlineList().size() + "): " + ChatColor.WHITE + clan.getOnlineString());
		return false;
	}

	@Override
	public boolean executeAsync() throws CommandException {
		// TODO Auto-generated method stub
		return false;
	}

}
