// File: src/main/java/com/example/parkourregion/RegionManager.java
package com.example.parkourregion;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Map;

public class RegionManager {

    private final ParkourRegionPlugin plugin;
    private final Map<String, Region> regions;

    public RegionManager(ParkourRegionPlugin plugin) {
        this.plugin = plugin;
        this.regions = new HashMap<>();
        loadRegions();
    }

    public Map<String, Region> getRegions() {
        return regions;
    }

    public void addRegion(String name, Region region) {
        regions.put(name, region);
        saveRegion(name, region);
    }

    public void saveRegion(String name, Region region) {
        FileConfiguration config = plugin.getConfig();
        config.set("regions." + name + ".start", region.getStartLocationString());
        config.set("regions." + name + ".end", region.getEndLocationString());
        config.set("regions." + name + ".blacklist", region.getBlacklist());
        plugin.saveConfig();
    }

    public void loadRegions() {
        FileConfiguration config = plugin.getConfig();
        if (!config.isConfigurationSection("regions")) return;
        for (String key : config.getConfigurationSection("regions").getKeys(false)) {
            Location start = Region.stringToLocation(config.getString("regions." + key + ".start"));
            Location end = Region.stringToLocation(config.getString("regions." + key + ".end"));
            regions.put(key, new Region(start, end, config.getStringList("regions." + key + ".blacklist")));
        }
    }
}
