package com.github.radiant.ezclans.commands.player;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.radiant.ezclans.EzClans;
import com.github.radiant.ezclans.commands.ACommand;
import com.github.radiant.ezclans.commands.CommandException;
import com.github.radiant.ezclans.core.Clan;
import com.github.radiant.ezclans.core.ClanMember;
import com.github.radiant.ezclans.core.Clans;
import com.github.radiant.ezclans.lang.Lang;

public class InfoCommand extends ACommand {
	
	public static final boolean consoleExecutable = false;

	public InfoCommand(CommandSender sender, String[] args, EzClans plugin) {
		super(sender, args, plugin);
	}

	@Override
	public boolean execute() throws Exception {
		if (!consoleExecutable && !(sender instanceof Player)) {
			throw new CommandException(Lang.getLang("must_be_player"));
		}
		if (args.length > 1) {
			String clanName = "";
			for (int i = 1; i < args.length; i++) {
				clanName += args[i]+" ";
			}
			clanName = clanName.trim();
			Clan clan = Clans.getClanByName(clanName);
			if (clan != null) {
				Clans.msgInfo(clan, (Player) sender);
			}
			else {
				throw new CommandException(Lang.getLang("no_such_clan"));
			}
		}
		else {
			Player player = (Player) sender;
			ClanMember member = Clans.getMember(player.getUniqueId());
			if (member != null) {
				Clans.msgInfoMember(member.getClan(), player);
			}
			else {
				throw new CommandException(Lang.getLang("no_such_clan"));
			}
		}
		
		return false;
	}

	@Override
	public boolean executeAsync() {
		// TODO Auto-generated method stub
		return false;
	}

}
