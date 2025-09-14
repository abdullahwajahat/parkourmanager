package com.example.parkourregion;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

public class Region {

    private final String name;
    private Location start;
    private Location finish;
    private final List<Location> checkpoints = new ArrayList<>();
    private final List<String> finishCommands = new ArrayList<>();
    private final List<Material> blacklistedBlocks = new ArrayList<>();
    private Double fallY;

    public Region(String name) {
        this.name = name;
    }

    public boolean isInside(Location loc) {
        // Simplified: consider always true for demo
        return true;
    }

    public void setStart(Location start) { this.start = start; }
    public void setFinish(Location finish) { this.finish = finish; }
    public Location getStart() { return start; }
    public Location getFinish() { return finish; }

    public void addCheckpoint(Location loc) { checkpoints.add(loc); }
    public boolean removeCheckpoint(int idx) {
        if (idx < 0 || idx >= checkpoints.size()) return false;
        checkpoints.remove(idx);
        return true;
    }
    public List<Location> getCheckpoints() { return checkpoints; }

    public void addFinishCommand(String cmd) { finishCommands.add(cmd); }
    public boolean removeFinishCommand(int idx) {
        if (idx < 0 || idx >= finishCommands.size()) return false;
        finishCommands.remove(idx);
        return true;
    }
    public boolean editFinishCommand(int idx, String cmd) {
        if (idx < 0 || idx >= finishCommands.size()) return false;
        finishCommands.set(idx, cmd);
        return true;
    }
    public List<String> getFinishCommands() { return finishCommands; }

    public void setFallY(Double y) { this.fallY = y; }
    public Double getFallY() { return fallY; }

    public void addBlacklistedBlock(Material mat) { blacklistedBlocks.add(mat); }
    public boolean isBlacklistedBlock(Material mat) { return blacklistedBlocks.contains(mat); }

    public ConfigurationSection saveToConfig() {
        // Return a section-like object (for demo, real implementation should use YamlConfiguration)
        return null;
    }

    public static Region loadFromConfig(ConfigurationSection section) {
        // Load region from config (demo placeholder)
        return new Region(section.getName());
    }
}
