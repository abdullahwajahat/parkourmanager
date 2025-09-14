package com.example.parkourregion;


import org.bukkit.plugin.java.JavaPlugin;


public class ParkourRegionPlugin extends JavaPlugin {
private RegionManager regionManager;
private PlayerDataManager playerDataManager;


@Override
public void onEnable() {
saveDefaultConfig();
saveResource("messages.yml", false);
regionManager = new RegionManager(this);
playerDataManager = new PlayerDataManager();


getServer().getPluginManager().registerEvents(new MovementListener(this, regionManager, playerDataManager), this);
getServer().getPluginManager().registerEvents(new InteractionListener(this, regionManager, playerDataManager), this);
getServer().getPluginManager().registerEvents(new RegionBoundaryListener(regionManager, playerDataManager), this);


getCommand("parkourregion").setExecutor(new ParkourCommand(this, regionManager, playerDataManager));


getLogger().info("ParkourRegion enabled");
}


@Override
public void onDisable() {
regionManager.save();
getLogger().info("ParkourRegion disabled");
}


public PlayerDataManager getPlayerDataManager() { return playerDataManager; }
}
