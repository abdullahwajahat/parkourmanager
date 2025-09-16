package com.example.parkourmanager.data;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.*;

public class Region {
    private String name;
    private Location pos1;
    private Location pos2;
    private Location start;
    private Location finish;
    private final Map<Integer, Location> checkpoints = new HashMap<>();
    private final List<String> finishCommands = new ArrayList<>();
    private final Set<String> blacklistBlocks = new HashSet<>();
    private double fallY = Double.NaN;

    public Region(String name, Location pos1, Location pos2) {
        this.name = name;
        this.pos1 = pos1;
        this.pos2 = pos2;
    }

    public String getName() { return name; }
    public Location getStart() { return start; }
    public Location getFinish() { return finish; }
    public void setStart(Location loc) { this.start = loc; }
    public void setFinish(Location loc) { this.finish = loc; }
    public Map<Integer, Location> getCheckpoints() { return checkpoints; }

    // 🔹 Get checkpoint
    public Location getCheckpoint(int number) {
        return checkpoints.get(number);
    }

    // 🔹 Check if location is inside region
    public boolean isInside(Location loc) {
        if (pos1 == null || pos2 == null || loc == null || !loc.getWorld().equals(pos1.getWorld())) return false;

        double x1 = Math.min(pos1.getX(), pos2.getX());
        double x2 = Math.max(pos1.getX(), pos2.getX());
        double y1 = Math.min(pos1.getY(), pos2.getY());
        double y2 = Math.max(pos1.getY(), pos2.getY());
        double z1 = Math.min(pos1.getZ(), pos2.getZ());
        double z2 = Math.max(pos1.getZ(), pos2.getZ());

        double x = loc.getX();
        double y = loc.getY();
        double z = loc.getZ();

        return (x >= x1 && x <= x2) &&
               (y >= y1 && y <= y2) &&
               (z >= z1 && z <= z2);
    }

    // 🔹 Info string for /pm info
    public String toInfoString() {
        return "&aRegion: &f" + name +
               " &aStart: &f" + (start != null ? start.toVector().toString() : "none") +
               " &aFinish: &f" + (finish != null ? finish.toVector().toString() : "none") +
               " &aCheckpoints: &f" + checkpoints.size() +
               " &aBlacklist: &f" + blacklistBlocks.size();
    }

    // 🔹 Show particle outline
    public void spawnOutline(Particle particle, Player player) {
        if (pos1 == null || pos2 == null) return;

        double minX = Math.min(pos1.getX(), pos2.getX());
        double maxX = Math.max(pos1.getX(), pos2.getX());
        double minY = Math.min(pos1.getY(), pos2.getY());
        double maxY = Math.max(pos1.getY(), pos2.getY());
        double minZ = Math.min(pos1.getZ(), pos2.getZ());
        double maxZ = Math.max(pos1.getZ(), pos2.getZ());

        for (double x = minX; x <= maxX; x++) {
            for (double y = minY; y <= maxY; y++) {
                player.spawnParticle(particle, new Location(pos1.getWorld(), x, y, minZ), 1);
                player.spawnParticle(particle, new Location(pos1.getWorld(), x, y, maxZ), 1);
            }
        }
        for (double z = minZ; z <= maxZ; z++) {
            for (double y = minY; y <= maxY; y++) {
                player.spawnParticle(particle, new Location(pos1.getWorld(), minX, y, z), 1);
                player.spawnParticle(particle, new Location(pos1.getWorld(), maxX, y, z), 1);
            }
        }
    }

    // 🔹 Save region data to config
    public void saveToConfig(ConfigurationSection section) {
        section.set("pos1", pos1);
        section.set("pos2", pos2);
        section.set("start", start);
        section.set("finish", finish);
        section.set("fallY", fallY);
        section.set("blacklist", new ArrayList<>(blacklistBlocks));
        section.set("finishCommands", finishCommands);

        ConfigurationSection cpSec = section.createSection("checkpoints");
        for (Map.Entry<Integer, Location> entry : checkpoints.entrySet()) {
            cpSec.set(String.valueOf(entry.getKey()), entry.getValue());
        }
    }

    // 🔹 Load region data from config
    public static Region loadFromConfig(String name, ConfigurationSection section) {
        Location pos1 = (Location) section.get("pos1");
        Location pos2 = (Location) section.get("pos2");
        Region region = new Region(name, pos1, pos2);

        region.start = (Location) section.get("start");
        region.finish = (Location) section.get("finish");
        region.fallY = section.getDouble("fallY", Double.NaN);
        region.blacklistBlocks.addAll(section.getStringList("blacklist"));
        region.finishCommands.addAll(section.getStringList("finishCommands"));

        ConfigurationSection cpSec = section.getConfigurationSection("checkpoints");
        if (cpSec != null) {
            for (String key : cpSec.getKeys(false)) {
                region.checkpoints.put(Integer.parseInt(key), (Location) cpSec.get(key));
            }
        }

        return region;
    }
}
