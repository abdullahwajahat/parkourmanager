package com.example.parkourmanager.data;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class RegionManager {
    private final Map<String, Region> regions;

    public RegionManager() {
        this.regions = new HashMap<>();
    }

    // ✅ Create and register a region
    public void createRegion(String name, Location pos1, Location pos2) {
        Region region = new Region(name, pos1, pos2);
        regions.put(name.toLowerCase(), region);
    }

    // ✅ Get a region
    public Region getRegion(String name) {
        return regions.get(name.toLowerCase());
    }

    // ✅ Remove a region
    public void removeRegion(String name) {
        regions.remove(name.toLowerCase());
    }

    // ✅ All regions
    public Collection<Region> getRegions() {
        return regions.values();
    }

    // ✅ Checkpoints
    public void setCheckpoint(String regionName, int index, Location location) {
        Region region = getRegion(regionName);
        if (region != null) {
            region.setCheckpoint(index, location);
        }
    }

    // ✅ Finish commands
    public void addFinishCommand(String regionName, String command) {
        Region region = getRegion(regionName);
        if (region != null) {
            region.addFinishCommand(command);
        }
    }

    public void clearFinishCommands(String regionName) {
        Region region = getRegion(regionName);
        if (region != null) {
            region.setFinishCommands(new java.util.ArrayList<>());
        }
    }

    // ✅ Fall Y
    public void setFallY(String regionName, int y) {
        Region region = getRegion(regionName);
        if (region != null) {
            region.setFallY(y);
        }
    }

    // ✅ Blacklist blocks
    public void addBlacklistBlock(String regionName, String materialName) {
        Region region = getRegion(regionName);
        if (region != null) {
            try {
                Material mat = Material.valueOf(materialName.toUpperCase());
                region.addBlacklistBlock(mat);
            } catch (IllegalArgumentException e) {
                Bukkit.getLogger().warning("[ParkourManager] Invalid block in blacklist: " + materialName);
            }
        }
    }

    public void clearBlacklistBlocks(String regionName) {
        Region region = getRegion(regionName);
        if (region != null) {
            region.setBlacklistBlocks(new java.util.ArrayList<>());
        }
    }
}
