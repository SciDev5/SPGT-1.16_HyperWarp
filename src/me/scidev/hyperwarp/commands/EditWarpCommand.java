package me.scidev.hyperwarp.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import me.scidev.hyperwarp.HyperWarp;
import me.scidev.hyperwarp.Warp;
import me.scidev.hyperwarp.i10n.TextGetterUtil;
import net.md_5.bungee.api.ChatColor;

public class EditWarpCommand implements CommandExecutor, TabCompleter {
	@Override
	public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
		if (args.length < 2) {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', TextGetterUtil.getTextSectionWithCommandAlias("warps.edit.error.commandSyntax.base", alias)));
			return true;
		}
		Warp warp = HyperWarp.getWarpByName(args[0]);
		if (warp == null) {
			Map<String, String> textObjects = new HashMap<>();
			textObjects.put("WARPNAME",args[0]);
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', TextGetterUtil.getTextSectionWithOtherText("warps.edit.error.noSuchWarp", textObjects)));
			return true;
		}
		
		switch (args[1]) {
		case "rename":
			if (args.length != 3) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', TextGetterUtil.getTextSectionWithCommandAlias("warps.edit.error.commandSyntax.name", alias)));
				break;
			}
			if (HyperWarp.getWarpByName(args[2]) != null) {
				Map<String, String> textObjects = new HashMap<>();
				textObjects.put("WARPNAME",args[2]);
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', TextGetterUtil.getTextSectionWithOtherText("warps.edit.error.warpNameTaken", textObjects)));
				break;
			}
			warp.name = args[2];
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', TextGetterUtil.getTextSectionWithWarpInfo("warps.edit.success.name", warp)));
			break;
			
		case "position":
			if (args.length != 5) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', TextGetterUtil.getTextSectionWithCommandAlias("warps.edit.error.commandSyntax.position", alias)));
				break;
			}
			double x,y,z;
			try {
				x = Double.parseDouble(args[2]);
				y = Double.parseDouble(args[3]);
				z = Double.parseDouble(args[4]);
			} catch (NumberFormatException e) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', TextGetterUtil.getTextSectionWithCommandAlias("warps.edit.error.commandSyntax.position", alias)));
				break;
			}
			warp.destination.set(x, y, z);
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', TextGetterUtil.getTextSectionWithWarpInfo("warps.edit.success.position", warp)));
			break;
			
		case "rotation":
			if (args.length != 4) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', TextGetterUtil.getTextSectionWithCommandAlias("warps.edit.error.commandSyntax.rotation", alias)));
				break;
			}
			float pitch,yaw;
			try {
				pitch = Float.parseFloat(args[2]);
				yaw = Float.parseFloat(args[3]);
			} catch (NumberFormatException e) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', TextGetterUtil.getTextSectionWithCommandAlias("warps.edit.error.commandSyntax.rotation", alias)));
				break;
			}
			warp.destination.setPitch(pitch);
			warp.destination.setYaw(yaw);
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', TextGetterUtil.getTextSectionWithWarpInfo("warps.edit.success.rotation", warp)));
			break;
			
		case "world":
			if (args.length != 3) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', TextGetterUtil.getTextSectionWithCommandAlias("warps.edit.error.commandSyntax.world", alias)));
				break;
			}
			World world = HyperWarp.instance.getServer().getWorld(args[2]);
			if (world == null) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', TextGetterUtil.getTextSectionWithCommandAlias("warps.edit.error.commandSyntax.world", alias)));
				break;
			}
			warp.destination.setWorld(world);
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', TextGetterUtil.getTextSectionWithWarpInfo("warps.edit.success.world", warp)));
			break;
			
		case "permission":
			if (args.length != 3) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', TextGetterUtil.getTextSectionWithCommandAlias("warps.edit.error.commandSyntax.permission", alias)));
				break;
			}
			Permission permission = HyperWarp.instance.getServer().getPluginManager().getPermission(args[2]);
			if (!args[2].equalsIgnoreCase("none") && permission == null) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', TextGetterUtil.getTextSectionWithCommandAlias("warps.edit.error.commandSyntax.permission", alias)));
			}
			warp.setPermission(permission);
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', TextGetterUtil.getTextSectionWithWarpInfo("warps.edit.success.permission", warp)));
			break;
			
		case "description":
			if (args.length == 2) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', TextGetterUtil.getTextSectionWithCommandAlias("warps.edit.error.commandSyntax.description", alias)));
				break;
			}
			String description = "";
			if (args[2] != "empty" || args.length != 3) {
				for (int i = 2; i < args.length; i++) {
					description += " "+args[i];
				}
				description = description.substring(1);
			}
			warp.setDescription(description);
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', TextGetterUtil.getTextSectionWithWarpInfo("warps.edit.success.description", warp)));
			break;
			
		default:
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', TextGetterUtil.getTextSectionWithCommandAlias("warps.edit.error.commandSyntax.base", alias)));
			break;
		}
		
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		List<String> list = new ArrayList<String>();
		if (args.length == 1) {
			for (Warp warp : HyperWarp.warps)
				if (warp.name.startsWith(args[0]))
					list.add(warp.name);
		} else if (args.length == 2) {
			list.add("rename");
			list.add("position");
			list.add("rotation");
			list.add("world");
			list.add("permission");
			list.add("description");
		} else if (args.length >= 3) {
			Player player = null;
			Location playerLocation = null;
			String xstr = "0";
			String ystr = "0";
			String zstr = "0";
			String pitchstr = "0";
			String yawstr = "0";
			if (sender instanceof Player) {
				player = (Player) sender;
				playerLocation = player.getLocation();
				xstr = TextGetterUtil.doubleToFixed(playerLocation.getX(), 3);
				ystr = TextGetterUtil.doubleToFixed(playerLocation.getY(), 3);
				zstr = TextGetterUtil.doubleToFixed(playerLocation.getZ(), 3);
				pitchstr = TextGetterUtil.floatToFixed(playerLocation.getPitch(), 3);
				yawstr = TextGetterUtil.floatToFixed(playerLocation.getYaw(), 3);
			}
			switch (args[1]) {
			case "rename": 
				break;
				
			case "position":
				if (args.length == 3) { // x
					if (args[2].length() == 0) list.add(xstr + " " + ystr + " " + zstr);
					else                       list.add(args[2] + " "+ ystr + " " + zstr);
				} else if (args.length == 4) { // y
					if (args[3].length() == 0) list.add(ystr + " " + zstr);
					else                       list.add(args[3] + " " + zstr);
				} else if (args.length == 5) { // z
					if (args[4].length() == 0) list.add(zstr);
					else                       list.add(args[4]);
				}
				break;
				
			case "rotation":
				if (args.length == 3) { // pitch
					if (args[2].length() == 0) list.add(pitchstr + " " + yawstr);
					else                       list.add(args[2] + " " + yawstr);
				} else if (args.length == 4) { // yaw
					if (args[3].length() == 0) list.add(yawstr);
					else                       list.add(args[3]);
				}
				break;
				
			case "world": 
				for (World world : HyperWarp.instance.getServer().getWorlds())
					if (world.getName().startsWith(args[2]))
						list.add(world.getName());
				break;
				
			case "permission":
				if ("none".startsWith(args[2]))
					list.add("none");
				for (Permission permission : HyperWarp.instance.getServer().getPluginManager().getPermissions())
					if (permission.getName().startsWith(args[2]))
						list.add(permission.getName()); 
				break;
				
			case "description": 
				break;
			}
		}
		return list;
	}
}
