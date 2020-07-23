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

public class SetWarpCommand implements CommandExecutor, TabCompleter {
	@Override
	public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
		if (args.length == 0 || args.length >= 2 && args.length < 4 || args.length == 5) {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', TextGetterUtil.getTextSectionWithCommandAlias("warps.set.error.commandSyntax", alias)));
			return true;
		}
		
		Player player = null;
		if (sender instanceof Player) 
			player = (Player) sender;
		
		if (player == null && args.length < 7) {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', TextGetterUtil.getTextSection("warps.set.error.isConsole")));
			return true;
		}
		
		// Assemble things for the warp.
		
		String name = args[0];
		String description = null;
		Location destination = null;
		Permission permission = null;
		
		if (args.length == 1) {
			destination = player.getLocation().clone();
		} else if (args.length == 4) {
			destination = parseLocation(player, alias, args[1], args[2], args[3], null, null, null);
		} else if (args.length == 6) {
			destination = parseLocation(player, alias, args[1], args[2], args[3], args[4], args[5], null);
		} else if (args.length >= 7) {
			destination = parseLocation(player, alias, args[1], args[2], args[3], args[4], args[5], args[6]);
		}
		if (args.length >= 8) {
			permission = HyperWarp.instance.getServer().getPluginManager().getPermission(args[7]);
			if (!args[7].equalsIgnoreCase("none") && permission == null) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', TextGetterUtil.getTextSection("warps.set.error.permissionInvalid")));
			}
		}
		if (args.length >= 9) {
			description = "";
			for (int i = 8; i < args.length; i++) 
				description += " "+args[i];
			description = description.substring(1);
		}

		// Check things to make sure we can make this warp.
		
		if (destination == null)
			return true;
		Warp oldWarp = HyperWarp.getWarpByName(args[0]);
		if (oldWarp != null) {
			Map<String, String> textObjects = new HashMap<>();
			textObjects.put("WARPNAME",args[0]);
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', TextGetterUtil.getTextSectionWithOtherText("warps.set.error.warpNameTaken", textObjects)));
			return true;
		}
			
		// No issues, create the warp.
		
		Warp warp = new Warp(name, destination);
		if (description != null) warp.setDescription(description);
		if (permission != null) warp.setPermission(permission);
		
		HyperWarp.warps.add(warp);
		
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', TextGetterUtil.getTextSectionWithWarpInfo("warps.set.success", warp)));
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		List<String> list = new ArrayList<String>();
		if (sender instanceof Player) {
			Player player = (Player) sender;
			Location playerLocation = player.getLocation();
			String xstr = TextGetterUtil.doubleToFixed(playerLocation.getX(), 3);
			String ystr = TextGetterUtil.doubleToFixed(playerLocation.getY(), 3);
			String zstr = TextGetterUtil.doubleToFixed(playerLocation.getZ(), 3);
			String pitchstr = TextGetterUtil.floatToFixed(playerLocation.getPitch(), 3);
			String yawstr = TextGetterUtil.floatToFixed(playerLocation.getYaw(), 3);
			switch (args.length) {
			case 1: // name
				if (args[0].length() == 0)
					list.add("<name>");
				break;
				
			case 2: // x
				if (args[1].length() == 0) list.add(xstr + " " + ystr + " " + zstr);
				else                       list.add(args[1] + " "+ ystr + " " + zstr);
				break;
				
			case 3: // y
				if (args[2].length() == 0) list.add(ystr + " " + zstr);
				else                       list.add(args[2] + " " + zstr);
				break;
				
			case 4: // z
				if (args[3].length() == 0) list.add(zstr);
				else                       list.add(args[3]);
				break;
				
			case 5: // pitch
				if (args[4].length() == 0) list.add(pitchstr + " " + yawstr);
				else                       list.add(args[4] + " " + yawstr);
				break;
				
			case 6: // yaw
				if (args[5].length() == 0) list.add(yawstr);
				else                       list.add(args[5]);
				break;
				
			case 7: // world
				for (World world : HyperWarp.instance.getServer().getWorlds())
					if (world.getName().startsWith(args[6]))
						list.add(world.getName());
				break;
				
			case 8: // permission
				if ("none".startsWith(args[7]))
					list.add("none");
				for (Permission permission : HyperWarp.instance.getServer().getPluginManager().getPermissions())
					if (permission.getName().startsWith(args[7]))
						list.add(permission.getName());
				break;
				
			default: // description
				if (args[args.length-1].length() == 0)
					list.add("<description>");
				break;
			}
		}
		return list;
	}
	
	private Location parseLocation(Player sender, String commandAlias, String xstr, String ystr, String zstr, String pitchstr, String yawstr, String worldstr) {
		double x,y,z; float pitch=0f,yaw=0f; World world=null;
		try {
			x = Double.parseDouble(xstr);
			y = Double.parseDouble(ystr);
			z = Double.parseDouble(zstr);
			if (pitchstr != null)
				pitch = Float.parseFloat(pitchstr);
			if (yawstr != null)
				yaw = Float.parseFloat(yawstr);
			world = sender.getWorld();
		} catch (NumberFormatException e) {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', TextGetterUtil.getTextSectionWithCommandAlias("warps.set.error.commandSyntax", commandAlias)));
			return null;
		}
		if (worldstr != null) {
			world = HyperWarp.instance.getServer().getWorld(worldstr);
			if (world == null) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', TextGetterUtil.getTextSection("warps.set.error.worldNotFound")));
				return null;
			}
		}
		return new Location(world, x, y, z, yaw, pitch);
	}
}
