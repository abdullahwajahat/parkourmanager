package com.example.parkourregion;

import org.bukkit.Location;

import java.util.List;

public class Region {

    private final String name;
    private final Location min;
    private final Location max;
    private final List<String> blacklist;
    private final int cooldown;

    public Region(String name, Location min, Location max, List<String> blacklist, int cooldown) {
        this.name = name;
        this.min = min;
        this.max = max;
        this.blacklist = blacklist;
        this.cooldown = cooldown;
    }

    public String getName() {
        return name;
    }

    public Location getMin() {
        return min;
    }

    public Location getMax() {
        return max;
    }

    public List<String> getBlacklist() {
        return blacklist;
    }

    public int getCooldown() {
        return cooldown;
    }

    public boolean contains(Location loc) {
        if (!loc.getWorld().equals(min.getWorld())) return false;
        return loc.getX() >= min.getX() && loc.getX() <= max.getX()
                && loc.getY() >= min.getY() && loc.getY() <= max.getY()
                && loc.getZ() >= min.getZ() && loc.getZ() <= max.getZ();
    }
}
