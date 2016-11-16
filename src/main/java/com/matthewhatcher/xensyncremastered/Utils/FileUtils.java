package com.matthewhatcher.xensyncremastered.Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.matthewhatcher.xensyncremastered.XenSync;

public class FileUtils {
	public void firstRun() {
		try {
			XenSync plugin = XenSync.getInstance();
			
			if(!plugin.getDataFolder().exists()) {
				plugin.getDataFolder().mkdirs();
			}
			
			File file = new File(plugin.getDataFolder(), "config.yml");
			
			if(!file.exists()) {
				plugin.saveDefaultConfig();
			}
			
		} catch(Exception e) {
			XenSync.logger.severe("XenSync is shutting down because it either can't read the config or can't write it.");
			XenSync.getInstance().getPluginLoader().disablePlugin(XenSync.getInstance());
		}
	}
	
	public HashMap<String, String> getGroupConversions() throws IOException {
		HashMap<String, String> conversions = new HashMap<>();
		File file = new File(XenSync.getInstance().getDataFolder(), "groupconversions");
		String line;
		
		if(!file.exists())
			file.createNewFile();
		
		BufferedReader reader = new BufferedReader(new FileReader(file));
		
		while((line = reader.readLine()) != null) {
			conversions.put(line.split(":")[0], line.split(":")[1]);
		}
		
		reader.close();
		
		return conversions;
	}
	
	public ArrayList<String> getGroupExecptions() throws IOException {
		ArrayList<String> exceptions = new ArrayList<>();
		File file = new File(XenSync.getInstance().getDataFolder(), "groupconversions");
		String line;
		
		if(!file.exists())
			file.createNewFile();
		
		BufferedReader reader = new BufferedReader(new FileReader(file));
		
		while((line = reader.readLine()) != null) {
			exceptions.add(line);
		}
		
		reader.close();
		
		return exceptions;
	}
	
	public ArrayList<String> getPlayerExecptions() throws IOException {
		ArrayList<String> exceptions = new ArrayList<>();
		File file = new File(XenSync.getInstance().getDataFolder(), "groupconversions");
		String line;
		
		if(!file.exists())
			file.createNewFile();
		
		BufferedReader reader = new BufferedReader(new FileReader(file));
		
		while((line = reader.readLine()) != null) {
			exceptions.add(line);
		}
		
		reader.close();
		
		return exceptions;
	}
}
