package com.github.radiant.ezclans.core;

import org.bukkit.Location;

public class TeleportPlayerInfo {
	private final int taskId;
	private final Location initialLocation;
	
	public TeleportPlayerInfo(int taskId, Location initialLocation) {
		super();
		this.taskId = taskId;
		this.initialLocation = initialLocation.clone();
	}

	public int getTask() {
		return taskId;
	}

	public Location getInitialLocation() {
		return initialLocation;
	}
	
}
