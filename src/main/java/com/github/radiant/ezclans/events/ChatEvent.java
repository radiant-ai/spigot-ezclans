package com.github.radiant.ezclans.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.github.radiant.ezclans.EzClans;

public class ChatEvent implements Listener {
	
	private EzClans plugin;
	
	public ChatEvent(EzClans plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerChat(AsyncPlayerChatEvent e) {
		Player p = e.getPlayer();
		String message = e.getMessage();
		if (message.charAt(0) == ';') {
			e.setCancelled(true);
			Bukkit.getScheduler().callSyncMethod(plugin, () -> p.performCommand("clan chat "+message.substring(1)));
		}
	}
}
