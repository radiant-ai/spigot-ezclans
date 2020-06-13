package com.github.radiant.ezclans.lang;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.github.radiant.ezclans.BackupTask;
import com.github.radiant.ezclans.EzClans;

public class CustomConfig {
	
	private FileConfiguration customConfig = null;
	private File customConfigFile = null;
	private EzClans plugin;
	private String name;
	
	public CustomConfig(EzClans plugin, String name) {
		this.plugin = plugin;
		this.name = name;
	}
	
	public void reloadCustomConfig() {
	    if (customConfigFile == null) {
	    customConfigFile = new File(plugin.getDataFolder(), name);
	    }
	    customConfig = YamlConfiguration.loadConfiguration(customConfigFile);

	    try {
	    	Reader defConfigStream = new InputStreamReader(plugin.getResource(name), "UTF8");
		    if (defConfigStream != null) {
		        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
		        customConfig.setDefaults(defConfig);
		        customConfig.options().copyDefaults(true);
		        customConfig.save(customConfigFile);
		    }
	    }
	    catch (Exception e) { 
	    	plugin.getLogger().severe(e.getStackTrace().toString());
	    }
	}
	
	public FileConfiguration getCustomConfig() {
	    if (customConfig == null) {
	        reloadCustomConfig();
	    }
	    return customConfig;
	}
	
	public void saveCustomConfig() {
	    if (customConfig == null || customConfigFile == null) {
	        return;
	    }
	    try {
	        getCustomConfig().save(customConfigFile);
	    } catch (IOException ex) {
	        plugin.getLogger().log(Level.SEVERE, "Could not save config to " + customConfigFile, ex);
	    }
	}
	
	public void saveDefaultConfig() {
	    if (customConfigFile == null) {
	        customConfigFile = new File(plugin.getDataFolder(), name);
	    }
	    if (!customConfigFile.exists()) {
	    	plugin.saveResource(name, false);
	    }
	    else {
	    	customConfig = YamlConfiguration.loadConfiguration(customConfigFile);
	    	try {
		    	Reader defConfigStream = new InputStreamReader(plugin.getResource(name), "UTF8");
			    if (defConfigStream != null) {
			        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
			        customConfig.setDefaults(defConfig);
			        customConfig.options().copyDefaults(true);
			        customConfig.save(customConfigFile);
			    }
		    }
		    catch (Exception e) { 
		    	plugin.getLogger().severe(e.getStackTrace().toString());
		    }
	    }
	}
	
	public void scheduleBackup(int startTime, int ticksRepeat) {
		BackupTask task = new BackupTask(customConfigFile);
		Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, task, startTime, ticksRepeat);
	}
}
