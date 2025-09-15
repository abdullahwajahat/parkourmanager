package com.example.parkourmanager;

import com.example.parkourmanager.commands.ParkourCommand;
import com.example.parkourmanager.data.RegionManager;
import com.example.parkourmanager.listeners.PlayerListener;
import com.example.parkourmanager.utils.MessageUtil;
import org.bukkit.plugin.java.JavaPlugin;

public class ParkourManager extends JavaPlugin {

    private static ParkourManager instance;
    private RegionManager regionManager;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        saveResource("messages.yml", false);

        MessageUtil.load(this);
        this.regionManager = new RegionManager(this);

        getCommand("parkourmanager").setExecutor(new ParkourCommand(this));
        getCommand("parkourmanager").setTabCompleter(new ParkourCommand(this));

        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);

        getLogger().info("ParkourManager enabled!");
    }

    @Override
    public void onDisable() {
        regionManager.saveRegions();
        getLogger().info("ParkourManager disabled!");
    }

    public static ParkourManager getInstance() {
        return instance;
    }

    public RegionManager getRegionManager() {
        return regionManager;
    }
}
