package com.example.parkourregion;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegionManager {

    private final ParkourRegion plugin;
    private final Map<String, Region> regions = new HashMap<>();

    public RegionManager(ParkourRegion plugin) {
        this.plugin = plugin;
        loadRegions();
    }

    public void createRegion(String name) {
        Region r = new Region(name);
        regions.put(name, r);
        saveRegions();
    }

    public void deleteRegion(String name) {
        regions.remove(name);
        saveRegions();
    }

    public Region getRegion(String name) {
        return regions.get(name);
    }

    public Region getRegion(Location loc) {
        return regions.values().stream().filter(r -> r.isInside(loc)).findFirst().orElse(null);
    }

    public List<String> getAllRegionNames() {
        return new ArrayList<>(regions.keySet());
    }

    public void loadRegions() {
        FileConfiguration cfg = plugin.getConfig();
        if (!cfg.contains("regions")) return;

        cfg.getConfigurationSection("regions").getKeys(false).forEach(key -> {
            regions.put(key, Region.loadFromConfig(cfg.getConfigurationSection("regions." + key)));
        });
    }

    public void saveRegions() {
        FileConfiguration cfg = plugin.getConfig();
        cfg.set("regions", null);
        regions.forEach((name, region) -> cfg.set("regions." + name, region.saveToConfig()));
        plugin.saveConfig();
    }
}
