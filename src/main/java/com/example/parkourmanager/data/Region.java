package com.example.parkourmanager.data;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

import java.util.*;

public class Region {
    private final String name;
    private Location pos1;
    private Location pos2;
    private Location start;
    private Location finish;
    private final Map<Integer, Location> checkpoints;
    private final Set<Material> blacklist;
    private final List<String> finishCommands;
    private Integer fallY;

    public Region(String name, Location pos1, Location pos2) {
        this.name = name;
        this.pos1 = pos1;
        this.pos2 = pos2;
        this.checkpoints = new HashMap<>();
        this.blacklist = new HashSet<>();
        this.finishCommands = new ArrayList<>();
        this.fallY = null;
    }

    public String getName() {
        return name;
    }

    public Location getPos1() {
        return pos1;
    }

    public Location getPos2() {
        return pos2;
    }

    public void setPos1(Location pos1) {
        this.pos1 = pos1;
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

    public void addCheckpoint(int number, Location loc) {
        checkpoints.put(number, loc);
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

    public void addBlacklist(Material material) {
        blacklist.add(material);
    }

    public void removeBlacklist(Material material) {
        blacklist.remove(material);
    }

    public Set<Material> getBlacklist() {
        return blacklist;
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

    public Integer getFallY() {
        return fallY;
    }

    public void setFallY(Integer fallY) {
        this.fallY = fallY;
    }

    public void spawnOutline(Particle particle, Player player) {
        if (pos1 == null || pos2 == null) return;

        int minX = Math.min(pos1.getBlockX(), pos2.getBlockX());
        int maxX = Math.max(pos1.getBlockX(), pos2.getBlockX());
        int minY = Math.min(pos1.getBlockY(), pos2.getBlockY());
        int maxY = Math.max(pos1.getBlockY(), pos2.getBlockY());
        int minZ = Math.min(pos1.getBlockZ(), pos2.getBlockZ());
        int maxZ = Math.max(pos1.getBlockZ(), pos2.getBlockZ());

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                player.spawnParticle(particle, new Location(pos1.getWorld(), x, y, minZ), 1);
                player.spawnParticle(particle, new Location(pos1.getWorld(), x, y, maxZ), 1);
            }
        }

        for (int z = minZ; z <= maxZ; z++) {
            for (int y = minY; y <= maxY; y++) {
                player.spawnParticle(particle, new Location(pos1.getWorld(), minX, y, z), 1);
                player.spawnParticle(particle, new Location(pos1.getWorld(), maxX, y, z), 1);
            }
        }

        for (int x = minX; x <= maxX; x++) {
            for (int z = minZ; z <= maxZ; z++) {
                player.spawnParticle(particle, new Location(pos1.getWorld(), x, minY, z), 1);
                player.spawnParticle(particle, new Location(pos1.getWorld(), x, maxY, z), 1);
            }
        }
    }

    public boolean contains(Location loc) {
        if (pos1 == null || pos2 == null || loc == null) return false;

        int minX = Math.min(pos1.getBlockX(), pos2.getBlockX());
        int maxX = Math.max(pos1.getBlockX(), pos2.getBlockX());
        int minY = Math.min(pos1.getBlockY(), pos2.getBlockY());
        int maxY = Math.max(pos1.getBlockY(), pos2.getBlockY());
        int minZ = Math.min(pos1.getBlockZ(), pos2.getBlockZ());
        int maxZ = Math.max(pos1.getBlockZ(), pos2.getBlockZ());

        return loc.getBlockX() >= minX && loc.getBlockX() <= maxX &&
               loc.getBlockY() >= minY && loc.getBlockY() <= maxY &&
               loc.getBlockZ() >= minZ && loc.getBlockZ() <= maxZ;
    }
}
