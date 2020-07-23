package me.scidev.hyperwarp;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

public class Warp {

	public String name;
	public String description;
	public Location destination;
	private Permission permission;
	private String permissionName;
	
	public Warp(String name, Location destination) {
		this.name = name;
		this.destination = destination;
		this.permission = null;
		this.description = "";
	}
	public Warp setPermission(Permission permission) {
		this.permission = permission;
		if (permission != null)
			this.permissionName = permission.getName();
		else
			this.permissionName = null;
		return this;
	}
	public Warp setPermission(String permission) {
		this.permissionName = permission;
		return this;
	}
	public Warp setDescription(String description) {
		this.description = description;
		return this;
	}
	
	public Permission getPermission() {
		if (this.permission == null && this.permissionName != null)
			this.permission = HyperWarp.instance.getServer().getPluginManager().getPermission(this.permissionName);
		return this.permission;
	}
	
	public boolean teleportPlayer(Player player) {
		if (this.permission != null)
			if (!player.hasPermission(this.getPermission()))
				return false;
		
		player.teleport(this.destination);
		return true;
	}
}
