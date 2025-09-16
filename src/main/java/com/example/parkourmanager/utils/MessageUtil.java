package com.example.parkourmanager.utils;

import com.example.parkourmanager.ParkourManager;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class MessageUtil {

    private static ParkourManager plugin;
    private static FileConfiguration messagesConfig;
    private static File messagesFile;

    public static void init(ParkourManager pl) {
        plugin = pl;
        loadMessages();
    }

    /**
     * Loads or reloads the messages.yml file.
     */
    public static void loadMessages() {
        messagesFile = new File(plugin.getDataFolder(), "messages.yml");

        if (!messagesFile.exists()) {
            plugin.saveResource("messages.yml", false);
        }

        messagesConfig = YamlConfiguration.loadConfiguration(messagesFile);
    }

    /**
     * Reloads messages.yml from disk.
     */
    public static void reload() {
        loadMessages();
    }

    /**
     * Saves messages.yml to disk.
     */
    public static void save() {
        try {
            messagesConfig.save(messagesFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save messages.yml!");
            e.printStackTrace();
        }
    }

    /**
     * Send a message from messages.yml to a CommandSender.
     */
    public static void send(CommandSender sender, String path) {
        String msg = getMessage(path);
        if (msg != null && !msg.isEmpty()) {
            sender.sendMessage(msg);
        }
    }

    /**
     * Send a message with placeholders replaced.
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
     */
    public static String getMessage(String path) {
        if (messagesConfig == null) {
            return ChatColor.RED + "messages.yml not loaded!";
        }
        String raw = messagesConfig.getString(path);
        if (raw == null) {
            return ChatColor.RED + "Missing message: " + path;
        }
        return ChatColor.translateAlternateColorCodes('&', raw);
    }
}
