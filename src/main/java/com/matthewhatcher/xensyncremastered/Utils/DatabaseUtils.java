package com.matthewhatcher.xensyncremastered.Utils;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;

import com.google.common.base.Joiner;
import com.google.common.primitives.Ints;
import com.matthewhatcher.xensyncremastered.XenSync;

public class DatabaseUtils {
	private String uri, username, password;
	private FileConfiguration config;
	private boolean debug = false, requireValidEmail = true;
	
	public DatabaseUtils() {
		this.config = XenSync.getInstance().getConfig();
		this.uri = this.config.getString("mysql.uri");
		this.username = this.config.getString("mysql.user");
		this.password = this.config.getString("mysql.password");
		this.debug = this.config.getBoolean("debug");
		this.requireValidEmail = this.config.getBoolean("member-feature.require-valid-email");
	}
	
	public Connection getConnection() throws SQLException {
		return DriverManager.getConnection("jdbc:mysql://" + this.uri, this.username, this.password);
	}
	
	public void setup() {
		try {
			if(!this.isDriverLoaded()) {
				Class.forName("com.mysql.jdbc.Driver").newInstance();
			}
		} catch(ClassNotFoundException | IllegalAccessException | InstantiationException e) {
			XenSync.logger.severe("XenSync shutting down. Can not create database driver instance.");
			XenSync.getInstance().getPluginLoader().disablePlugin(XenSync.getInstance());
		}
	}
	
	public void syncPlayer(int userId, int groupId, int[] forumSecondaryGroupIds) {
		this.syncPlayer(userId, groupId);
		
		List<Integer> ids = Ints.asList(forumSecondaryGroupIds);
		String cdl = Joiner.on((String)",").join((Iterable<?>)ids);
		
		try {
			Connection con = getConnection();
			con.createStatement().executeUpdate("UPDATE `xf_user` SET `secondary_group_ids` = '" + cdl + "' WHERE `user_id` = '" + userId + "'");
			con.close();
		} catch (SQLException e) {
			XenSync.logger.severe("Could not connect to the MySQL database.");
		}
	}
	
	public void syncPlayer(int userId, int groupId) {
		try {
            Connection con = getConnection();
            con.createStatement().executeUpdate("UPDATE `xf_user` SET `user_group_id` = '" + groupId + "' WHERE `user_id` = '" + userId + "'");
            con.createStatement().executeUpdate("UPDATE `xf_user` SET `display_style_group_id` = '" + groupId + "' WHERE `user_id` = '" + userId + "'");
            con.close();
        }
        catch (SQLException e) {
        	XenSync.logger.severe("Could not connect to the MySQL database.");
        }
	}
	
	public boolean isDriverLoaded() {
		boolean loaded = false;
		Enumeration<Driver> e = DriverManager.getDrivers();
		
		while(e.hasMoreElements()) {
			String name = e.nextElement().getClass().getName();
			if(!name.equalsIgnoreCase("com.mysql.jdbc.Driver"))
				continue;
			
			loaded = true;
		}
		
		return loaded;
	}
	
	public int getUserId(String identifier) {
		if(this.config.getBoolean("player-identifier.use-profile-fields"))
			return this.getUserIdNormal(identifier);
		
		return this.getUserIdCustom(identifier);
	}
	
	public int getGroupId(String groupName) {
		groupBlock: {
			try {
				Connection con = getConnection();
				ResultSet rs = con.createStatement().executeQuery("SELECT `user_group_id` FROM `xf_user_group` WHERE `title` = '" + groupName + "'");
				
				if(!rs.first())
					break groupBlock;
				
				if(!rs.next()) {
					rs.first();
					return rs.getInt("user_group_id");
				}
				
				XenSync.logger.warning("Two or more forums have the name " + groupName);
				return -1;
			} catch(SQLException e) {
				XenSync.logger.severe("Could not connect to the MySQL database.");
				return -1;
			}
		}
	
		XenSync.logger.warning("No forum groups have the name " + groupName + ". Are you sure you created one?");
		return -1;
	}
	
	private int getUserIdNormal(String identifier) {
		try {
			int id = -1;
			Connection con = getConnection();
			ResultSet rs = con.createStatement().executeQuery("SELECT `user_id` FROM `xf_user_field_value` WHERE (`field_id` = '" + this.config.getString("player-identifier.field") + "' AND `field_value` = '" + identifier + "')");
			
			if(rs.first()) {
				if(!rs.next()) {
					rs.first();
					id = rs.getInt("user_id");
				} else {
					if(this.debug) {
						XenSync.logger.info("Two or more forum users with the Minecraft name of " + identifier);
					}
					
					id = -1;
				}
			} else {
				if(this.debug)
					XenSync.logger.info("There is no forum user with the Minecraft name of " + identifier);
			}
			
			rs.close();
			con.close();
			
			return id;
		} catch(SQLException e) {
			XenSync.logger.severe("Could not connect to the MySQL database.");
			return -1;
		}
	}
	
	public boolean isEligableForMember(int userId) {
		try {
            Connection con = getConnection();
            ResultSet rs = con.createStatement().executeQuery("SELECT `user_state` FROM `xf_user` WHERE `user_id` = '" + userId + "'");
            if (rs.first() && !rs.next()) {
                rs.first();
                
                boolean valid = rs.getString("user_state").equalsIgnoreCase("valid");
                
                rs.close();
                con.close();
                
                return this.requireValidEmail ? valid : true;
            }
            rs.close();
            con.close();
        } catch (SQLException e) {
        	XenSync.logger.severe("Could not connect to the MySQL database.");
        }
		
        return false;
	}
	
	private int getUserIdCustom(String identifier) {
		try {
			int id = -1;
			Connection con = getConnection();
			ResultSet rs = con.createStatement().executeQuery("SELECT `" + this.config.getString("player-identifier.table-xenid") + "` FROM `" + this.config.getString("player-identifier.database-table") + "` WHERE `" + this.config.getString("player-identifier.table-playerid") + "` = '" + identifier + "'");
			
			if(rs.first()) {
				if(!rs.next()) {
					rs.first();
					id = rs.getInt("user_id");
				} else {
					if(this.debug) {
						XenSync.logger.info("Two or more forum users with the Minecraft name of " + identifier);
					}
					
					id = -1;
				}
			} else {
				if(this.debug)
					XenSync.logger.info("There is no forum user with the Minecraft name of " + identifier);
			}
			
			rs.close();
			con.close();
			
			return id;
		} catch(SQLException e) {
			XenSync.logger.severe("Could not connect to the MySQL database.");
			return -1;
		}
	}
}
