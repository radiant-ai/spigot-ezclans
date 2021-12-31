package com.github.radiant.ezclans.events;

import com.github.radiant.ezclans.EzClans;
import com.github.radiant.ezclans.core.ClanMember;
import com.github.radiant.ezclans.core.Clans;
import com.github.radiant.ezclans.lang.Lang;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class LoginEvent implements Listener {
    @EventHandler
    public void playerLogin(PlayerLoginEvent event) {
        ClanMember member = Clans.getMember(event.getPlayer().getUniqueId());
        if (member != null) {
            member.setName(event.getPlayer().getName());
            String message = member.getClan().getLoginMessage();
            if (!message.isEmpty()) {
                String tag = member.getClan().getTag();
                message = Lang.colorString("&f["+tag+"&f]:&r "+message);
                String finalMessage = message;
                Bukkit.getScheduler().runTaskLater(EzClans.plugin, () -> {
                    Player player = Bukkit.getPlayer(member.getUuid());
                    if (player != null && player.isOnline()) {
                        event.getPlayer().sendMessage(finalMessage);
                    }
                }, 30);
            }
        }
    }
}
