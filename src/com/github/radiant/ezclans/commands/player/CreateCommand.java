package com.github.radiant.ezclans.commands.player;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.radiant.ezclans.EzClans;
import com.github.radiant.ezclans.commands.ACommand;
import com.github.radiant.ezclans.commands.CommandException;
import com.github.radiant.ezclans.commands.CommandManager;
import com.github.radiant.ezclans.core.Clan;
import com.github.radiant.ezclans.core.ClanMember;
import com.github.radiant.ezclans.core.Clans;
import com.github.radiant.ezclans.core.Pendings;
import com.github.radiant.ezclans.integrations.EzEconomy;
import com.github.radiant.ezclans.lang.Lang;

public class CreateCommand extends ACommand{
	
	protected static final boolean consoleExecutable = false;

	public CreateCommand(CommandSender sender, String[] args, EzClans plugin) {
		super(sender, args, plugin);
	}
	
	@Override
	public boolean execute() throws Exception {
		if (!consoleExecutable && !(sender instanceof Player)) {
			throw new CommandException(Lang.getLang("must_be_player"));
		}
		Player p = (Player) sender;
		double cost = Clans.clanCost;
		if (args.length < 2) {
			throw new CommandException(Lang.getLang("not_enough_args"));
		}
		ClanMember currentMember = Clans.getMember(p.getUniqueId());
		if (currentMember != null) {
			throw new CommandException(Lang.getLang("already_in_clan"));
		}
		String clanName = "";
		for (int i = 1; i < args.length; i++) {
			clanName += args[i]+" ";
		}
		clanName = clanName.trim();
		if (!CommandManager.legalName(clanName)) {
			throw new CommandException(Lang.getLang("illegal_name"));
		}
		Clan byName = Clans.getClanByName(clanName);
		if (byName != null) {
			throw new CommandException(Lang.getLang("already_exists"));
		}
		String pending = Pendings.getPending(p.getUniqueId());
		if (pending != null) {
			if (!pending.equals(clanName)) {
				Pendings.removePending(p.getUniqueId());
				throw new CommandException(Lang.getLang("repeat_name"));
			}
			else {
				double balance = EzEconomy.getBalance(p);
				if (balance < cost) {
					throw new CommandException(String.format(Lang.getLang("not_enough_money"), Double.toString(cost)));
				}
				Clan clan = Clans.createClan(clanName);
				if (clan == null) {
					throw new CommandException(Lang.getLang("could_not_create"));
				}
				ClanMember member = clan.addPlayer(p);
				member.setAsLeader();
				EzEconomy.withdraw(p, cost);
				sender.sendMessage(String.format(Lang.getLang("clan_created"), Lang.colorString(clanName)));
				Pendings.removePending(p.getUniqueId());
			}
		}
		else {
			Pendings.setPending(p.getUniqueId(), clanName);
			p.sendMessage(String.format(Lang.getLang("clan_create_confirm"), Lang.colorString(clanName), Double.toString(cost)));
		}
		
		return false;
	}

	@Override
	public boolean executeAsync() {
		// TODO Auto-generated method stub
		return false;
	}

}
