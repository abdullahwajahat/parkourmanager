package com.example.parkourregion;

import org.bukkit.plugin.java.JavaPlugin;

public final class ParkourRegionPlugin extends JavaPlugin {

    private static ParkourRegionPlugin instance;
    private RegionManager regionManager;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();
        saveResource("messages.yml", false);

        regionManager = new RegionManager(this);
        getServer().getPluginManager().registerEvents(new MovementListener(regionManager), this);
        getServer().getPluginManager().registerEvents(new InteractionListener(regionManager), this);

        getCommand("por").setExecutor(new ParkourCommand(regionManager));
        getCommand("por").setTabCompleter(new ParkourTabCompleter(regionManager));
    }

    @Override
    public void onDisable() {
        regionManager.saveRegions();
    }

    public static ParkourRegionPlugin getInstance() {
        return instance;
    }

    public RegionManager getRegionManager() {
        return regionManager;
    }
}
