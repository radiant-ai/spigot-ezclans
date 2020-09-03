package com.github.radiant.ezclans.core;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.github.radiant.ezclans.lang.Lang;
import com.github.radiant.ezclans.logs.EzLogs;
import com.github.radiant.ezclans.utils.InventoryUtils;

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
	private int bank;
	private int level;
	private Inventory storage;
	
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
		this.bank = 0;
		this.level = 1;
		this.storage = Bukkit.createInventory(null, 9, Lang.getLang("clan_storage"));
	}

	public Clan(UUID id, String name, String tag, List<ClanMember> members, ClanMember leader, Date creationDate, Location home, int bank, int level, Inventory storage) {
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
		this.bank = bank;
		this.level = level;
		this.storage = storage;
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
	
	public int getBank() {
		return bank;
	}

	public void setBank(int bank) {
		this.bank = bank;
	}
	
	public void withdraw(int amt) {
		if (amt > bank) {
			bank = 0;
		}
		else {
			bank = bank - amt;
		}
	}
	
	public void deposit(int amt) {
		bank = bank + amt;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public Inventory getStorage() {
		return storage;
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
	
	public String getOnlineList() {
		String result = "";
		for (ClanMember cm : members) {
			if (Bukkit.getPlayer(cm.getUuid()) != null && Bukkit.getPlayer(cm.getUuid()).isOnline()) {
				result+=cm.getName()+" ";
			}
		}
		if (result.isEmpty()) {
			result = Lang.getLang("nobody");
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
	
	public void upgrade() throws ClanException {
		int aimingLvl = level + 1;
		if (aimingLvl <= 3) {
			int cost = Clans.getClanCost(aimingLvl);
			if (cost<=bank) {
				clanMessage(Lang.getLang("clan_upgrade"));
				bank-=cost;
				EzLogs.logBalance(new ClanMember(null, "LVL"+aimingLvl, this), cost, "UPGRADE");
				upgradeStorage(aimingLvl);
				level = aimingLvl;
			}
			else {
				throw new ClanException(Lang.getLang("upgrade_cost")+cost+"$");
			}
		}
		else {
			throw new ClanException(Lang.getLang("upgrade_max"));
		}
	}
	
	public void closeStorageForAll() {
		List<HumanEntity> viewers = storage.getViewers();
		List<HumanEntity> viewersCopy = new LinkedList<HumanEntity>();
		viewersCopy.addAll(viewers);
		for (HumanEntity viewer : viewersCopy) {
			viewer.closeInventory();
		}
	}
	
	private void upgradeStorage(int lvl) {
		Inventory newStorage = null;
		closeStorageForAll();
		if (lvl==2) {
			newStorage = Bukkit.createInventory(null, 27, Lang.getLang("clan_storage"));
		}
		else if (lvl==3) {
			newStorage = Bukkit.createInventory(null, 54, Lang.getLang("clan_storage"));
		}
		newStorage.setContents(storage.getContents());
		storage = newStorage;
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
		result.put("bank", ""+bank);
		result.put("level", ""+level);
		ItemStack[] stacks = storage.getContents();
		for (int i = 0; i < stacks.length; i++) {
			if (!InventoryUtils.testSavable(stacks[i])) {
				stacks[i] = null;
				storage.setItem(i, null);
			}
		}
		result.put("storage",stacks);
		return result;
	}
	
	public static Clan deserialize(Map<String, Object> map) {
		UUID uuid = UUID.fromString((String)map.get("id"));
		String n = (String) map.get("name");
		String t = (String) map.get("tag");
		Date date = new Date(Long.parseLong((String) map.get("creationdate")));
		UUID leaderUUID = UUID.fromString((String) map.get("leader"));
		
		int bank = 0;
		Object bankRaw = map.get("bank");
		if (bankRaw!=null) {
			bank = Integer.parseInt((String) bankRaw);
		}
		
		int level = 1;
		Object levelRaw = map.get("level");
		if (levelRaw!=null) {
			levelRaw = Integer.parseInt((String) levelRaw);
			level = (int) levelRaw;
		}
		
		
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> mapMembers = (List<Map<String, Object>>) map.get("members");
		List<ClanMember> memberList = new ArrayList<ClanMember>();
		Object home_o = map.get("home");
		Location clanHome = null;
		if (home_o instanceof Location) {
			clanHome = (Location) home_o;
		}
		String storageName = Lang.getLang("clan_storage");
		Inventory inv = Bukkit.createInventory(null, 9, storageName);
		if (level==2) {
			inv = Bukkit.createInventory(null, 27, storageName);
		}
		else if (level==3) {
			inv = Bukkit.createInventory(null, 54, storageName);
		}
		ArrayList<ItemStack> stacks = (ArrayList<ItemStack>) map.get("storage");
		if (stacks != null) {
			for (int i = 0; i<stacks.size() && i<inv.getSize(); i++) {
				inv.setItem(i, stacks.get(i));
			}
		}
		
		Clan result = new Clan(uuid, n, t, null, null, date, clanHome, bank, level, inv);
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
