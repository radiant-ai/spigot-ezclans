package com.github.radiant.ezclans.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.logging.Level;

import com.github.radiant.ezclans.EzClans;
import com.github.radiant.ezclans.core.Clan;
import com.github.radiant.ezclans.core.ClanMember;
import com.github.radiant.ezclans.core.Clans;
import com.github.radiant.ezclans.lang.CustomConfig;

public class DBYml implements IDBConnector {

	public CustomConfig clans;
	
	public DBYml(EzClans plugin) {
		clans = new CustomConfig(plugin, "clans.yml");
		clans.reloadCustomConfig(false);
		clans.scheduleBackup(2000, 36000);
	}

	@Override
	public void loadClans() {
		List<HashMap<String, Object>> list = (List<HashMap<String, Object>>) clans.getCustomConfig(false).get("clans");
		Map<UUID, Clan> result = new HashMap<UUID, Clan>();
		if (list != null) {
			for (HashMap<String, Object> clanMap : list) {
				try {
					Clan clan = Clan.deserialize(clanMap);
					result.put(clan.getId(), clan);
				}
				catch (Exception exception) {
					EzClans.plugin.getLogger().log(Level.WARNING, "Failed to load clan: " + clanMap.get("name"));
				}
			}
			refreshMembers(result);
			Clans.setClans(result);
		}
	}
	
	public void refreshMembers(Map<UUID, Clan> clans) {
		Iterator<Entry<UUID, Clan>> mapIterator = clans.entrySet().iterator();
		HashMap<UUID, ClanMember> members = new HashMap<UUID, ClanMember>();
		while (mapIterator.hasNext()) { 
			Entry<UUID, Clan> mapElement = (Entry<UUID, Clan>)mapIterator.next(); 
			for (ClanMember m : mapElement.getValue().getMembers()) {
				members.put(m.getUuid(),m);
			}
        }
		Clans.setMembers(members);
	}

	@Override
	public boolean saveClans(Map<UUID, Clan> map) {
		if (map != null && !map.isEmpty()) {
			Iterator<Entry<UUID, Clan>> mapIterator = map.entrySet().iterator();
			ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			while (mapIterator.hasNext()) { 
				Entry<UUID, Clan> mapElement = (Entry<UUID, Clan>)mapIterator.next();
				try {
					list.add(mapElement.getValue().serialize());
				}
				catch (Exception exception) {
					EzClans.plugin.getLogger().log(Level.SEVERE, "Error saving clan " + mapElement.getValue().getName(), exception);
				}
	        }
			clans.getCustomConfig(false).set("clans",list);
			clans.saveCustomConfig();
			
			return true;
		}
		
		return false;
	}
	
	
}
