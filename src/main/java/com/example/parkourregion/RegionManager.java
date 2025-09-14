package com.example.parkourregion;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class RegionManager {

    private final JavaPlugin plugin;
    private final Map<String, Region> regions = new HashMap<>();

    public RegionManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void createRegion(String name) {
        regions.put(name, new Region(name));
    }

    public Region getRegion(String name) {
        return regions.get(name);
    }

    public Map<String, Region> getRegions() {
        return regions;
    }

    public void loadRegions() {
        ConfigurationSection section = plugin.getConfig().getConfigurationSection("regions");
        if (section == null) return;

        for (String key : section.getKeys(false)) {
            Region r = Region.loadFromConfig(section.getConfigurationSection(key));
            regions.put(key, r);
        }
    }

    public void saveRegions() {
        ConfigurationSection section = plugin.getConfig().createSection("regions");
        for (Region r : regions.values()) {
            ConfigurationSection rSec = section.createSection(r.getName());
            r.saveToConfig(rSec);
        }
        plugin.saveConfig();
    }
}
