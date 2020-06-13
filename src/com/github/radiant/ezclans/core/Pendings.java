package com.github.radiant.ezclans.core;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Pendings {
	public static Map<UUID, String> pending = new HashMap<UUID, String>();
	public static Map<UUID, UUID> disbandPending = new HashMap<UUID, UUID>();
	public static Map<UUID, TeleportPlayerInfo> pendingTeleport = new HashMap<UUID, TeleportPlayerInfo>();
	public static Map<UUID, Clan> pendingInvite = new HashMap<UUID, Clan>();
	
	public static String getPending(UUID playerId) {
		return pending.get(playerId);
	}
	
	public static void setPending(UUID playerId, String name) {
		pending.put(playerId, name);
	}
	
	public static void removePending(UUID playerId) {
		pending.remove(playerId);
	}
	
	public static UUID getDisbandPending(UUID playerId) {
		return disbandPending.get(playerId);
	}
	
	public static void setDisbandPending(UUID playerId, UUID clanId) {
		disbandPending.put(playerId, clanId);
	}
	
	public static void removeDisbandPending(UUID playerId) {
		disbandPending.remove(playerId);
	}
	
	public static TeleportPlayerInfo getTeleportPending(UUID playerId) {
		return pendingTeleport.get(playerId);
	}
	
	public static void setTeleportPending(UUID playerId, TeleportPlayerInfo info) {
		pendingTeleport.put(playerId, info);
	}
	
	public static void removeTeleportPending(UUID playerId) {
		pendingTeleport.remove(playerId);
	}
	
	public static Clan getInvitePending(UUID playerId) {
		return pendingInvite.get(playerId);
	}
	
	public static void setInvitePending(UUID playerId, Clan clan) {
		pendingInvite.put(playerId, clan);
	}
	
	public static void removeInvitePending(UUID playerId) {
		pendingInvite.remove(playerId);
	}
}
