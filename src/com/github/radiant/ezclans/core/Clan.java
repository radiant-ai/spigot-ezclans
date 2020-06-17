package com.github.radiant.ezclans.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.entity.Player;

import com.github.radiant.ezclans.lang.Lang;

@SerializableAs("Clan")
public class Clan implements Cloneable, ConfigurationSerializable {
	private final UUID id;
	private String name;
	private String tag;
	private List<ClanMember> members;
	private ClanMember leader;
	private final Date creationDate;
	private Location home;
	private boolean disbanded;
	
	public Clan(String name) {
		super();
		this.name = name;
		this.id = UUID.randomUUID();
		this.tag = "";
		this.members = new ArrayList<ClanMember>();
		this.leader = null;
		this.creationDate = new Date();
		this.home = null;
		this.disbanded = false;
	}

	public Clan(UUID id, String name, String tag, List<ClanMember> members, ClanMember leader, Date creationDate, Location home) {
		super();
		this.id = id;
		this.name = name;
		this.tag = tag;
		if (members == null) {
			this.members = new ArrayList<ClanMember>();
		}
		else {
			this.members = members;
		}
		this.leader = leader;
		this.creationDate = creationDate;
		this.home = home;
		this.disbanded = false;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public ClanMember getLeader() {
		return leader;
	}

	public void setLeader(ClanMember member) {
		if (members.contains(member)) {
			if (this.leader != null) {
				this.leader.setStatus("member");
			}
			this.leader = member;
			member.setStatus("leader");
		}
	}

	public UUID getId() {
		return id;
	}

	public List<ClanMember> getMembers() {
		return members;
	}

	public void setMembers(List<ClanMember> members) {
		this.members = members;
	}

	public Date getCreationDate() {
		return creationDate;
	}
	
	public Location getHome() {
		return home;
	}

	public void setHome(Location home) {
		this.home = home.clone();
	}

	public int clanSize() {
		return members.size();
	}
	
	public ClanMember addPlayer(Player p) {
		ClanMember member = new ClanMember(p, this);
		members.add(member);
		return member;
	}
	
	public ClanMember getMemberByName(String name) {
		return members.stream().filter(m -> m.getName().equals(name)).findAny().orElse(null);
	}
	
	public boolean removeMember(ClanMember member) {
		int index = members.indexOf(member);
		if (index >= 0) {
			members.remove(index);
			Clans.removeMember(member);
			return true;
		}
		return false;
	}
	
	public boolean isDisbanded() {
		return disbanded;
	}

	public void setDisbanded(boolean disbanded) {
		this.disbanded = disbanded;
	}
	
	public String getMemberList() {
		String result = "";
		for (ClanMember cm : members) {
			String color = "&r";
			if (cm.isLeader()) {
				color = "&a&l";
			}
			else if (cm.isModerator()) {
				color = "&a";
			}
			result+=color+cm.getName()+"&r ";
		}
		return result;
	}
	
	public void clanMessage(ClanMember cm, String msg) {
		for (ClanMember member : members) {
			Player p = Bukkit.getPlayer(member.getUuid());
			if (p != null && p.isOnline()) {
				p.sendMessage(Lang.colorString(String.format(Clans.clanChatFormat, cm.getName(), msg)));
			}
		}
	}
	
	public void clanMessage(String msg) {
		for (ClanMember member : members) {
			Player p = Bukkit.getPlayer(member.getUuid());
			if (p != null && p.isOnline()) {
				p.sendMessage(Lang.colorString(msg));
			}
		}
	}
	
	public String shortEntry() {
		return String.format(Lang.getLang("list_clan"), name, tag);
	}

	@Override
	public Map<String, Object> serialize() {
		LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>();
		result.put("id", id.toString());
		result.put("name", name);
		result.put("tag", tag);
		ArrayList<Map<String, Object>> memberMap = new ArrayList<>();
		for (ClanMember cm : members) {
			memberMap.add(cm.serialize());
		}
		result.put("members", memberMap);
		result.put("leader", leader.getUuid().toString());
		result.put("creationdate", ""+creationDate.getTime());
		result.put("home", home==null ? "null" : home);
		return result;
	}
	
	public static Clan deserialize(Map<String, Object> map) {
		UUID uuid = UUID.fromString((String)map.get("id"));
		String n = (String) map.get("name");
		String t = (String) map.get("tag");
		Date date = new Date(Long.parseLong((String) map.get("creationdate")));
		UUID leaderUUID = UUID.fromString((String) map.get("leader"));
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> mapMembers = (List<Map<String, Object>>) map.get("members");
		List<ClanMember> memberList = new ArrayList<ClanMember>();
		Object home_o = map.get("home");
		Location clanHome = null;
		if (home_o instanceof Location) {
			clanHome = (Location) home_o;
		}
		Clan result = new Clan(uuid, n, t, null, null, date, clanHome);
		ClanMember leaderMember = null;
		for (Map<String, Object> m : mapMembers) {
			ClanMember mem = ClanMember.deserialize(m, result);
			memberList.add(mem);
			if (mem.getUuid().equals(leaderUUID)) {
				leaderMember = mem;
			}
		}
		result.setMembers(memberList);
		result.setLeader(leaderMember);
		return result;
	}
}
