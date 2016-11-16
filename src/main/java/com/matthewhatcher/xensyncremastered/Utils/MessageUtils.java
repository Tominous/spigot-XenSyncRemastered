package com.matthewhatcher.xensyncremastered.Utils;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MessageUtils {
	private static String prefix = "&c[XenSync Remastered] &b";
	
	public static void send(Player p, String m) {
		String message = prefix + m;
		p.sendMessage(message.replaceAll("&", "§"));
	}
	
	public static void send(CommandSender s, String m) {
		String message = prefix + m;
		s.sendMessage(message.replaceAll("&", "§"));
	}
}
