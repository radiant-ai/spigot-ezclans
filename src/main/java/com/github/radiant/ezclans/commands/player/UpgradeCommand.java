package com.github.radiant.ezclans.commands.player;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.radiant.ezclans.EzClans;
import com.github.radiant.ezclans.commands.ACommand;
import com.github.radiant.ezclans.commands.CommandException;
import com.github.radiant.ezclans.core.ClanException;
import com.github.radiant.ezclans.core.ClanMember;
import com.github.radiant.ezclans.core.Clans;
import com.github.radiant.ezclans.lang.Lang;

public class UpgradeCommand extends ACommand {
	
	protected static final boolean consoleExecutable = false;

	public UpgradeCommand(CommandSender sender, String[] args, EzClans plugin) {
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
		try {
			member.getClan().upgrade();
		}
		catch (ClanException e) {
			p.sendMessage(Lang.getLang("upgrade_fail")+" "+e.getMessage());
		}
		catch (Exception e) {
			p.sendMessage(Lang.getLang("upgrade_fail")+"unknown");
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean executeAsync() throws CommandException {
		// TODO Auto-generated method stub
		return false;
	}

}
