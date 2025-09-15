package com.example.parkourmanager.data;

import org.bukkit.Location;
import java.util.*;

public class Region {
    private final String name;
    private Location pos1;
    private Location pos2;
    private Location start;
    private Location finish;
    private final Map<Integer, Location> checkpoints = new HashMap<>();
    private final List<String> finishCommands = new ArrayList<>();
    private final List<String> blacklist = new ArrayList<>();
    private double fallY = Double.MIN_VALUE;
    private int cooldown = 0; // changed from String "0s" → int 0

    public Region(String name) {
        this.name = name;
    }

    public String getName() { return name; }
    public Location getPos1() { return pos1; }
    public Location getPos2() { return pos2; }
    public void setPos1(Location pos1) { this.pos1 = pos1; }
    public void setPos2(Location pos2) { this.pos2 = pos2; }
    public Location getStart() { return start; }
    public void setStart(Location start) { this.start = start; }
    public Location getFinish() { return finish; }
    public void setFinish(Location finish) { this.finish = finish; }
    public Map<Integer, Location> getCheckpoints() { return checkpoints; }
    public List<String> getFinishCommands() { return finishCommands; }
    public List<String> getBlacklist() { return blacklist; }
    public double getFallY() { return fallY; }
    public void setFallY(double fallY) { this.fallY = fallY; }

    // cooldown now int
    public int getCooldown() { return cooldown; }
    public void setCooldown(int cooldown) { this.cooldown = cooldown; }

    public boolean isInside(Location loc) {
        if (pos1 == null || pos2 == null) return false;
        double x1 = Math.min(pos1.getX(), pos2.getX());
        double y1 = Math.min(pos1.getY(), pos2.getY());
        double z1 = Math.min(pos1.getZ(), pos2.getZ());
        double x2 = Math.max(pos1.getX(), pos2.getX());
        double y2 = Math.max(pos1.getY(), pos2.getY());
        double z2 = Math.max(pos1.getZ(), pos2.getZ());

        return loc.getX() >= x1 && loc.getX() <= x2 &&
               loc.getY() >= y1 && loc.getY() <= y2 &&
               loc.getZ() >= z1 && loc.getZ() <= z2 &&
               loc.getWorld().equals(pos1.getWorld());
    }

    public Location getCenter() {
        if (pos1 == null || pos2 == null) return null;
        return new Location(pos1.getWorld(),
                (pos1.getX() + pos2.getX()) / 2.0,
                (pos1.getY() + pos2.getY()) / 2.0,
                (pos1.getZ() + pos2.getZ()) / 2.0);
    }
}
