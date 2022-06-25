package com.github.radiant.ezclans.events;

import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.github.radiant.ezclans.core.Clans;

public class PvpEvent implements Listener {
	
	//If player receives damage from another player and they are both in same clan and they have clan pvp disabled, cancel the damage.
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void EntityDamageByEntityEvent(final EntityDamageByEntityEvent event) {
		if (!(event.getEntity() instanceof Player attackedPlayer)) {
			return;
		}
		Player attacker = null;

		if (event.getDamager() instanceof Player attackingPlayer) {
			attacker = attackingPlayer;
		}

		if (event.getDamager() instanceof Projectile projectile &&
		projectile.getShooter() instanceof Player shootingPlayer) {
			attacker = shootingPlayer;
		}

		if (attacker == null) {
			return;
		}

		if (!shouldCancelPvp(attacker, attackedPlayer)) {
			return;
		}

		event.setCancelled(true);
	}

	private boolean shouldCancelPvp(Player player1, Player player2) {
		var member1 = Clans.getMember(player1.getUniqueId());
		var member2 = Clans.getMember(player2.getUniqueId());
		if (member1 == null || member2 == null) {
			return false;
		}
		if (member1.getClan() != member2.getClan()) {
			return false;
		}
		return !member1.isClanPvp() || !member2.isClanPvp();
	}
}
