// File: src/main/java/com/example/parkourregion/Region.java
package com.example.parkourregion;

import org.bukkit.Location;
import java.util.List;
import java.util.ArrayList;

public class Region {

    private final String name;
    private Location start;  // mutable
    private Location end;    // mutable
    private final List<String> blacklist;

    public Region(String name, Location start, Location end, List<String> blacklist) {
        this.name = name;
        this.start = start;
        this.end = end;
        this.blacklist = blacklist != null ? blacklist : new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public Location getStart() {
        return start;
    }

    public Location getEnd() {
        return end;
    }

    public void setStart(Location start) {
        this.start = start;
    }

    public void setEnd(Location end) {
        this.end = end;
    }

    public List<String> getBlacklist() {
        return blacklist;
    }

    public boolean contains(Location loc) {
        if (!loc.getWorld().equals(start.getWorld())) return false;

        double x = loc.getX();
        double y = loc.getY();
        double z = loc.getZ();

        double minX = Math.min(start.getX(), end.getX());
        double minY = Math.min(start.getY(), end.getY());
        double minZ = Math.min(start.getZ(), end.getZ());

        double maxX = Math.max(start.getX(), end.getX());
        double maxY = Math.max(start.getY(), end.getY());
        double maxZ = Math.max(start.getZ(), end.getZ());

        return x >= minX && x <= maxX &&
               y >= minY && y <= maxY &&
               z >= minZ && z <= maxZ;
    }
}
