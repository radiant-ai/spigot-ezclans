package com.github.radiant.ezclans.events;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
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
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerChat(AsyncChatEvent e) {
		Player p = e.getPlayer();
		var plainSerializer = PlainTextComponentSerializer.plainText();
		var message = plainSerializer.serialize(e.message());
		if (message.charAt(0) == ';') {
			e.setCancelled(true);
			Bukkit.getScheduler().callSyncMethod(plugin, () -> p.performCommand("clan chat "+message.substring(1)));
		}
	}
}
