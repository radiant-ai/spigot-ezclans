package com.github.radiant.ezclans.commands.admin;

import com.github.radiant.ezclans.EzClans;
import com.github.radiant.ezclans.commands.ACommand;
import com.github.radiant.ezclans.commands.CommandException;
import com.github.radiant.ezclans.core.Clans;
import com.github.radiant.ezclans.lang.Lang;
import org.bukkit.command.CommandSender;

public class ConvertAdminCommand extends ACommand {
    protected static final String requirePermission = "ezclans.admin";

    public ConvertAdminCommand(CommandSender sender, String[] args, EzClans plugin) {
        super(sender, args, plugin);
    }

    @Override
    public boolean execute() throws Exception {
        if (!sender.hasPermission(requirePermission)) {
            throw new CommandException(Lang.getLang("not_enough_perms"));
        }
        Clans.useLegacySerialization = false;
        EzClans.db.saveClans(Clans.getClans());
        sender.sendMessage("Conversion executed successfully! Now stop the server and flip the config value to true.");
        return false;
    }

    @Override
    public boolean executeAsync() throws CommandException {
        return false;
    }
}
