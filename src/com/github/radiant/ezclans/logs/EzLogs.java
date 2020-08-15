package com.github.radiant.ezclans.logs;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import com.github.radiant.ezclans.EzClans;
import com.github.radiant.ezclans.core.Clan;
import com.github.radiant.ezclans.core.ClanMember;

public class EzLogs {
	private static EzClans plugin = null;
	private static ConcurrentLinkedQueue<String> logCache = null;
	private static File logFile = null;
	private static int flushInterval = 200;
	
	public static void initialize(EzClans p) {
		try {
			plugin = p;
			logCache = new ConcurrentLinkedQueue<String>();
			File folder = plugin.getDataFolder();
			logFile = new File(folder, "logs"+File.separator+getLogName());
			new File(logFile.getParent()).mkdirs();
			if (!logFile.exists()) {
				logFile.createNewFile();
			}
			Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, new Runnable() {
				public void run() {
					flush();
				}
			}, flushInterval, flushInterval);
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public static void flush() {
		if (!logCache.isEmpty()) {
			try {
				File folder = plugin.getDataFolder();
				logFile = new File(folder, "logs"+File.separator+getLogName());
				new File(logFile.getParent()).mkdirs();
				if (!logFile.exists()) {
					logFile.createNewFile();
				}
				FileWriter fileWritter = new FileWriter(logFile, true);
		        BufferedWriter bw = new BufferedWriter(fileWritter);
		        
		        while (!logCache.isEmpty()) {
		        	bw.write(logCache.poll());
		        	bw.newLine();
		        }
		        
		        bw.close();
			}
			catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	
	public static void logBalance(ClanMember cm, Clan c, int amt, String action) {
		String entry = "";
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");  
		LocalDateTime now = LocalDateTime.now();
		entry = entry+="["+dtf.format(now)+"] ";
		entry+="Action: "+action;
		entry+=" Amt: "+amt;
		entry+=" Clan: "+c.getTag();
		entry+=" Id: "+c.getId();
		entry+=" Player: "+cm.getName();
		logCache.add(entry);
	}
	
	public static void logStorage(ClanMember cm, Clan c, ItemStack is, String action) {
		String entry = "";
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");  
		LocalDateTime now = LocalDateTime.now();
		entry = entry+="["+dtf.format(now)+"] ";
		entry+="Action: "+action;
		entry+=" Item: "+is.getType().toString();
		entry+=" Amt: "+is.getAmount();
		entry+=" Clan: "+c.getTag();
		entry+=" Id: "+c.getId();
		entry+=" Player: "+cm.getName();
		logCache.add(entry);
	}
	
	private static String getLogName() {
		String result = "";
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");  
		LocalDateTime now = LocalDateTime.now();
		result = dtf.format(now);
		result+="-ezclans.log";
		return result;
	}
}
