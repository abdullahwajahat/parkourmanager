package com.example.parkourregion;

import org.bukkit.plugin.java.JavaPlugin;

public final class ParkourRegionPlugin extends JavaPlugin {

    private RegionManager regionManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        regionManager = new RegionManager(this);

        // Register commands
        getCommand("por").setExecutor(new ParkourCommand(this));
        getCommand("por").setTabCompleter(new ParkourTabCompleter(this));

        // Register listeners
        getServer().getPluginManager().registerEvents(new MovementListener(this), this);
        getServer().getPluginManager().registerEvents(new InteractionListener(this), this);
    }

    public RegionManager getRegionManager() {
        return regionManager;
    }
}
