package com.github.radiant.ezclans.commands.player;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.radiant.ezclans.EzClans;
import com.github.radiant.ezclans.commands.ACommand;
import com.github.radiant.ezclans.commands.CommandException;
import com.github.radiant.ezclans.core.ClanMember;
import com.github.radiant.ezclans.core.Clans;
import com.github.radiant.ezclans.core.HomeTask;
import com.github.radiant.ezclans.core.Pendings;
import com.github.radiant.ezclans.core.TeleportPlayerInfo;
import com.github.radiant.ezclans.lang.Lang;

public class HomeCommand extends ACommand  {

	protected static final boolean consoleExecutable = false;

	public HomeCommand(CommandSender sender, String[] args, EzClans plugin) {
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
		Location home = member.getClan().getHome();
		if (home == null) {
			throw new CommandException(Lang.getLang("no_clanhome"));
		}
		TeleportPlayerInfo info = Pendings.getTeleportPending(p.getUniqueId());
		if (info != null) {
			return false;
		}
		HomeTask ht = new HomeTask(p, home);
		p.sendMessage(String.format(Lang.getLang("home_prepare"), Integer.toString(Clans.homeCd)));
		int taskId = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, ht, 20*Clans.homeCd);
		TeleportPlayerInfo tpinfo = new TeleportPlayerInfo(taskId, p.getLocation());
		Pendings.setTeleportPending(p.getUniqueId(), tpinfo);
		return false;
	}

	@Override
	public boolean executeAsync() throws CommandException {
		// TODO Auto-generated method stub
		return false;
	}

}
