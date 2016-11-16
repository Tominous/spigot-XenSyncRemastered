package com.matthewhatcher.xensyncremastered;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.matthewhatcher.xensyncremastered.Listeners.SyncListener;
import com.matthewhatcher.xensyncremastered.Tasks.SyncTask;
import com.matthewhatcher.xensyncremastered.Utils.DatabaseUtils;
import com.matthewhatcher.xensyncremastered.Utils.FileUtils;
import com.matthewhatcher.xensyncremastered.Utils.PlayerUtils;

import net.milkbowl.vault.permission.Permission;

public class XenSync extends JavaPlugin {
	public FileUtils fileUtils;
	public PlayerUtils playerUtils;
	public DatabaseUtils databaseUtils;
	public Permission permission = null;
	public ArrayList<String> playerExceptions, groupExceptions;
	public HashMap<String, String> groupConversions;
	private TimerTask syncTask;
	
	public static Logger logger;
	private static XenSync instance;
	
	public static XenSync getInstance() {
		return instance;
	}
	
	@Override
	public void onEnable() {
		instance = this;
		logger = this.getLogger();
		
		this.setupPermissions();
		
		playerUtils = new PlayerUtils();
		databaseUtils = new DatabaseUtils();
		fileUtils = new FileUtils();
		fileUtils.firstRun();
		
		try {
			playerExceptions = fileUtils.getPlayerExecptions();
			groupExceptions = fileUtils.getGroupExecptions();
			groupConversions = fileUtils.getGroupConversions();
		} catch (IOException e) {
			logger.severe("Could not load conversions/exceptions. XenSync is shutting down.");
			getInstance().getPluginLoader().disablePlugin(getInstance());
		}
		
		this.syncTask = new SyncTask();
		new Timer().schedule(this.syncTask, TimeUnit.MINUTES.toMillis(getInstance().getConfig().getInt("sync-task-delay")));
		
		getInstance().getServer().getPluginManager().registerEvents(new SyncListener(), getInstance());
		
		super.onEnable();
	}
	
	@Override
	public void onDisable() {
		if(this.syncTask != null)
			this.syncTask.cancel();
		
		super.onDisable();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private boolean setupPermissions() {
		RegisteredServiceProvider<?> permissionProvider = getInstance().getServer().getServicesManager().getRegistration((Class) Permission.class);
		
		if(permissionProvider == null) {
			logger.severe("Vault is not present, please install vault. XenSync is shutting down.");
			getInstance().getPluginLoader().disablePlugin(getInstance());
			return false;
		}
		
		permission = (Permission) permissionProvider.getProvider();
		return true;
	}
}
