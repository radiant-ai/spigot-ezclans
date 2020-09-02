package com.github.radiant.ezclans.commands;

import org.bukkit.command.CommandSender;

import com.github.radiant.ezclans.EzClans;

public abstract class ACommand {
	protected CommandSender sender;
	protected String[] args;
	protected EzClans plugin;
	protected static final boolean consoleExecutable = false;
	
	public ACommand(CommandSender sender, String[] args, EzClans plugin) {
		this.sender = sender;
		this.args = args;
		this.plugin = plugin;
	}
	public abstract boolean execute() throws Exception;
	
	public abstract boolean executeAsync() throws CommandException;
}
