package com.example.parkourregion;

import org.bukkit.plugin.java.JavaPlugin;

public class ParkourRegion extends JavaPlugin {

    private static ParkourRegion instance;
    private RegionManager regionManager;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        // Initialize region manager
        regionManager = new RegionManager(this);
        regionManager.loadRegions();

        // Register command & tab completer
        this.getCommand("por").setExecutor(new ParkourCommand(regionManager));
        this.getCommand("por").setTabCompleter(new ParkourTabCompleter(regionManager));

        // Register listener
        getServer().getPluginManager().registerEvents(new MovementListener(regionManager), this);

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
