package com.github.radiant.ezclans;

import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.radiant.ezclans.commands.ClanCommandExecutor;
import com.github.radiant.ezclans.commands.CommandManager;
import com.github.radiant.ezclans.core.Clan;
import com.github.radiant.ezclans.core.ClanMember;
import com.github.radiant.ezclans.core.Clans;
import com.github.radiant.ezclans.db.DBYml;
import com.github.radiant.ezclans.db.IDBConnector;
import com.github.radiant.ezclans.events.ChatEvent;
import com.github.radiant.ezclans.events.PlayerMove;
import com.github.radiant.ezclans.events.PvpEvent;
import com.github.radiant.ezclans.integrations.ClanTabCompleter;
import com.github.radiant.ezclans.integrations.EzEconomy;
import com.github.radiant.ezclans.integrations.EzPlaceholder;
import com.github.radiant.ezclans.lang.Lang;
import com.github.radiant.ezclans.logs.EzLogs;
import com.github.radiant.ezclans.logs.InventoryListener;

public class EzClans extends JavaPlugin {
	public static IDBConnector db;
	
	public void onEnable() {
		if (!EzEconomy.setupEconomy() ) {
            Bukkit.getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
		if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
			new EzPlaceholder(this).register();
        }
		Lang.initialize(this);
		enableSerialization();
		registerCommands();
		loadConfigs();
		loadDB();
		CommandManager.initialize(this);
		EzLogs.initialize(this);
		registerEvents();
	}
	
	public void onDisable() {
		EzLogs.flush();
		db.saveClans(Clans.clans);
	}
	
	private void registerCommands() {
		this.getCommand("clan").setExecutor(new ClanCommandExecutor());
		this.getCommand("adminclan").setExecutor(new ClanCommandExecutor());
		this.getCommand("clan").setTabCompleter(new ClanTabCompleter());
		this.getCommand("adminclan").setTabCompleter(new ClanTabCompleter());
	}
	
	private void registerEvents() {
		getServer().getPluginManager().registerEvents(new PlayerMove(), this);
		getServer().getPluginManager().registerEvents(new PvpEvent(), this);
		getServer().getPluginManager().registerEvents(new ChatEvent(this), this);
		getServer().getPluginManager().registerEvents(new InventoryListener(), this);
	}
	
	private void loadConfigs() {
		getConfig().options().copyDefaults(true);
		saveConfig();
		Clans.loadConfig(getConfig());
	}
	
	private void loadDB() {
		db = new DBYml(this);
		db.loadClans();
	}
	
	private void enableSerialization() {
		ConfigurationSerialization.registerClass(Clan.class, "Clan");
		ConfigurationSerialization.registerClass(ClanMember.class, "Clan");
	}
	
	public boolean reload() {
		db.saveClans(Clans.clans);
		loadConfigs();
		loadDB();
		Lang.initialize(this);
		return true;
	}
	
	public void backup() {
		
	}
}
