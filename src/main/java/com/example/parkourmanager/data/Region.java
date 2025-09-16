package com.example.parkourmanager.data;

import org.bukkit.Location;
import org.bukkit.Material;

import java.util.*;

public class Region {

    private final String name;
    private Location pos1;
    private Location pos2;
    private Location start;
    private Location finish;
    private final Map<Integer, Location> checkpoints;
    private final List<String> finishCommands;
    private final Set<Material> blacklistBlocks;
    private int fallY = Integer.MIN_VALUE; // Default disabled
    private long cooldownMillis = 0; // Default no cooldown

    public Region(String name) {
        this.name = name;
        this.checkpoints = new HashMap<>();
        this.finishCommands = new ArrayList<>();
        this.blacklistBlocks = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public Location getPos1() {
        return pos1;
    }

    public void setPos1(Location pos1) {
        this.pos1 = pos1;
    }

    public Location getPos2() {
        return pos2;
    }

    public void setPos2(Location pos2) {
        this.pos2 = pos2;
    }

    public Location getStart() {
        return start;
    }

    public void setStart(Location start) {
        this.start = start;
    }

    public Location getFinish() {
        return finish;
    }

    public void setFinish(Location finish) {
        this.finish = finish;
    }

    // ✅ Added missing methods

    public void setCheckpoint(int number, Location location) {
        checkpoints.put(number, location);
    }

    public void removeCheckpoint(int number) {
        checkpoints.remove(number);
    }

    public Location getCheckpoint(int number) {
        return checkpoints.get(number);
    }

    public Map<Integer, Location> getCheckpoints() {
        return checkpoints;
    }

    public void addFinishCommand(String command) {
        finishCommands.add(command);
    }

    public void removeFinishCommand(int index) {
        if (index >= 0 && index < finishCommands.size()) {
            finishCommands.remove(index);
        }
    }

    public void setFinishCommand(int index, String command) {
        if (index >= 0 && index < finishCommands.size()) {
            finishCommands.set(index, command);
        }
    }

    public List<String> getFinishCommands() {
        return finishCommands;
    }

    public void setFinishCommands(List<String> commands) {
        finishCommands.clear();
        finishCommands.addAll(commands);
    }

    public void addBlacklistBlock(Material block) {
        blacklistBlocks.add(block);
    }

    public void removeBlacklistBlock(Material block) {
        blacklistBlocks.remove(block);
    }

    public Set<Material> getBlacklistBlocks() {
        return blacklistBlocks;
    }

    public void setBlacklistBlocks(List<Material> blocks) {
        blacklistBlocks.clear();
        blacklistBlocks.addAll(blocks);
    }

    public int getFallY() {
        return fallY;
    }

    public void setFallY(int fallY) {
        this.fallY = fallY;
    }

    public long getCooldownMillis() {
        return cooldownMillis;
    }

    public void setCooldownMillis(long cooldownMillis) {
        this.cooldownMillis = cooldownMillis;
    }

    // Utility method to check if a location is inside the region
    public boolean isInside(Location loc) {
        if (pos1 == null || pos2 == null || !Objects.equals(pos1.getWorld(), loc.getWorld())) {
            return false;
        }

        double minX = Math.min(pos1.getX(), pos2.getX());
        double maxX = Math.max(pos1.getX(), pos2.getX());
        double minY = Math.min(pos1.getY(), pos2.getY());
        double maxY = Math.max(pos1.getY(), pos2.getY());
        double minZ = Math.min(pos1.getZ(), pos2.getZ());
        double maxZ = Math.max(pos1.getZ(), pos2.getZ());

        return loc.getX() >= minX && loc.getX() <= maxX
                && loc.getY() >= minY && loc.getY() <= maxY
                && loc.getZ() >= minZ && loc.getZ() <= maxZ;
    }
}
