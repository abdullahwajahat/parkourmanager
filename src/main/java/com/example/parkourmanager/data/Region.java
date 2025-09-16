package com.example.parkourmanager.data;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Region {

    private final String id;
    private Location pos1;
    private Location pos2;
    private Location start;
    private Location finish;
    private final List<Location> checkpoints = new ArrayList<>();

    private final List<String> finishCommands = new ArrayList<>();
    private final List<Material> blacklistBlocks = new ArrayList<>();

    private int fallY = -64;
    private int cooldown = 0;

    public Region(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setPos1(Location pos1) {
        this.pos1 = pos1;
    }

    public void setPos2(Location pos2) {
        this.pos2 = pos2;
    }

    public Location getPos1() {
        return pos1;
    }

    public Location getPos2() {
        return pos2;
    }

    public void setStart(Location start) {
        this.start = start;
    }

    public Location getStart() {
        return start;
    }

    public void setFinish(Location finish) {
        this.finish = finish;
    }

    public Location getFinish() {
        return finish;
    }

    public void addCheckpoint(Location loc) {
        checkpoints.add(loc);
    }

    public Location getCheckpoint(int index) {
        if (index >= 0 && index < checkpoints.size()) {
            return checkpoints.get(index);
        }
        return null;
    }

    public List<Location> getCheckpoints() {
        return checkpoints;
    }

    // ============= Fixes for your errors =============

    public List<String> getFinishCommands() {
        return finishCommands;
    }

    public void addFinishCommand(String command) {
        finishCommands.add(command);
    }

    public int getFallY() {
        return fallY;
    }

    public void setFallY(int fallY) {
        this.fallY = fallY;
    }

    public List<Material> getBlacklist() {
        return blacklistBlocks;
    }

    public void addBlacklistBlock(Material material) {
        blacklistBlocks.add(material);
    }

    public int getCooldown() {
        return cooldown;
    }

    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }

    // Check if a location is inside region
    public boolean isInside(Location loc) {
        if (pos1 == null || pos2 == null || !pos1.getWorld().equals(loc.getWorld())) return false;

        double x1 = Math.min(pos1.getX(), pos2.getX());
        double y1 = Math.min(pos1.getY(), pos2.getY());
        double z1 = Math.min(pos1.getZ(), pos2.getZ());

        double x2 = Math.max(pos1.getX(), pos2.getX());
        double y2 = Math.max(pos1.getY(), pos2.getY());
        double z2 = Math.max(pos1.getZ(), pos2.getZ());

        return loc.getX() >= x1 && loc.getX() <= x2
                && loc.getY() >= y1 && loc.getY() <= y2
                && loc.getZ() >= z1 && loc.getZ() <= z2;
    }

    // Used in debug/info commands
    public String toInfoString() {
        return "Region{" +
                "id='" + id + '\'' +
                ", pos1=" + (pos1 != null ? pos1.toVector() : "null") +
                ", pos2=" + (pos2 != null ? pos2.toVector() : "null") +
                ", start=" + (start != null ? start.toVector() : "null") +
                ", finish=" + (finish != null ? finish.toVector() : "null") +
                ", checkpoints=" + checkpoints.size() +
                ", fallY=" + fallY +
                ", cooldown=" + cooldown +
                '}';
    }

    // Outline with particles
    public void spawnOutline(Particle particle, Player player) {
        if (pos1 == null || pos2 == null) return;

        double minX = Math.min(pos1.getX(), pos2.getX());
        double maxX = Math.max(pos1.getX(), pos2.getX());
        double minY = Math.min(pos1.getY(), pos2.getY());
        double maxY = Math.max(pos1.getY(), pos2.getY());
        double minZ = Math.min(pos1.getZ(), pos2.getZ());
        double maxZ = Math.max(pos1.getZ(), pos2.getZ());

        for (double x = minX; x <= maxX; x += 1.0) {
            for (double y = minY; y <= maxY; y += 1.0) {
                player.spawnParticle(particle, new Location(pos1.getWorld(), x, y, minZ), 1);
                player.spawnParticle(particle, new Location(pos1.getWorld(), x, y, maxZ), 1);
            }
        }
        for (double z = minZ; z <= maxZ; z += 1.0) {
            for (double y = minY; y <= maxY; y += 1.0) {
                player.spawnParticle(particle, new Location(pos1.getWorld(), minX, y, z), 1);
                player.spawnParticle(particle, new Location(pos1.getWorld(), maxX, y, z), 1);
            }
        }
    }

    // Save to config
    public void saveToConfig(ConfigurationSection section) {
        section.set("pos1", pos1);
        section.set("pos2", pos2);
        section.set("start", start);
        section.set("finish", finish);
        section.set("checkpoints", checkpoints);
        section.set("finishCommands", finishCommands);
        section.set("fallY", fallY);
        section.set("cooldown", cooldown);

        List<String> blacklistNames = new ArrayList<>();
        for (Material m : blacklistBlocks) {
            blacklistNames.add(m.name());
        }
        section.set("blacklist", blacklistNames);
    }

    // Load from config
    public static Region loadFromConfig(String id, ConfigurationSection section) {
        Region region = new Region(id);
        region.pos1 = (Location) section.get("pos1");
        region.pos2 = (Location) section.get("pos2");
        region.start = (Location) section.get("start");
        region.finish = (Location) section.get("finish");

        region.checkpoints.addAll((List<Location>) section.getList("checkpoints", new ArrayList<>()));
        region.finishCommands.addAll(section.getStringList("finishCommands"));

        region.fallY = section.getInt("fallY", -64);
        region.cooldown = section.getInt("cooldown", 0);

        List<String> blacklistNames = section.getStringList("blacklist");
        for (String name : blacklistNames) {
            Material mat = Material.matchMaterial(name);
            if (mat != null) {
                region.blacklistBlocks.add(mat);
            }
        }

        return region;
    }
}
