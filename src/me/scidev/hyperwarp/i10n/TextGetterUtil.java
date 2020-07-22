package me.scidev.hyperwarp.i10n;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.scidev.hyperwarp.HyperWarp;
import me.scidev.hyperwarp.Warp;

public class TextGetterUtil {
	public static String getTextSection(String configNodeAddress) {
		return HyperWarp.instance
				.getConfig()
				.getConfigurationSection("i10n")
				.getString(configNodeAddress,"<I10N at ["+configNodeAddress+"]>");
	}
	public static String getTextSectionWithOtherText(String configNodeAddress, Map<String, String> otherTexts) {
		String textSection = getTextSection(configNodeAddress);
		for (String key : otherTexts.keySet())
			textSection = textSection.replaceAll("%"+Pattern.quote(key)+"%", Matcher.quoteReplacement(otherTexts.get(key)));
		return textSection;
	}
	public static String getTextSectionWithWarpInfo(String configNodeAddress, Warp warp) {
		Map<String, String> textMap = new HashMap<>();
		textMap.put("NAME",warp.name);
		if (warp.description.length() > 0)
			textMap.put("DESCRIPTION",warp.description);
		else 
			textMap.put("DESCRIPTION",getTextSection("warps.noDescription"));
		textMap.put("X",Double.toString(warp.destination.getX()));
		textMap.put("Y",Double.toString(warp.destination.getY()));
		textMap.put("Z",Double.toString(warp.destination.getZ()));
		textMap.put("YAW",Float.toString(warp.destination.getYaw()));
		textMap.put("PITCH",Float.toString(warp.destination.getPitch()));
		textMap.put("WORLD",warp.destination.getWorld().getName());
		if (warp.permission == null) textMap.put("PERMISSION",getTextSection("warps.permissionUnnecessary"));
		else textMap.put("PERMISSION",warp.permission.getName());
		return getTextSectionWithOtherText(configNodeAddress, textMap);
	}
	public static String getTextSectionWithCommandAlias(String configNodeAddress, String alias) {
		Map<String, String> textMap = new HashMap<>();
		textMap.put("ALIAS",alias);
		return getTextSectionWithOtherText(configNodeAddress, textMap);
	}
}
