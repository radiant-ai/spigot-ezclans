package com.github.radiant.ezclans.integrations;

import org.bukkit.entity.Player;

import com.github.radiant.ezclans.EzClans;
import com.github.radiant.ezclans.core.Clan;
import com.github.radiant.ezclans.core.ClanMember;
import com.github.radiant.ezclans.core.Clans;
import com.github.radiant.ezclans.lang.Lang;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.md_5.bungee.api.ChatColor;

public class EzPlaceholder extends PlaceholderExpansion {
	
    private EzClans plugin;


    public EzPlaceholder(EzClans plugin){
        this.plugin = plugin;
    }
    
    @Override
    public boolean persist(){
        return true;
    }


    public boolean canRegister(){
        return true;
    }


    @Override
    public String getAuthor(){
        return plugin.getDescription().getAuthors().toString();
    }


    @Override
    public String getIdentifier(){
        return "ezclans";
    }


    @Override
    public String getVersion(){
        return plugin.getDescription().getVersion();
    }


    @Override
    public String onPlaceholderRequest(Player player, String identifier){

        if(player == null){
            return "";
        }
        
        //%ezclans_clan_name%
        else if(identifier.equals("clan_name")){
			ClanMember member = Clans.getMember(player.getUniqueId());
			if (member != null) {
				Clan clan = member.getClan();
				return Lang.colorString(clan.getName());
			}
			else {
				return "";
			}
        }

	    // %ezclans_clan_tag%
		else if(identifier.equals("clan_tag")){
	    	ClanMember member = Clans.getMember(player.getUniqueId());
			if (member != null) {
				Clan clan = member.getClan();
				return Lang.colorString(clan.getTag());
			}
			else {
				return "";
			}
	    }
	    
	 // %ezclans_clan_online%
	    else if(identifier.equals("clan_online")){
	    	ClanMember member = Clans.getMember(player.getUniqueId());
			if (member != null) {
				Clan clan = member.getClan();
				return clan.getOnlineList().size()+"";
			}
			else {
				return "";
			}
	    }
	    
	 // %ezclans_clan_size%
	    else if(identifier.equals("clan_size")){
	    	ClanMember member = Clans.getMember(player.getUniqueId());
			if (member != null) {
				Clan clan = member.getClan();
				return clan.getMembers().size()+"";
			}
			else {
				return "";
			}
	    }
	    
	 // %ezclans_clan_maxsize%
	    else if(identifier.equals("clan_maxsize")){
	    	ClanMember member = Clans.getMember(player.getUniqueId());
			if (member != null) {
				Clan clan = member.getClan();
				return Clans.getMaxMembers(clan.getLevel())+"";
			}
			else {
				return "";
			}
	    }
	    
	 // %ezclans_clan_leader%
	    else if(identifier.equals("clan_leader")){
	    	ClanMember member = Clans.getMember(player.getUniqueId());
			if (member != null) {
				Clan clan = member.getClan();
				return clan.getLeader().getName();
			}
			else {
				return "";
			}
	    }
	    
	 // %ezclans_clan_bank%
	    else if(identifier.equals("clan_bank")){
	    	ClanMember member = Clans.getMember(player.getUniqueId());
			if (member != null) {
				Clan clan = member.getClan();
				return clan.getBank()+"";
			}
			else {
				return "";
			}
	    }
        
     // %ezclans_clan_maxbank%
	    else if(identifier.equals("clan_maxbank")){
	    	ClanMember member = Clans.getMember(player.getUniqueId());
			if (member != null) {
				Clan clan = member.getClan();
				return Clans.getMaxBank(clan.getLevel())+"";
			}
			else {
				return "";
			}
	    }
        
     // %ezclans_clan_level%
	    else if(identifier.equals("clan_level")){
	    	ClanMember member = Clans.getMember(player.getUniqueId());
			if (member != null) {
				Clan clan = member.getClan();
				return clan.getLevel()+"";
			}
			else {
				return "";
			}
	    }
        
     // %ezclans_clan_pvp%
	    else if(identifier.equals("clan_pvp")){
	    	ClanMember member = Clans.getMember(player.getUniqueId());
			if (member != null) {
				if (member.isClanPvp()) {
					return ChatColor.translateAlternateColorCodes('&', "&a+");
				}
				else {
					return ChatColor.translateAlternateColorCodes('&', "&c-");
				}
			}
			else {
				return "";
			}
	    }

		// %ezclans_clan_color%
		else if(identifier.equals("clan_color")) {
			ClanMember member = Clans.getMember(player.getUniqueId());
			if (member != null) {
				String color = member.getClan().getColor();
				if (!color.isEmpty())
					return String.format("{%s}◆", member.getClan().getColor());
				else
					return "";
			}
			else {
				return "";
			}
		}
        
        return null;
    }
}
