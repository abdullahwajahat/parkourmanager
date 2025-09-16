package com.example.parkourmanager.utils;

import com.example.parkourmanager.ParkourManager;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Map;

public class MessageUtil {

    private static ParkourManager plugin;

    public static void init(ParkourManager pl) {
        plugin = pl;
    }

    /**
     * Send a message from messages.yml to a CommandSender.
     *
     * @param sender CommandSender (player or console)
     * @param path   Key in messages.yml
     */
    public static void send(CommandSender sender, String path) {
        String msg = getMessage(path);
        if (msg != null && !msg.isEmpty()) {
            sender.sendMessage(msg);
        }
    }

    /**
     * Send a message with placeholders replaced.
     *
     * @param sender       CommandSender
     * @param path         Key in messages.yml
     * @param placeholders Map of placeholders to replace
     */
    public static void send(CommandSender sender, String path, Map<String, String> placeholders) {
        String msg = getMessage(path);
        if (msg != null && !msg.isEmpty()) {
            for (Map.Entry<String, String> entry : placeholders.entrySet()) {
                msg = msg.replace("{" + entry.getKey() + "}", entry.getValue());
            }
            sender.sendMessage(msg);
        }
    }

    /**
     * Get a message from messages.yml with color codes translated.
     *
     * @param path Key in messages.yml
     * @return formatted message
     */
    public static String getMessage(String path) {
        if (plugin == null) return ChatColor.RED + "Plugin not initialized!";
        String raw = plugin.getConfig().getString("messages." + path);
        if (raw == null) {
            return ChatColor.RED + "Missing message: " + path;
        }
        return ChatColor.translateAlternateColorCodes('&', raw);
    }
}
