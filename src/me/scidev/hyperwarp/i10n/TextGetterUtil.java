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
		textMap.put("X",doubleToFixed(warp.destination.getX(),3));
		textMap.put("Y",doubleToFixed(warp.destination.getY(),3));
		textMap.put("Z",doubleToFixed(warp.destination.getZ(),3));
		textMap.put("YAW",floatToFixed(warp.destination.getYaw(),3));
		textMap.put("PITCH",floatToFixed(warp.destination.getPitch(),3));
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
	
	public static String doubleToFixed(double n, int digits) {
		return String.format("%."+digits+"f", n);
	}
	public static String floatToFixed(float n, int digits) {
		return String.format("%."+digits+"f", n);
	}
}
