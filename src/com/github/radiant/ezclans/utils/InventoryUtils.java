package com.github.radiant.ezclans.utils;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

public class InventoryUtils {
	public static boolean testSavable(ItemStack item) {
		try {
			if (item.getType()==Material.WRITTEN_BOOK || item.getType()==Material.WRITABLE_BOOK) {
				File tempFile = new File(Bukkit.getPluginManager().getPlugin("EzClans").getDataFolder(), "testfile.yml");
				YamlConfiguration customConfig= new YamlConfiguration();
				customConfig.set("item", item);
				customConfig.save(tempFile);
				tempFile.delete();
			}
			return true;
		}
		catch (Exception e) {
			return false;
		}
	}
}
