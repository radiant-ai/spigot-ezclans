package com.github.radiant.ezclans.commands.player;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.radiant.ezclans.EzClans;
import com.github.radiant.ezclans.commands.ACommand;
import com.github.radiant.ezclans.commands.CommandException;
import com.github.radiant.ezclans.core.Clan;
import com.github.radiant.ezclans.core.ClanMember;
import com.github.radiant.ezclans.core.Clans;
import com.github.radiant.ezclans.integrations.EzEconomy;
import com.github.radiant.ezclans.lang.Lang;
import com.github.radiant.ezclans.logs.EzLogs;

public class BalanceCommand extends ACommand {
	
	protected static final boolean consoleExecutable = false;

	public BalanceCommand(CommandSender sender, String[] args, EzClans plugin) {
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
		Clan clan = member.getClan();
		if (args.length < 2) {
			p.sendMessage(String.format(Lang.getLang("balance"), clan.getBank()+" $"));
		}
		else if (args.length > 2) {
			if (!member.isLeader() && !member.isModerator()) {
				throw new CommandException(Lang.getLang("not_leader_or_moderator"));
			}
			String action = args[1];
			String amt = args[2];
			int parsedAmt = -1;
			try {
				parsedAmt = Integer.parseInt(amt);
			}
			catch (NumberFormatException e) {
			}
			
			if (parsedAmt <= 0) {
				throw new CommandException(Lang.getLang("amt_invalid"));
			}
			double balance = EzEconomy.getBalance(p);
			int bank = clan.getBank();
			if (action.equals("add")) {
				if (balance >= parsedAmt) {
					clan.deposit(parsedAmt);
					EzEconomy.withdraw(p, parsedAmt);
					p.sendMessage(String.format(Lang.getLang("balance_add"), parsedAmt+" $"));
					EzLogs.logBalance(member, clan, parsedAmt, "deposit");
				}
				else {
					throw new CommandException(String.format(Lang.getLang("not_enough_money"), parsedAmt+" $"));
				}
			}
			else if (action.equals("take")) {
				if (bank >= parsedAmt) {
					clan.withdraw(parsedAmt);
					EzEconomy.pay(p, parsedAmt);
					p.sendMessage(String.format(Lang.getLang("balance_take"), parsedAmt+" $"));
					EzLogs.logBalance(member, clan, parsedAmt, "withdraw");
				}
				else {
					throw new CommandException(String.format(Lang.getLang("bank_not_enough"), parsedAmt+" $"));
				}
			}
			else {
				throw new CommandException(Lang.getLang("balance_usage"));
			}
		}
		else {
			throw new CommandException(Lang.getLang("balance_usage"));
		}
		
		return false;
	}

	@Override
	public boolean executeAsync() throws CommandException {
		// TODO Auto-generated method stub
		return false;
	}

}
