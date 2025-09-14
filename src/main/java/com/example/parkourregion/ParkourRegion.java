package com.example.parkourregion;

import org.bukkit.plugin.java.JavaPlugin;
import com.example.parkourregion.managers.*;
import com.example.parkourregion.commands.ParkourCommand;

public final class ParkourRegion extends JavaPlugin {

    private RegionManager regionManager;
    private CooldownManager cooldownManager;
    private SelectionManager selectionManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        saveResource("messages.yml", false);

        this.regionManager = new RegionManager(this);
        this.cooldownManager = new CooldownManager();
        this.selectionManager = new SelectionManager();

        ParkourCommand command = new ParkourCommand(this);
        getCommand("por").setExecutor(command);
        getCommand("por").setTabCompleter(command);

        getLogger().info("ParkourRegion enabled!");
    }

    @Override
    public void onDisable() {
        regionManager.saveRegions();
        getLogger().info("ParkourRegion disabled!");
    }

    public RegionManager getRegionManager() { return regionManager; }
    public CooldownManager getCooldownManager() { return cooldownManager; }
    public SelectionManager getSelectionManager() { return selectionManager; }
}
