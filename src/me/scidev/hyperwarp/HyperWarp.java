package me.scidev.hyperwarp;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.java.JavaPlugin;

import me.scidev.hyperwarp.commands.DelWarpCommand;
import me.scidev.hyperwarp.commands.DoWarpCommand;
import me.scidev.hyperwarp.commands.EditWarpCommand;
import me.scidev.hyperwarp.commands.GetWarpCommand;
import me.scidev.hyperwarp.commands.ListWarpsCommand;
import me.scidev.hyperwarp.commands.SetWarpCommand;
import me.scidev.hyperwarp.i10n.TextGetterUtil;

public class HyperWarp extends JavaPlugin {
	
	public static HyperWarp instance;
	
	public static List<Warp> warps;
	
	public HyperWarp() {
		super();
		instance = this;
		warps = new ArrayList<Warp>();
	}
	
	@Override
	public void onEnable() {
		saveDefaultConfig();
		
		registerCommands();
		
		Permission permission = getServer().getPluginManager().getPermission("hyperwarp.commands.setwarp.use");
		warps.add(new Warp("hello",new Location(this.getServer().getWorld("world"), 0, 80, 0)).setPermission(permission));
	}
	
	public void registerCommands() {
		
		// TODO: ADD /setwarp AND /editwarp
		
		getServer().getPluginCommand("listwarps").setExecutor(new ListWarpsCommand());
		getServer().getPluginCommand("getwarp").setExecutor(new GetWarpCommand());
		getServer().getPluginCommand("warp").setExecutor(new DoWarpCommand());
		getServer().getPluginCommand("delwarp").setExecutor(new DelWarpCommand());
		getServer().getPluginCommand("setwarp").setExecutor(new SetWarpCommand());
		getServer().getPluginCommand("editwarp").setExecutor(new EditWarpCommand());
		
		getServer().getPluginCommand("listwarps").setPermissionMessage(TextGetterUtil.getTextSection("noPermission"));
		getServer().getPluginCommand("getwarp").setPermissionMessage(TextGetterUtil.getTextSection("noPermission"));
		getServer().getPluginCommand("warp").setPermissionMessage(TextGetterUtil.getTextSection("noPermission"));
		getServer().getPluginCommand("delwarp").setPermissionMessage(TextGetterUtil.getTextSection("noPermission"));
		getServer().getPluginCommand("setwarp").setPermissionMessage(TextGetterUtil.getTextSection("noPermission"));
		getServer().getPluginCommand("editwarp").setPermissionMessage(TextGetterUtil.getTextSection("noPermission"));
	}
	
	public static Warp getWarpByName(String name) {
		for (Warp warp : warps)
			if (warp.name.equalsIgnoreCase(name))
				return warp;
		return null;
	}
}
