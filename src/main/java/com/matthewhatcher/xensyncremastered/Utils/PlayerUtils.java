package com.matthewhatcher.xensyncremastered.Utils;

import java.util.Arrays;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import com.matthewhatcher.xensyncremastered.XenSync;

public class PlayerUtils {
	private FileConfiguration config;
	private boolean nameSystem = true;
	
	public PlayerUtils() {
		this.config = XenSync.getInstance().getConfig();
		this.nameSystem = this.config.getBoolean("player-identifier.name-system");
	}
	
	public String getPlayerIdentifier(Player p) {
		return this.nameSystem ? p.getName() : p.getUniqueId().toString();
	}
	
	public void sync(Player p) {
		XenSync plugin = XenSync.getInstance();
		
		if(!plugin.playerExceptions.contains(p.getName())) {
			List<String> groups = Arrays.asList(plugin.permission.getPlayerGroups(p));
			
			for(String group : plugin.groupExceptions) {
				if(groups.contains(group))
					return;
			}
			
			for(int i = 0; i < groups.size(); i++) {
				String xenGroup = plugin.groupConversions.get(groups.get(i));
				if(xenGroup != null && xenGroup != "") {
					groups.set(i, xenGroup);
				}
			}
			
			int[] groupIds = new int[groups.size()];
			for(int i = 0; i < groupIds.length; i++) {
				int gId = plugin.databaseUtils.getGroupId(groups.get(i));
				if(gId != -1) {
					groupIds[i] = gId;
				}
			}
			
			plugin.databaseUtils.syncPlayer(plugin.databaseUtils.getUserId(plugin.playerUtils.getPlayerIdentifier(p)), 2, groupIds);
		}
	}
}
