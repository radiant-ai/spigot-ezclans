package com.github.radiant.ezclans.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import org.bukkit.configuration.ConfigurationSection;

import com.github.radiant.ezclans.EzClans;
import com.github.radiant.ezclans.core.Clan;
import com.github.radiant.ezclans.core.ClanMember;
import com.github.radiant.ezclans.core.Clans;
import com.github.radiant.ezclans.lang.CustomConfig;

public class DBYml implements IDBConnector {

	public CustomConfig clans;
	
	public DBYml(EzClans plugin) {
		clans = new CustomConfig(plugin, "clans.yml");
		clans.reloadCustomConfig();
		clans.scheduleBackup(1000, 1000);
	}

	@Override
	public void loadClans() {
		clans.reloadCustomConfig();
		List<HashMap<String, Object>> list = (List<HashMap<String, Object>>) clans.getCustomConfig().get("clans");
		Map<UUID, Clan> result = new HashMap<UUID, Clan>();
		if (list != null) {
			for (HashMap<String, Object> clanMap : list) {
				Clan clan = Clan.deserialize(clanMap);
				result.put(clan.getId(), clan);
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
				list.add(mapElement.getValue().serialize());
	        }
			clans.getCustomConfig().set("clans",list);
			clans.saveCustomConfig();
			
			return true;
		}
		
		return false;
	}
	
	
}
