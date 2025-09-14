package com.example.parkourregion;

import org.bukkit.Location;
import org.bukkit.Material;

import java.util.List;

public class Region {

    private final String name;
    private final Location min;
    private final Location max;
    private final List<Material> blacklistedBlocks;

    public Region(String name, Location min, Location max, List<Material> blacklistedBlocks) {
        this.name = name;
        this.min = min;
        this.max = max;
        this.blacklistedBlocks = blacklistedBlocks;
    }

    public String getName() {
        return name;
    }

    public List<Material> getBlacklistedBlocks() {
        return blacklistedBlocks;
    }

    public boolean contains(Location loc) {
        return loc.getWorld().equals(min.getWorld()) &&
                loc.getBlockX() >= min.getBlockX() && loc.getBlockX() <= max.getBlockX() &&
                loc.getBlockY() >= min.getBlockY() && loc.getBlockY() <= max.getBlockY() &&
                loc.getBlockZ() >= min.getBlockZ() && loc.getBlockZ() <= max.getBlockZ();
    }
}
