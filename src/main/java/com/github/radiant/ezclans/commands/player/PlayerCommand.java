package com.github.radiant.ezclans.commands.player;

import com.github.radiant.ezclans.EzClans;
import com.github.radiant.ezclans.commands.ACommand;
import com.github.radiant.ezclans.commands.CommandException;
import com.github.radiant.ezclans.core.ClanMember;
import com.github.radiant.ezclans.core.Clans;
import com.github.radiant.ezclans.lang.Lang;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlayerCommand extends ACommand {
    public PlayerCommand(CommandSender sender, String[] args, EzClans plugin) {
        super(sender, args, plugin);
    }

    @Override
    public boolean execute() throws Exception {
        if (!consoleExecutable && !(sender instanceof Player)) {
            throw new CommandException(Lang.getLang("must_be_player"));
        }
        Player p = (Player) sender;
        if (args.length < 2) {
            throw new CommandException(Lang.getLang("not_enough_args"));
        }
        ClanMember member = Clans.getMemberByName(args[1]);
        if (member == null) {
            throw new CommandException(Lang.getLang("player_not_found"));
        }
        String tag = member.getClan().getTag();
        String name = member.getClan().getName();
        String message = String.format(Lang.getLang("in_clan"), member.getName(), Lang.colorString(tag), Lang.colorString(name));
        p.sendMessage(message);
        return false;
    }

    @Override
    public boolean executeAsync() throws CommandException {
        return false;
    }
}
