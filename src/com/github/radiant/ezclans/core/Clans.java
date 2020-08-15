package com.github.radiant.ezclans.core;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import com.github.radiant.ezclans.lang.Lang;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class Clans {
	public static Map<UUID, Clan> clans = new HashMap<UUID, Clan>();
	public static Map<UUID,ClanMember> members = new TreeMap<UUID,ClanMember>();
	
	public static double clanCost = 5000;
	public static int homeCd = 4;
	public static String clanChatFormat = "&7[&2ClanChat&7] &r&c%s&r: %s";
	public static int clansPerPage = 8;
	
	public static void loadConfig(FileConfiguration config) {
		clanCost = config.getDouble("clan_creation_cost");
		homeCd = config.getInt("clan_home_cd");
		clanChatFormat = config.getString("clan_msg");
	}
	
	public static void addClan(Clan c) {
		clans.put(c.getId(), c);
	}
	
	public static Clan createClan(String name) {
		Clan clan = new Clan(name);
		clans.put(clan.getId(), clan);
		return clan;
	}
	
	public static Clan getClan(UUID id) {
		return clans.get(id);
	}
	
	public static Clan getClanByName(String name) {
		for (Entry<UUID, Clan> entry : clans.entrySet()) {
	        if (entry.getValue().getName().equals(name)) {
	            return entry.getValue();
	        }
	    }
	    return null;
	}
	
	public static Clan getClanByTag(String tag) {
		for (Entry<UUID, Clan> entry : clans.entrySet()) {
	        if (entry.getValue().getTag().equals(tag)) {
	            return entry.getValue();
	        }
	    }
	    return null;
	}
	
	public static ClanMember getMember(UUID id) {
		return members.get(id);
	}
	
	public static Clan getPlayerClan(Player p) {
		ClanMember member = members.get(p.getUniqueId());
		if (member != null) {
			return member.getClan();
		}
		else {
			return null;
		}
	}
	
	public static void addMember(ClanMember member) {
		members.put(member.getUuid(),member);
	}
	
	public static void removeMember(ClanMember member) {
		members.remove(member.getUuid());
	}

	public static Map<UUID, Clan> getClans() {
		return clans;
	}

	public static void setClans(Map<UUID, Clan> clans) {
		Clans.clans = clans;
	}

	public static Map<UUID, ClanMember> getMembers() {
		return members;
	}

	public static void setMembers(Map<UUID, ClanMember> members) {
		Clans.members = members;
	}
	
	public static void msgInfo(Clan clan, Player p) {
		p.sendMessage(Lang.getLang("clan_info"));
		p.sendMessage(String.format(Lang.getLang("clan_name"), Lang.colorString(clan.getName())));
		p.sendMessage(String.format(Lang.getLang("clan_tag"), Lang.colorString(clan.getTag())));
		p.sendMessage(String.format(Lang.getLang("clan_leader"), clan.getLeader().getName()));
		p.sendMessage(String.format(Lang.getLang("clan_size"), Integer.toString(clan.clanSize())));
	}
	
	public static void msgInfoMember(Clan clan, Player p) {
		p.sendMessage(Lang.getLang("clan_info"));
		p.sendMessage(String.format(Lang.getLang("clan_name"), Lang.colorString(clan.getName())));
		p.sendMessage(String.format(Lang.getLang("clan_tag"), Lang.colorString(clan.getTag())));
		p.sendMessage(String.format(Lang.getLang("clan_leader"), clan.getLeader().getName()));
		p.sendMessage(String.format(Lang.getLang("clan_balance"), clan.getBank()+" $"));
		p.sendMessage(String.format(Lang.getLang("clan_size"), Integer.toString(clan.clanSize())));
		p.sendMessage(Lang.getLang("clan_members")+" "+Lang.colorString(clan.getMemberList()));
	}
	
	public static void msgInfoLeader(Clan clan, Player p) {
		msgInfoMember(clan, p);
	}
	
	public static int clanNumber() {
		return clans.size();
	}
	
	public static void sendClanList(CommandSender s, int page) {
		int maxPages = (int) Math.ceil(((double) clanNumber())/clansPerPage);
		if (page > maxPages) {
			page = maxPages;
		}
		else if (page <= 0) {
			page = 1;
		}
		Iterator<Entry<UUID, Clan>> it = clans.entrySet().iterator();
		int i = 0;
		s.sendMessage(Lang.getLang("list_list")+" "+String.format(Lang.getLang("page"), Integer.toString(page), Integer.toString(maxPages))+":");
	    while (it.hasNext() && i<page*clansPerPage) {
	        Entry<UUID, Clan> pair = it.next();
	        if (i >= (page-1)*clansPerPage) {
	        	Clan clan = pair.getValue();
	        	TextComponent msg = new TextComponent(Lang.colorString(clan.shortEntry()));
	        	msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(Lang.getLang("click_to_info")+" "+Lang.colorString(clan.getName()))));
	        	msg.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/clan info "+clan.getName()));
	        	s.spigot().sendMessage(msg);
	        }
	        i++;
	    }
	    s.sendMessage(Lang.getLang("list_page"));
	}
	
	public static List<String> getClanNames(String startsWith) {
		List<String> clanList = new LinkedList<String>();
		Iterator<Entry<UUID, Clan>> it = clans.entrySet().iterator();
	    while (it.hasNext()) {
	        Entry<UUID, Clan> pair = it.next();
	        String name = pair.getValue().getName();
	        if (name.startsWith(startsWith)) {
	        	clanList.add(name);
	        }
	    }
	    return clanList;
	}
	
	public static void removeClan(UUID id) {
		Clan clan = clans.get(id);
		clan.setDisbanded(true);
		clans.remove(id);
		List<ClanMember> list = clan.getMembers();
		for (ClanMember m : list) {
			members.remove(m.getUuid());
		}
	}
}