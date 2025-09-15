package com.example.parkourmanager.utils;
import com.example.parkourmanager.ParkourManager;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class MessageUtil {
    private static FileConfiguration messages;

    public static void load(ParkourManager plugin) {
        File file = new File(plugin.getDataFolder(), "messages.yml");
        messages = YamlConfiguration.loadConfiguration(file);
    }

    public static String get(String path) {
        return ChatColor.translateAlternateColorCodes('&',
                messages.getString("prefix", "") + messages.getString(path, path));
    }
    
    // Send to any command sender
    public static void send(CommandSender sender, String message) {
        if (sender != null && message != null) {
            sender.sendMessage(message);
        }
    }

    // Send to player with placeholders
    public static void send(Player player, String message, Map<String, String> placeholders) {
        if (player != null && message != null) {
            String processed = message;
            if (placeholders != null) {
                for (Map.Entry<String, String> entry : placeholders.entrySet()) {
                    processed = processed.replace("%" + entry.getKey() + "%", entry.getValue());
                }
            }
            player.sendMessage(processed);
        }
    }

    // Send to player without placeholders
    public static void send(Player player, String message) {
        send((CommandSender) player, message);
    }
}
