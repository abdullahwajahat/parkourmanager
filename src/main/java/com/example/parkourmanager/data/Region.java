package com.example.parkourmanager.data;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.*;

public class Region {

    private final String name;
    private final String worldName;

    private Location pos1;
    private Location pos2;
    private Location start;
    private Location finish;

    private final Map<Integer, Location> checkpoints = new HashMap<>();
    private final List<String> finishCommands = new ArrayList<>();
    private final Set<Material> blacklist = new HashSet<>();

    private int fallY = Integer.MIN_VALUE; // special: if not set, use region maxY
    private String cooldown = "0s";

    public Region(String name, Location pos1, Location pos2) {
        this.name = name;
        this.worldName = pos1.getWorld().getName();
        this.pos1 = pos1;
        this.pos2 = pos2;
    }

    // ----- Getters -----

    public String getName() {
        return name;
    }

    public String getWorldName() {
        return worldName;
    }

    public Location getPos1() {
        return pos1;
    }

    public Location getPos2() {
        return pos2;
    }

    public Location getStart() {
        return start;
    }

    public Location getFinish() {
        return finish;
    }

    public Map<Integer, Location> getCheckpoints() {
        return checkpoints;
    }

    public List<String> getFinishCommands() {
        return finishCommands;
    }

    public Set<Material> getBlacklist() {
        return blacklist;
    }

    public int getFallY() {
        return fallY;
    }

    public String getCooldown() {
        return cooldown;
    }

    // ----- Setters -----

    public void setPos1(Location pos1) {
        this.pos1 = pos1;
    }

    public void setPos2(Location pos2) {
        this.pos2 = pos2;
    }

    public void setStart(Location start) {
        this.start = start;
    }

    public void setFinish(Location finish) {
        this.finish = finish;
    }

    public void setFallY(int fallY) {
        this.fallY = fallY;
    }

    public void setCooldown(String cooldown) {
        this.cooldown = cooldown;
    }

    // ----- Checkpoints -----

    public void addCheckpoint(int number, Location loc) {
        checkpoints.put(number, loc);
    }

    public void removeCheckpoint(int number) {
        checkpoints.remove(number);
    }

    public void editCheckpoint(int number, Location loc) {
        checkpoints.put(number, loc);
    }

    // ----- Finish Commands -----

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

    // ----- Blacklist -----

    public void addBlacklist(Material material) {
        blacklist.add(material);
    }

    public void removeBlacklist(Material material) {
        blacklist.remove(material);
    }

    // ----- Region Info -----

    public String getInfo() {
        return "&eRegion: &f" + name +
                "\n&7World: &f" + worldName +
                "\n&7Start: &f" + (start != null ? formatLoc(start) : "Not set") +
                "\n&7Finish: &f" + (finish != null ? formatLoc(finish) : "Not set") +
                "\n&7Checkpoints: &f" + checkpoints.size() +
                "\n&7Finish Commands: &f" + finishCommands.size() +
                "\n&7Blacklist: &f" + blacklist.size() +
                "\n&7Fall Y: &f" + (fallY == Integer.MIN_VALUE ? "Not set" : fallY) +
                "\n&7Cooldown: &f" + cooldown;
    }

    private String formatLoc(Location loc) {
        return String.format("%d,%d,%d", loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
    }

    // ----- Utilities -----

    public boolean contains(Location loc) {
        if (!loc.getWorld().getName().equals(worldName)) return false;

        int x1 = Math.min(pos1.getBlockX(), pos2.getBlockX());
        int y1 = Math.min(pos1.getBlockY(), pos2.getBlockY());
        int z1 = Math.min(pos1.getBlockZ(), pos2.getBlockZ());

        int x2 = Math.max(pos1.getBlockX(), pos2.getBlockX());
        int y2 = Math.max(pos1.getBlockY(), pos2.getBlockY());
        int z2 = Math.max(pos1.getBlockZ(), pos2.getBlockZ());

        return loc.getBlockX() >= x1 && loc.getBlockX() <= x2
                && loc.getBlockY() >= y1 && loc.getBlockY() <= y2
                && loc.getBlockZ() >= z1 && loc.getBlockZ() <= z2;
    }

    public Location getMiddle() {
        int x = (pos1.getBlockX() + pos2.getBlockX()) / 2;
        int y = (pos1.getBlockY() + pos2.getBlockY()) / 2;
        int z = (pos1.getBlockZ() + pos2.getBlockZ()) / 2;
        return new Location(Bukkit.getWorld(worldName), x, y, z);
    }
}
