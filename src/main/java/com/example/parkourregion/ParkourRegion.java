package com.example.parkourregion;

import org.bukkit.plugin.java.JavaPlugin;
import com.example.parkourregion.managers.RegionManager;
import com.example.parkourregion.commands.ParkourCommand;

public final class ParkourRegion extends JavaPlugin {

    private RegionManager regionManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        this.regionManager = new RegionManager(this);
        getCommand("por").setExecutor(new ParkourCommand(regionManager));
        getCommand("por").setTabCompleter(new ParkourCommand(regionManager));
        getLogger().info("ParkourRegion enabled!");
    }

    @Override
    public void onDisable() {
        regionManager.saveRegions();
        getLogger().info("ParkourRegion disabled!");
    }

    public RegionManager getRegionManager() {
        return regionManager;
    }
}
