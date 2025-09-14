package com.example.parkourregion.utils;

import com.example.parkourregion.ParkourRegion;
import org.bukkit.ChatColor;

import java.io.File;
import java.util.Objects;

public class MessageUtil {

    public static String color(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String get(String path) {
        String msg = ParkourRegion.getInstance().getConfig().getString(path);
        if (msg == null) {
            // fallback to messages.yml
            msg = ParkourRegion.getInstance().getConfig().getString(path);
        }
        return color(Objects.requireNonNullElse(msg, path));
    }
}
