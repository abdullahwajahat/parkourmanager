package com.example.parkourmanager.data;

import org.bukkit.Location;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class Region {
    private final String name;
    private Location pos1;
    private Location pos2;
    private final List<Location> checkpoints;
    private List<String> finishCommands;
    private int fallY;
    private List<Material> blacklistBlocks;

    // ✅ Constructor matches RegionManager (name, pos1, pos2)
    public Region(String name, Location pos1, Location pos2) {
        this.name = name;
        this.pos1 = pos1;
        this.pos2 = pos2;
        this.checkpoints = new ArrayList<>();
        this.finishCommands = new ArrayList<>();
        this.blacklistBlocks = new ArrayList<>();
        this.fallY = 0; // default
    }

    // ✅ Basic getters/setters
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

    // ✅ Checkpoints
    public List<Location> getCheckpoints() {
        return checkpoints;
    }

    public void addCheckpoint(Location checkpoint) {
        checkpoints.add(checkpoint);
    }

    public void setCheckpoint(int index, Location checkpoint) {
        if (index < checkpoints.size()) {
            checkpoints.set(index, checkpoint);
        } else {
            checkpoints.add(checkpoint);
        }
    }

    // ✅ Finish commands
    public List<String> getFinishCommands() {
        return finishCommands;
    }

    public void setFinishCommands(List<String> finishCommands) {
        this.finishCommands = finishCommands;
    }

    public void addFinishCommand(String command) {
        this.finishCommands.add(command);
    }

    // ✅ Fall Y level
    public int getFallY() {
        return fallY;
    }

    public void setFallY(int fallY) {
        this.fallY = fallY;
    }

    // ✅ Blacklist blocks
    public List<Material> getBlacklistBlocks() {
        return blacklistBlocks;
    }

    public void setBlacklistBlocks(List<Material> blacklistBlocks) {
        this.blacklistBlocks = blacklistBlocks;
    }

    public void addBlacklistBlock(Material material) {
        this.blacklistBlocks.add(material);
    }
}
