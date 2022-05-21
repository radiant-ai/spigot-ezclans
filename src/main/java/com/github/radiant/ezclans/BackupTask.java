package com.github.radiant.ezclans;

import java.io.File;
import java.nio.file.Files;
import java.util.Date;

import org.bukkit.Bukkit;

import com.github.radiant.ezclans.core.Clans;

public class BackupTask implements Runnable {
	private File toBackup;
	public BackupTask(File toBackup) {
		super();
		this.toBackup = toBackup;
	}
	@Override
	public void run() {
		//Bukkit.getLogger().info("[EzClans] Running backup!");
		try {
			EzClans.db.saveClans(Clans.getClans());
		}
		catch (Exception e) {
			Bukkit.getLogger().severe("[EzClans] Could not save clans!");
			e.printStackTrace();
		}
		if (toBackup.exists()) {
			try {
				String newPath = toBackup.getParent()+File.separator+"backups"+File.separator+toBackup.getName()+(new Date()).getTime();
				//Bukkit.getLogger().info("[EzClans] Saving to: "+newPath);
				File target = new File(newPath);
				target.getParentFile().mkdirs();
				Files.copy(toBackup.toPath(), target.toPath());
				//Bukkit.getLogger().info("[EzClans] Backup success!");
			}
			catch (Exception e) {
				Bukkit.getLogger().severe("[EzClans] Backup fail!");
				e.printStackTrace();
			}
		}
	}
}
