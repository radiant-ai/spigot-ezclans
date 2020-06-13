package com.github.radiant.ezclans.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.radiant.ezclans.EzClans;
import com.github.radiant.ezclans.core.ClanMember;
import com.github.radiant.ezclans.core.Clans;
import com.github.radiant.ezclans.lang.Lang;

public class PvpCommand extends ACommand {
	
	public static final boolean consoleExecutable = false;

	public PvpCommand(CommandSender sender, String[] args, EzClans plugin) {
		super(sender, args, plugin);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean execute() throws Exception {
		if (!consoleExecutable && !(sender instanceof Player)) {
			throw new CommandException(Lang.getLang("must_be_player"));
		}
		Player player = (Player) sender;
		ClanMember member = Clans.getMember(player.getUniqueId());
		if (member == null) {
			sender.sendMessage(Lang.getLang("not_in_clan"));
		}
		boolean newPvp = !member.isClanPvp();
		member.setClanPvp(newPvp);
		String status = newPvp ? Lang.getLang("enabled") : Lang.getLang("disabled");
		player.sendMessage(String.format(Lang.getLang("pvp_mode"), status));
		return false;
	}

	@Override
	public boolean executeAsync() throws CommandException {
		// TODO Auto-generated method stub
		return false;
	}
}
