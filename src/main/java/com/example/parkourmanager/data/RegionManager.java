package com.example.parkourmanager.data;

import com.example.parkourmanager.ParkourManager;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.*;

public class RegionManager {
    private final ParkourManager plugin;
    private final Map<String, Region> regions = new HashMap<>();

    public RegionManager(ParkourManager plugin) {
        this.plugin = plugin;
    }

    public void createRegion(Player player, String name) {
        Location pos1 = plugin.getSelectionManager().getPos1(player);
        Location pos2 = plugin.getSelectionManager().getPos2(player);
        if (pos1 == null || pos2 == null) return;

        Region region = new Region(name, pos1, pos2);
        regions.put(name, region);
        saveRegions();
    }

    public Region getRegion(String name) {
        return regions.get(name);
    }

    public Region getRegionAt(Location loc) {
        for (Region r : regions.values()) {
            if (r.isInside(loc)) return r;
        }
        return null;
    }

    public List<String> listRegions() {
        return new ArrayList<>(regions.keySet());
    }

    public void deleteRegion(String name) {
        regions.remove(name);
        saveRegions();
    }

    // 🔹 Get all regions
    public Collection<Region> getRegions() {
        return regions.values();
    }

    public void setStart(Player player, Location loc) {
        Region r = getRegionAt(loc);
        if (r != null) {
            r.setStart(loc);
            saveRegions();
        }
    }

    public void setFinish(Player player, Location loc) {
        Region r = getRegionAt(loc);
        if (r != null) {
            r.setFinish(loc);
            saveRegions();
        }
    }

    public void addCheckpoint(Player player, int num, Location loc) {
        Region r = getRegionAt(loc);
        if (r != null) {
            r.getCheckpoints().put(num, loc);
            saveRegions();
        }
    }

    public void editCheckpoint(Player player, int num, Location loc) {
        Region r = getRegionAt(loc);
        if (r != null) {
            r.getCheckpoints().put(num, loc);
            saveRegions();
        }
    }

    public void removeCheckpoint(String regionName, int num) {
        Region r = getRegion(regionName);
        if (r != null) {
            r.getCheckpoints().remove(num);
            saveRegions();
        }
    }

    public void addFinishCommand(String regionName, String cmd) {
        Region r = getRegion(regionName);
        if (r != null) {
            r.finishCommands.add(cmd);
            saveRegions();
        }
    }

    public void removeFinishCommand(String regionName, int index) {
        Region r = getRegion(regionName);
        if (r != null && index >= 0 && index < r.finishCommands.size()) {
            r.finishCommands.remove(index);
            saveRegions();
        }
    }

    public void setFinishCommand(String regionName, int index, String cmd) {
        Region r = getRegion(regionName);
        if (r != null && index >= 0 && index < r.finishCommands.size()) {
            r.finishCommands.set(index, cmd);
            saveRegions();
        }
    }

    public void setFallY(String regionName, int y) {
        Region r = getRegion(regionName);
        if (r != null) {
            r.fallY = y;
            saveRegions();
        }
    }

    public void setCooldown(String regionName, String time) {
        // placeholder if you want cooldown system later
    }

    public void addBlacklist(String regionName, org.bukkit.Material mat) {
        Region r = getRegion(regionName);
        if (r != null) {
            r.blacklistBlocks.add(mat.name());
            saveRegions();
        }
    }

    public void removeBlacklist(String regionName, org.bukkit.Material mat) {
        Region r = getRegion(regionName);
        if (r != null) {
            r.blacklistBlocks.remove(mat.name());
            saveRegions();
        }
    }

    public Location getRegionMiddle(String regionName) {
        Region r = getRegion(regionName);
        if (r == null) return null;

        double x = (r.getStart().getX() + r.getFinish().getX()) / 2;
        double y = (r.getStart().getY() + r.getFinish().getY()) / 2;
        double z = (r.getStart().getZ() + r.getFinish().getZ()) / 2;

        return new Location(r.getStart().getWorld(), x, y, z);
    }

    public void showRegionOutline(String name, Player player, Particle particle) {
        Region r = getRegion(name);
        if (r != null) r.spawnOutline(particle, player);
    }

    public String getRegionInfo(String name) {
        Region r = getRegion(name);
        return r != null ? r.toInfoString() : "No region found.";
    }

    public void saveRegions() {
        plugin.getConfig().set("regions", null);
        ConfigurationSection section = plugin.getConfig().createSection("regions");
        for (Region r : regions.values()) {
            ConfigurationSection rs = section.createSection(r.getName());
            r.saveToConfig(rs);
        }
        plugin.saveConfig();
    }

    public void loadRegions() {
        regions.clear();
        ConfigurationSection section = plugin.getConfig().getConfigurationSection("regions");
        if (section != null) {
            for (String key : section.getKeys(false)) {
                Region r = Region.loadFromConfig(key, section.getConfigurationSection(key));
                regions.put(key, r);
            }
        }
    }
}
