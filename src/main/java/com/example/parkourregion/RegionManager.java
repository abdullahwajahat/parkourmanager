package com.example.parkourmanager.data;

import com.example.parkourmanager.ParkourManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import java.util.*;

public class RegionManager {
    private final ParkourManager plugin;
    private final Map<String, Region> regions = new HashMap<>();

    public RegionManager(ParkourManager plugin) {
        this.plugin = plugin;
        loadRegions();
    }

    public void loadRegions() {
        regions.clear();
        ConfigurationSection sec = plugin.getConfig().getConfigurationSection("regions");
        if (sec != null) {
            for (String key : sec.getKeys(false)) {
                Region region = new Region(key);
                // TODO: load locations and other data
                regions.put(key, region);
            }
        }
    }

    public void saveRegions() {
        plugin.getConfig().set("regions", null);
        for (Region r : regions.values()) {
            // TODO: save all data
        }
        plugin.saveConfig();
    }

    public Map<String, Region> getRegions() { return regions; }
    public Region getRegion(String name) { return regions.get(name.toLowerCase()); }
    public void addRegion(Region region) { regions.put(region.getName().toLowerCase(), region); }
    public void removeRegion(String name) { regions.remove(name.toLowerCase()); }
}
