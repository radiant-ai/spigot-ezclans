package com.github.radiant.ezclans.integrations;

import org.bukkit.entity.Player;

import com.github.radiant.ezclans.EzClans;
import com.github.radiant.ezclans.core.Clan;
import com.github.radiant.ezclans.core.ClanMember;
import com.github.radiant.ezclans.core.Clans;
import com.github.radiant.ezclans.lang.Lang;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;

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
		if(identifier.equals("clan_name")){
			ClanMember member = Clans.getMember(player.getUniqueId());
			if (member != null) {
				Clan clan = member.getClan();
				return Lang.colorString(clan.getName());
			}
			else {
				return "no_clan";
			}
        }

	    // %ezclans_clan_tag%
	    if(identifier.equals("clan_tag")){
	    	ClanMember member = Clans.getMember(player.getUniqueId());
			if (member != null) {
				Clan clan = member.getClan();
				return Lang.colorString(clan.getTag());
			}
			else {
				return "no_clan";
			}
	    }
        
        return null;
    }
}
