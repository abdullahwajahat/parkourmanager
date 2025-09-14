// File: src/main/java/com/example/parkourregion/Region.java
package com.example.parkourregion;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.List;

public class Region {

    private final Location start;
    private final Location end;
    private final List<String> blacklist;

    public Region(Location start, Location end, List<String> blacklist) {
        this.start = start;
        this.end = end;
        this.blacklist = blacklist;
    }

    public Location getStart() {
        return start;
    }

    public Location getEnd() {
        return end;
    }

    public List<String> getBlacklist() {
        return blacklist;
    }

    public boolean contains(Location loc) {
        return loc.getWorld().equals(start.getWorld())
                && loc.getX() >= start.getX() && loc.getX() <= end.getX()
                && loc.getY() >= start.getY() && loc.getY() <= end.getY()
                && loc.getZ() >= start.getZ() && loc.getZ() <= end.getZ();
    }

    public String getStartLocationString() {
        return locationToString(start);
    }

    public String getEndLocationString() {
        return locationToString(end);
    }

    public static String locationToString(Location loc) {
        return loc.getWorld().getName() + "," + loc.getX() + "," + loc.getY() + "," + loc.getZ();
    }

    public static Location stringToLocation(String s) {
        String[] parts = s.split(",");
        return new Location(Bukkit.getWorld(parts[0]),
                Double.parseDouble(parts[1]),
                Double.parseDouble(parts[2]),
                Double.parseDouble(parts[3]));
    }
}
