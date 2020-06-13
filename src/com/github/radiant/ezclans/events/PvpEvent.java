package com.github.radiant.ezclans.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.projectiles.ProjectileSource;

import com.github.radiant.ezclans.core.ClanMember;
import com.github.radiant.ezclans.core.Clans;

public class PvpEvent implements Listener {
	
	@EventHandler(priority = EventPriority.HIGH)
    public void EntityDamageByEntityEvent(final EntityDamageByEntityEvent event) {
		Player attackingPlayer = null;
        Player attackedPlayer = null;
        if (event.getEntity() instanceof Player) {
            Entity damager = event.getDamager();
            attackingPlayer = null;
            attackedPlayer = (Player) event.getEntity();
            if (damager instanceof Arrow) {
            	Arrow arrow = (Arrow) damager;
            	ProjectileSource ps = arrow.getShooter();
            	if (ps instanceof Player) {
            		attackingPlayer = (Player) ps;
            	}
            }
            else if (damager instanceof ThrownPotion) {
            	ThrownPotion potion = (ThrownPotion) damager;
            	ProjectileSource ps = potion.getShooter();
            	if (ps instanceof Player) {
            		attackingPlayer = (Player) ps;
            	}
            }
            else if (damager instanceof Player) {
                attackingPlayer = (Player) damager;
                if (!(event.getEntity() instanceof Player)) {
                	return;
                }
            }
            else {
            	return;
            }
            if (attackingPlayer!=null && attackedPlayer!=null) {
            	ClanMember attacked = Clans.getMember(attackedPlayer.getUniqueId());
            	if (attacked == null) {
            		return;
            	}
            	ClanMember attacking = Clans.getMember(attackingPlayer.getUniqueId());
            	if (attacking == null) {
            		return;
            	}
            	if (attacked.getClan() != attacking.getClan()) {
            		return;
            	}
            	if (!attacked.isClanPvp() || !attacking.isClanPvp()) {
            		event.setCancelled(true);
            	}
            }
        }
    }
}
