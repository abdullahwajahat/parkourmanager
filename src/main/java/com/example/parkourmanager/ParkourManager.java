package com.example.parkourmanager;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class ParkourManager extends JavaPlugin {

    private static ParkourManager instance;

    private RegionManager regionManager;
    private SelectionManager selectionManager;
    private FileConfiguration messagesConfig;
    private File messagesFile;

    @Override
    public void onEnable() {
        instance = this;

        // Save default config.yml if not exists
        saveDefaultConfig();

        // Load messages.yml
        createMessagesFile();

        // Init managers
        regionManager = new RegionManager(this);
        selectionManager = new SelectionManager();

        // Register commands
        getCommand("parkourmanager").setExecutor(new ParkourCommand(this));
        getCommand("parkourmanager").setTabCompleter(new ParkourTabCompleter(this));

        // Register events
        Bukkit.getPluginManager().registerEvents(new RegionListener(this), this);
        Bukkit.getPluginManager().registerEvents(new BlacklistListener(this), this);

        getLogger().info("Parkour Manager enabled successfully!");
    }

    @Override
    public void onDisable() {
        regionManager.saveRegions();
        getLogger().info("Parkour Manager disabled!");
    }

    public static ParkourManager getInstance() {
        return instance;
    }

    public RegionManager getRegionManager() {
        return regionManager;
    }

    public SelectionManager getSelectionManager() {
        return selectionManager;
    }

    public FileConfiguration getMessagesConfig() {
        return messagesConfig;
    }

    public void saveMessagesConfig() {
        try {
            messagesConfig.save(messagesFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createMessagesFile() {
        messagesFile = new File(getDataFolder(), "messages.yml");
        if (!messagesFile.exists()) {
            messagesFile.getParentFile().mkdirs();
            saveResource("messages.yml", false);
        }

        messagesConfig = new YamlConfiguration();
        try {
            messagesConfig.load(messagesFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
}
