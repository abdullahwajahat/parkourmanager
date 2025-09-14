package com.example.parkourregion;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegionManager {

    private final ParkourRegionPlugin plugin;
    private final Map<String, Region> regions = new HashMap<>();

    public RegionManager(ParkourRegionPlugin plugin) {
        this.plugin = plugin;
    }

    public Map<String, Region> getRegions() {
        return regions;
    }

    public void addRegion(String name, Location min, Location max, List<Material> blacklist) {
        regions.put(name, new Region(name, min, max, blacklist));
    }

    public Region getRegion(String name) {
        return regions.get(name);
    }

    public Region getRegionAt(Location loc) {
        for (Region region : regions.values()) {
            if (region.contains(loc)) return region;
        }
        return null;
    }
}
