package com.candor.utils;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.candor.main.Candor;

public class MessageUtils {

	public static String translateAlternateColorCodes(String s) {
		return ChatColor.translateAlternateColorCodes('&', MessageUtils.convertColors(s));
	}
	
	public static String translateAlternateColorCodes(String s, boolean autoColour) {
		if(!autoColour) return s;
		return ChatColor.translateAlternateColorCodes('&', MessageUtils.convertColors(s));
	}
	
	public static String convertColors(String s) {
        Pattern pattern = Pattern.compile(Pattern.quote("{#") + "(.*?)" +  Pattern.quote("}"));
        Matcher match = pattern.matcher(s);
        String ns = s;
        while (match.find()) {
            String colorcode = match.group(1);
            ns = ns.replaceAll("\\{#" + colorcode + "\\}", MessageUtils.getColor("#"+colorcode).toString());
        }
 
        return ns;
	}
	
	public static List<String> removeColourCodeSymbol(List<String> data){
		if(data == null || data.isEmpty()) return data;
		data.replaceAll(i -> i.replace("§", "&"));
		return data;
	}
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static net.md_5.bungee.api.ChatColor getColor(String s) {
        try {
		Class c = net.md_5.bungee.api.ChatColor.class;
		Method m = c.getMethod("of", String.class);
        Object o = m.invoke(null, s);
        return (net.md_5.bungee.api.ChatColor)o;
 
        }catch (Exception e) {
            return net.md_5.bungee.api.ChatColor.WHITE;
        }
	}
    
	public static void sendConfigMessage(Player player, String key) {
		Candor.getInstance().configManager.messages.send(player, key);
	}
	
	public static void sendConfigMessage(Player player, String key, Map<String, String> pairs) {
		Candor.getInstance().configManager.messages.send(player, key, pairs);
	}

	
	public static void sendRawMessage(Player player, String s) {
		player.sendMessage(MessageUtils.translateAlternateColorCodes(s));
	}
	
	public static void inform(Player player, String s) {
		player.sendMessage(Candor.getInstance().configManager.messages.prefix + MessageUtils.translateAlternateColorCodes(s));
	}
	
	public static void sendMessage(Player player, String s) {
		player.sendMessage(Candor.getInstance().configManager.messages.prefix + " " + MessageUtils.translateAlternateColorCodes(s));
	}

	public static void sendConsoleMessage(String msg){
		  Bukkit.getConsoleSender().sendMessage(MessageUtils.translateAlternateColorCodes(msg));
	}
	public static void sendConsoleMessage(String[] msg){
		for(int i = 0; i < msg.length; i++)
		System.out.println(MessageUtils.translateAlternateColorCodes(msg[i]));
	}
	public static void sendHelp(Player player) {

		 Candor.getInstance().CommandManager.getCommands().forEach(i -> {
			 MessageUtils.sendRawMessage(player,Candor.getInstance().configManager.messages.prefix + " " + "&c" + i.name() + "&7 : &b" + i.info());
		 });
	}
}
