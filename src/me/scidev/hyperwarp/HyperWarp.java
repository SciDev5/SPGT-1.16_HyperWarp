package me.scidev.hyperwarp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import me.scidev.hyperwarp.commands.DelWarpCommand;
import me.scidev.hyperwarp.commands.DoWarpCommand;
import me.scidev.hyperwarp.commands.EditWarpCommand;
import me.scidev.hyperwarp.commands.GetWarpCommand;
import me.scidev.hyperwarp.commands.ListWarpsCommand;
import me.scidev.hyperwarp.commands.SetWarpCommand;
import me.scidev.hyperwarp.i10n.TextGetterUtil;

public class HyperWarp extends JavaPlugin {
	
	public static final Logger LOGGER = LogManager.getLogger("HyperWarp");
	
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
		
		loadWarps();
	}
	
	@Override
	public void onDisable() {
		saveWarps();
	}
	
	public void registerCommands() {
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
	
	private void loadWarps() {
		ConfigurationSection warpSection = getConfig().getConfigurationSection("warps");
		if (warpSection == null) return;
		Set<String> keys = warpSection.getKeys(false);
		for (String key : keys) {
			// Error text.
			Map<String, String> failToLoadMap = new HashMap<String, String>();
			failToLoadMap.put("WARPNAME",key);
			String failToLoadError = TextGetterUtil.getTextSectionWithOtherText("couldNotLoadWarp",failToLoadMap);
			
			// Try to load data
			ConfigurationSection warpData = warpSection.getConfigurationSection(key);
			if (warpData == null) { // key doesn't lead to ConfigurationSection.
				LOGGER.log(Level.ERROR, failToLoadError);
				continue;
			}
			
			Location location = warpData.getLocation("location", null);
			if (location == null) { // no location data.
				LOGGER.log(Level.ERROR, failToLoadError);
				continue;
			}
			
			String permissionStr = warpData.getString("permission", null);
			String description = warpData.getString("description", "");
			
			Warp warp = new Warp(key, location);
			warp.setPermission(permissionStr);
			warp.setDescription(description);
			warps.add(warp);
		}
	}
	
	private void saveWarps() { 
		getConfig().set("warps", null);
		ConfigurationSection warpSection = getConfig().createSection("warps");
		for (Warp warp : warps) {
			ConfigurationSection warpData = warpSection.createSection(warp.name);
			warpData.set("location", warp.destination);
			if (warp.description.length() > 0)
				warpData.set("description", warp.description);
			if (warp.getPermission() != null)
				warpData.set("permission", warp.getPermission().getName());
		}
		saveConfig();
	}
	
	public static Warp getWarpByName(String name) {
		for (Warp warp : warps)
			if (warp.name.equalsIgnoreCase(name))
				return warp;
		return null;
	}
}
