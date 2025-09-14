package com.example.parkourregion;

import org.bukkit.plugin.java.JavaPlugin;

public class ParkourRegion extends JavaPlugin {

    private static ParkourRegion instance;
    private RegionManager regionManager;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();
        saveResource("messages.yml", false);

        regionManager = new RegionManager(this);

        getCommand("por").setExecutor(new ParkourCommand(this));
        getCommand("por").setTabCompleter(new ParkourTabCompleter(this));

        getServer().getPluginManager().registerEvents(new MovementListener(this), this);
    }

    @Override
    public void onDisable() {
        if (regionManager != null) {
            regionManager.saveRegions();
        }
    }

    public RegionManager getRegionManager() {
        return regionManager;
    }

    public static ParkourRegion getInstance() {
        return instance;
    }
}
