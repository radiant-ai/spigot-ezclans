package com.github.radiant.ezclans.db;

import java.util.Map;
import java.util.UUID;

import com.github.radiant.ezclans.core.Clan;

public interface IDBConnector {
	public void loadClans();
	public boolean saveClans(Map<UUID, Clan> clans);
}
