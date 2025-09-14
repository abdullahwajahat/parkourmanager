package com.example.parkourregion;

import org.bukkit.plugin.java.JavaPlugin;

import com.example.parkourregion.commands.ParkourCommand;
import com.example.parkourregion.commands.ParkourGUICommand;
import com.example.parkourregion.managers.RegionManager;

public final class ParkourRegion extends JavaPlugin {

    private static ParkourRegion instance;
    private RegionManager regionManager;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        saveResource("messages.yml", false);

        regionManager = new RegionManager();

        // Register commands
        getCommand("por").setExecutor(new ParkourCommand());
        getCommand("por").setTabCompleter(new ParkourCommand());
        getCommand("por").setExecutor(new ParkourGUICommand());

        getLogger().info("ParkourRegion enabled!");
    }

    @Override
    public void onDisable() {
        regionManager.saveRegions();
        getLogger().info("ParkourRegion disabled!");
    }

    public static ParkourRegion getInstance() {
        return instance;
    }

    public RegionManager getRegionManager() {
        return regionManager;
    }
}

