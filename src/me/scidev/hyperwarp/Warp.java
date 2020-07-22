package me.scidev.hyperwarp;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

public class Warp {

	public String name;
	public String description;
	public Location destination;
	public Permission permission;
	
	public Warp(String name, Location destination) {
		this.name = name;
		this.destination = destination;
		this.permission = null;
		this.description = "";
	}
	public Warp setPermission(Permission permission) {
		this.permission = permission;
		return this;
	}
	public Warp setDescription(String description) {
		this.description = description;
		return this;
	}
	
	public boolean teleportPlayer(Player player) {
		if (this.permission != null)
			if (!player.hasPermission(this.permission))
				return false;
		
		player.teleport(this.destination);
		return true;
	}
}
