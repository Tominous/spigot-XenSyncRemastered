package com.matthewhatcher.xensyncremastered.Tasks;

import java.util.TimerTask;

import org.bukkit.entity.Player;
import com.matthewhatcher.xensyncremastered.XenSync;

public class SyncTask extends TimerTask {
	
	@Override
	public void run() {
		for(Object player : XenSync.getInstance().getServer().getOnlinePlayers()) {
			Player p = (Player)player;
			
			XenSync.getInstance().playerUtils.sync(p);
		}
	}

}
