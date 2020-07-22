package me.scidev.hyperwarp.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import me.scidev.hyperwarp.HyperWarp;
import me.scidev.hyperwarp.Warp;
import me.scidev.hyperwarp.i10n.TextGetterUtil;
import net.md_5.bungee.api.ChatColor;

public class ListWarpsCommand implements CommandExecutor, TabCompleter {
	@Override
	public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
		if (args.length != 0) {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', TextGetterUtil.getTextSectionWithCommandAlias("warps.list.error.commandSyntax", alias)));
			return true;
		}
		List<String> warpStrings = new ArrayList<String>();
		for (Warp warp : HyperWarp.warps)
			warpStrings.add(TextGetterUtil.getTextSectionWithWarpInfo("warps.list.row", warp));
		Map<String, String> textObjects = new HashMap<>();
		if (HyperWarp.warps.size() > 0)
			textObjects.put("WARPS",String.join(TextGetterUtil.getTextSection("warps.list.separator"), warpStrings));
		else textObjects.put("WARPS",TextGetterUtil.getTextSection("warps.list.empty"));
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', TextGetterUtil.getTextSectionWithOtherText("warps.list.success", textObjects)));
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		return new ArrayList<String>();
	}

}
