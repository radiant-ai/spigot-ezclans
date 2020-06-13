package com.github.radiant.ezclans.lang;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import com.github.radiant.ezclans.EzClans;

public class Lang {
	
	public static CustomConfig langFile;
	public static Map<String, String> options = new HashMap<String, String>();

	public static void initialize(EzClans plugin) {
		options = new HashMap<String, String>();
		langFile = new CustomConfig(plugin, "lang.yml");
		FileConfiguration fc = langFile.getCustomConfig();
		Set<String> keys = fc.getKeys(false);
		for (String key : keys) {
			options.put(key, fc.getString(key));
		}
	}

	public static String getLang(String key) {
		String option = options.get(key);
		if (option != null) {
			return ChatColor.translateAlternateColorCodes('&', option);
		}
		return "";
	}
	
	public static String colorString(String str) {
		return ChatColor.translateAlternateColorCodes('&', str);
	}
}
