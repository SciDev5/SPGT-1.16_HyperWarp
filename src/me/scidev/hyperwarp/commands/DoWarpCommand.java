package me.scidev.hyperwarp.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import me.scidev.hyperwarp.HyperWarp;
import me.scidev.hyperwarp.Warp;
import me.scidev.hyperwarp.i10n.TextGetterUtil;
import net.md_5.bungee.api.ChatColor;

public class DoWarpCommand implements CommandExecutor, TabCompleter {
	@Override
	public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
		if (args.length != 1) {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', TextGetterUtil.getTextSectionWithCommandAlias("warps.warp.error.commandSyntax", alias)));
			return true;
		}
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', TextGetterUtil.getTextSection("warps.warp.error.isConsole")));
			return true;
		}
		Warp warp = HyperWarp.getWarpByName(args[0]);
		if (warp == null) {
			Map<String, String> textObjects = new HashMap<>();
			textObjects.put("WARPNAME",args[0]);
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', TextGetterUtil.getTextSectionWithOtherText("warps.warp.error.noSuchWarp", textObjects)));
		} else {
			if (warp.teleportPlayer((Player) sender))
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', TextGetterUtil.getTextSectionWithWarpInfo("warps.warp.success", warp)));
			else 
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', TextGetterUtil.getTextSectionWithWarpInfo("warps.warp.error.noPermission",warp)));
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
		}
		return list;
	}
}
