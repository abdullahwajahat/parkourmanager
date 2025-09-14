// File: src/main/java/com/example/parkourregion/MessageManager.java
package com.example.parkourregion;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class MessageManager {

    private final ParkourRegionPlugin plugin;
    private final FileConfiguration messages;

    public MessageManager(ParkourRegionPlugin plugin) {
        this.plugin = plugin;
        this.messages = plugin.getConfig(); // or load messages.yml separately
    }

    public String getMessage(String path) {
        return messages.getString("messages." + path, "");
    }

    public List<String> getMessageList(String path) {
        return messages.getStringList("messages." + path + ".messages");
    }

    public String getSound(String path) {
        return messages.getString("messages." + path + ".sound", "");
    }
}
