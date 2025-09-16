package com.example.parkourmanager;

import com.example.parkourmanager.commands.ParkourCommand;
import com.example.parkourmanager.commands.TabCompletion;
import com.example.parkourmanager.data.RegionManager;
import com.example.parkourmanager.listeners.PlayerListener;
import org.bukkit.plugin.java.JavaPlugin;

public class ParkourManager extends JavaPlugin {

    private RegionManager regionManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        this.regionManager = new RegionManager(this);

        // Register command executor + tab completer
        ParkourCommand parkourCommand = new ParkourCommand(regionManager);
        getCommand("parkour").setExecutor(parkourCommand);
        getCommand("parkour").setTabCompleter(new TabCompletion(regionManager));

        // Register listener
        getServer().getPluginManager().registerEvents(new PlayerListener(regionManager), this);

        getLogger().info("ParkourManager enabled!");
    }

    @Override
    public void onDisable() {
        if (regionManager != null) {
            regionManager.saveRegions();
        }
        getLogger().info("ParkourManager disabled!");
    }

    public RegionManager getRegionManager() {
        return regionManager;
    }
}
