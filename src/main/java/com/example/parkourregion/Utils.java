package com.example.parkourregion;


import org.bukkit.ChatColor;
import org.bukkit.entity.Player;


public class Utils {
public static String color(String s) { if (s == null) return null; return ChatColor.translateAlternateColorCodes('&', s); }
public static void sendActionBar(Player p, String message) { p.sendActionBar(message); }
}
