package com.example.parkourregion;

import org.bukkit.plugin.java.JavaPlugin;

public class ParkourRegionPlugin extends JavaPlugin {

    private static ParkourRegionPlugin instance;
    private RegionManager regionManager;
    private MessageManager messageManager;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();
        saveResource("messages.yml", false);

        this.regionManager = new RegionManager(this);
        this.messageManager = new MessageManager(this);

        getCommand("por").setExecutor(new ParkourCommand(this));
        getCommand("por").setTabCompleter(new ParkourTabCompleter(this));

        getServer().getPluginManager().registerEvents(new MovementListener(this), this);
        getServer().getPluginManager().registerEvents(new InteractionListener(this), this);
    }

    public static ParkourRegionPlugin getInstance() {
        return instance;
    }

    public RegionManager getRegionManager() {
        return regionManager;
    }

    public MessageManager getMessageManager() {
        return messageManager;
    }
}
