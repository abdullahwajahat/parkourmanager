// File: src/main/java/com/example/parkourregion/ParkourRegionPlugin.java
package com.example.parkourregion;

import org.bukkit.plugin.java.JavaPlugin;

public class ParkourRegionPlugin extends JavaPlugin {

    private RegionManager regionManager;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        this.saveResource("messages.yml", false);

        regionManager = new RegionManager(this);

        getServer().getPluginManager().registerEvents(new MovementListener(this), this);
        getServer().getPluginManager().registerEvents(new InteractionListener(this), this);

        if (this.getCommand("por") != null) {
            this.getCommand("por").setExecutor(new ParkourCommand(this));
            this.getCommand("por").setTabCompleter(new ParkourCommand(this));
        }
    }

    public RegionManager getRegionManager() {
        return regionManager;
    }
}
