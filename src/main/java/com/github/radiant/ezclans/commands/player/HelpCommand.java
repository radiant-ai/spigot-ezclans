package com.github.radiant.ezclans.commands.player;

import org.bukkit.command.CommandSender;

import com.github.radiant.ezclans.EzClans;
import com.github.radiant.ezclans.commands.ACommand;
import com.github.radiant.ezclans.commands.CommandException;
import com.github.radiant.ezclans.lang.Lang;

public class HelpCommand extends ACommand {
	
	private static int pages = 4;
	
	public HelpCommand(CommandSender sender, String[] args, EzClans plugin) {
		super(sender, args, plugin);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean execute() throws Exception {
		int page = 1;
		if (args.length > 1) {
			try {
				page = Integer.parseInt(args[1]);
			}
			catch (Exception e) {}
		}
		printHelpPage(page);
		return false;
	}

	@Override
	public boolean executeAsync() throws CommandException {
		// TODO Auto-generated method stub
		return false;
	}
	
	private void printHelpPage(int page) {
		if (page > pages)
			page = pages;
		else if (page < 1)
			page = 1;
		sender.sendMessage(Lang.getLang("help")+" "+String.format(Lang.getLang("page"), Integer.toString(page), Integer.toString(pages))+":");
		switch (page) {
		case 4:
			sender.sendMessage(Lang.getLang("help_1"));
			sender.sendMessage(Lang.getLang("help_2"));
			sender.sendMessage(Lang.getLang("help_3"));
			sender.sendMessage(Lang.getLang("help_4"));
			sender.sendMessage(Lang.getLang("help_5"));
			sender.sendMessage(Lang.getLang("help_6"));
			sender.sendMessage(Lang.getLang("help_7"));
			sender.sendMessage(Lang.getLang("help_8"));
			break;
		case 3:
			sender.sendMessage(Lang.getLang("help_setleader"));
			sender.sendMessage(Lang.getLang("help_online"));
			sender.sendMessage(Lang.getLang("help_balance"));
			sender.sendMessage(Lang.getLang("help_upgrade"));
			sender.sendMessage(Lang.getLang("help_storage"));
			sender.sendMessage(Lang.getLang("help_color"));
			sender.sendMessage(Lang.getLang("help_loginmessage"));
			break;
		case 2:
			sender.sendMessage(Lang.getLang("help_rename"));
			sender.sendMessage(Lang.getLang("help_pvp"));
			sender.sendMessage(Lang.getLang("help_home"));
			sender.sendMessage(Lang.getLang("help_sethome"));
			sender.sendMessage(Lang.getLang("help_info"));
			sender.sendMessage(Lang.getLang("help_demote"));
			sender.sendMessage(Lang.getLang("help_promote"));
			break;
		default:
			sender.sendMessage(Lang.getLang("help_create"));
			sender.sendMessage(Lang.getLang("help_invite"));
			sender.sendMessage(Lang.getLang("help_kick"));
			sender.sendMessage(Lang.getLang("help_leave"));
			sender.sendMessage(Lang.getLang("help_disband"));
			sender.sendMessage(Lang.getLang("help_chat"));
			sender.sendMessage(Lang.getLang("help_settag"));
		}
		sender.sendMessage(Lang.getLang("help_page"));
	}

}
