package com.github.radiant.ezclans.core;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class HomeTask implements Runnable {
	
	private Player player;
	private Location location;
	
	public HomeTask(Player player, Location location) {
		this.player = player;
		this.location = location;
	}

	@Override
	public void run() {
		if (player.isOnline()) {
			player.teleport(location);
			Pendings.removeTeleportPending(player.getUniqueId());
		}
	}

}
