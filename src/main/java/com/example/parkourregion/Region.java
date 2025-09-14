// File: src/main/java/com/example/parkourregion/Region.java
package com.example.parkourregion;

import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

public class Region {

    private Location start;
    private List<String> blacklist = new ArrayList<>();

    public boolean contains(Location loc) {
        // Example check, implement your region bounds logic
        return loc.getWorld().equals(start.getWorld())
                && loc.getX() >= start.getX() && loc.getX() <= end.getX()
                && loc.getY() >= start.getY() && loc.getY() <= end.getY()
                && loc.getZ() >= start.getZ() && loc.getZ() <= end.getZ();
    }

    public void setStart(Location start) {
        this.start = start;
    }

    public Location getStart() {
        return start;
    }

    public List<String> getBlacklist() {
        return blacklist;
    }

    public void setBlacklist(List<String> blacklist) {
        this.blacklist = blacklist;
    }
}
