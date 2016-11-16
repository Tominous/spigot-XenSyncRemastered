package com.matthewhatcher.xensyncremastered.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import com.matthewhatcher.xensyncremastered.XenSync;

public class SyncListener implements Listener {
	@EventHandler
	public void onLogin(PlayerLoginEvent e) {
		XenSync.getInstance().playerUtils.sync(e.getPlayer());
	}
}
