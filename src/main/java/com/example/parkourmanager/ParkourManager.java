package com.example.parkourmanager;

import com.example.parkourmanager.commands.ParkourCommand;
import com.example.parkourmanager.data.RegionManager;
import com.example.parkourmanager.data.SelectionManager;
import com.example.parkourmanager.utils.MessageUtil;
import org.bukkit.plugin.java.JavaPlugin;

public class ParkourManager extends JavaPlugin {

    private static ParkourManager instance;
    private RegionManager regionManager;
    private SelectionManager selectionManager;

    @Override
    public void onEnable() {
        instance = this;

        // Save default config and messages.yml
        saveDefaultConfig();
        saveResource("messages.yml", false);

        // Initialize utils and managers
        MessageUtil.init(this);
        this.regionManager = new RegionManager(this);
        this.selectionManager = new SelectionManager();

        // Register command
        ParkourCommand cmd = new ParkourCommand(this);
        getCommand("parkourmanager").setExecutor(cmd);
        getCommand("parkourmanager").setTabCompleter(cmd);

        getLogger().info("ParkourManager enabled!");
    }

    @Override
    public void onDisable() {
        // Save regions before shutting down
        regionManager.saveRegions();
        getLogger().info("ParkourManager disabled!");
    }

    // Getters
    public static ParkourManager getInstance() {
        return instance;
    }

    public RegionManager getRegionManager() {
        return regionManager;
    }

    public SelectionManager getSelectionManager() {
        return selectionManager;
    }
}
