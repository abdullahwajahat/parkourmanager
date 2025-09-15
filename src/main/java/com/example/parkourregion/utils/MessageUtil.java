package com.example.parkourmanager.utils;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import com.example.parkourmanager.ParkourManager;

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
}
