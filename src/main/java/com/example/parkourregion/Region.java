package com.example.parkourregion;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

public class Region {

    private final String name;
    private Location corner1; // first corner of the cuboid
    private Location corner2; // opposite corner
    private Location start;
    private Location finish;
    private final List<Location> checkpoints = new ArrayList<>();
    private final List<String> finishCommands = new ArrayList<>();
    private final List<Material> blacklistedBlocks = new ArrayList<>();
    private Double fallY;

    public Region(String name) {
        this.name = name;
    }

    // ---------------------------
    // Cuboid detection
    // ---------------------------
    public boolean isInside(Location loc) {
        if (corner1 == null || corner2 == null) return false;
        if (!loc.getWorld().equals(corner1.getWorld())) return false;

        double xMin = Math.min(corner1.getX(), corner2.getX());
        double xMax = Math.max(corner1.getX(), corner2.getX());
        double yMin = Math.min(corner1.getY(), corner2.getY());
        double yMax = Math.max(corner1.getY(), corner2.getY());
        double zMin = Math.min(corner1.getZ(), corner2.getZ());
        double zMax = Math.max(corner1.getZ(), corner2.getZ());

        double x = loc.getX();
        double y = loc.getY();
        double z = loc.getZ();

        return x >= xMin && x <= xMax &&
               y >= yMin && y <= yMax &&
               z >= zMin && z <= zMax;
    }

    // ---------------------------
    // Setters & Getters
    // ---------------------------
    public String getName() { return name; }
    public void setCorner1(Location corner1) { this.corner1 = corner1; }
    public void setCorner2(Location corner2) { this.corner2 = corner2; }
    public Location getCorner1() { return corner1; }
    public Location getCorner2() { return corner2; }

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

    // ---------------------------
    // Save & Load
    // ---------------------------
    public void saveToConfig(ConfigurationSection section) {
        if (corner1 != null) {
            section.set("corner1.world", corner1.getWorld().getName());
            section.set("corner1.x", corner1.getX());
            section.set("corner1.y", corner1.getY());
            section.set("corner1.z", corner1.getZ());
        }
        if (corner2 != null) {
            section.set("corner2.world", corner2.getWorld().getName());
            section.set("corner2.x", corner2.getX());
            section.set("corner2.y", corner2.getY());
            section.set("corner2.z", corner2.getZ());
        }

        if (start != null) {
            section.set("start.world", start.getWorld().getName());
            section.set("start.x", start.getX());
            section.set("start.y", start.getY());
            section.set("start.z", start.getZ());
        }

        if (finish != null) {
            section.set("finish.world", finish.getWorld().getName());
            section.set("finish.x", finish.getX());
            section.set("finish.y", finish.getY());
            section.set("finish.z", finish.getZ());
        }

        section.set("fallY", fallY != null ? fallY : null);

        // Checkpoints
        for (int i = 0; i < checkpoints.size(); i++) {
            Location loc = checkpoints.get(i);
            section.set("checkpoints." + i + ".world", loc.getWorld().getName());
            section.set("checkpoints." + i + ".x", loc.getX());
            section.set("checkpoints." + i + ".y", loc.getY());
            section.set("checkpoints." + i + ".z", loc.getZ());
        }

        section.set("finishCommands", finishCommands);

        List<String> blocks = new ArrayList<>();
        for (Material mat : blacklistedBlocks) blocks.add(mat.name());
        section.set("blacklistedBlocks", blocks);
    }

    public static Region loadFromConfig(ConfigurationSection section) {
        Region region = new Region(section.getName());

        if (section.contains("corner1.world")) {
            World w = Bukkit.getWorld(section.getString("corner1.world"));
            double x = section.getDouble("corner1.x");
            double y = section.getDouble("corner1.y");
            double z = section.getDouble("corner1.z");
            region.setCorner1(new Location(w, x, y, z));
        }

        if (section.contains("corner2.world")) {
            World w = Bukkit.getWorld(section.getString("corner2.world"));
            double x = section.getDouble("corner2.x");
            double y = section.getDouble("corner2.y");
            double z = section.getDouble("corner2.z");
            region.setCorner2(new Location(w, x, y, z));
        }

        if (section.contains("start.world")) {
            World w = Bukkit.getWorld(section.getString("start.world"));
            double x = section.getDouble("start.x");
            double y = section.getDouble("start.y");
            double z = section.getDouble("start.z");
            region.setStart(new Location(w, x, y, z));
        }

        if (section.contains("finish.world")) {
            World w = Bukkit.getWorld(section.getString("finish.world"));
            double x = section.getDouble("finish.x");
            double y = section.getDouble("finish.y");
            double z = section.getDouble("finish.z");
            region.setFinish(new Location(w, x, y, z));
        }

        if (section.contains("fallY")) region.setFallY(section.getDouble("fallY"));

        if (section.contains("checkpoints")) {
            ConfigurationSection cps = section.getConfigurationSection("checkpoints");
            for (String key : cps.getKeys(false)) {
                World w = Bukkit.getWorld(cps.getString(key + ".world"));
                double x = cps.getDouble(key + ".x");
                double y = cps.getDouble(key + ".y");
                double z = cps.getDouble(key + ".z");
                region.addCheckpoint(new Location(w, x, y, z));
            }
        }

        if (section.contains("finishCommands")) region.getFinishCommands().addAll(section.getStringList("finishCommands"));

        if (section.contains("blacklistedBlocks")) {
            for (String s : section.getStringList("blacklistedBlocks")) {
                Material mat = Material.matchMaterial(s);
                if (mat != null) region.addBlacklistedBlock(mat);
            }
        }

        return region;
    }
}
