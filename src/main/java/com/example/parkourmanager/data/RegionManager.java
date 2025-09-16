package com.example.parkourmanager.data;

import com.example.parkourmanager.ParkourManager;
import com.example.parkourmanager.utils.MessageUtil;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.*;

public class RegionManager {

    private final ParkourManager plugin;
    private final Map<String, Region> regions = new HashMap<>();

    public RegionManager(ParkourManager plugin) {
        this.plugin = plugin;
        loadRegions();
    }

    // ========== REGION CREATION ==========
    public void createRegion(Player player, String name) {
        if (regions.containsKey(name)) {
            MessageUtil.send(player, "region-exists", Map.of("region", name));
            return;
        }

        Location pos1 = plugin.getSelectionManager().getPos1(player);
        Location pos2 = plugin.getSelectionManager().getPos2(player);
        if (pos1 == null || pos2 == null) {
            MessageUtil.send(player, "need-selection");
            return;
        }

        Region region = new Region(name, pos1, pos2);
        regions.put(name, region);
        saveRegions();
    }

    public void deleteRegion(String name) {
        regions.remove(name);
        saveRegions();
    }

    // ========== GETTERS ==========
    public Region getRegion(String name) {
        return regions.get(name);
    }

    public List<String> listRegions() {
        return new ArrayList<>(regions.keySet());
    }

    public Region getRegionAt(Location loc) {
        for (Region r : regions.values()) {
            if (r.isInside(loc)) {
                return r;
            }
        }
        return null;
    }

    public Location getRegionMiddle(String name) {
        Region r = getRegion(name);
        if (r == null) return null;
        return r.getMiddle();
    }

    public String getRegionInfo(String name) {
        Region r = getRegion(name);
        if (r == null) return "Unknown region.";
        return r.toInfoString();
    }

    // ========== REGION SETTINGS ==========
    public void setStart(Player player, Location loc) {
        Region region = getRegionAt(loc);
        if (region == null) {
            MessageUtil.send(player, "no-region-info");
            return;
        }
        region.setStart(loc);
        saveRegions();
    }

    public void setFinish(Player player, Location loc) {
        Region region = getRegionAt(loc);
        if (region == null) {
            MessageUtil.send(player, "no-region-info");
            return;
        }
        region.setFinish(loc);
        saveRegions();
    }

    public void addCheckpoint(String regionName, int number, Location loc) {
        Region r = getRegion(regionName);
        if (r == null) return;
        r.addCheckpoint(number, loc);
        saveRegions();
    }

    public void editCheckpoint(String regionName, int number, Location loc) {
        Region r = getRegion(regionName);
        if (r == null) return;
        r.editCheckpoint(number, loc);
        saveRegions();
    }

    public void removeCheckpoint(String regionName, int number) {
        Region r = getRegion(regionName);
        if (r == null) return;
        r.removeCheckpoint(number);
        saveRegions();
    }

    public void addFinishCommand(String regionName, String cmd) {
        Region r = getRegion(regionName);
        if (r == null) return;
        r.addFinishCommand(cmd);
        saveRegions();
    }

    public void removeFinishCommand(String regionName, int index) {
        Region r = getRegion(regionName);
        if (r == null) return;
        r.removeFinishCommand(index);
        saveRegions();
    }

    public void setFinishCommand(String regionName, int index, String cmd) {
        Region r = getRegion(regionName);
        if (r == null) return;
        r.setFinishCommand(index, cmd);
        saveRegions();
    }

    public void setFallY(String regionName, int y) {
        Region r = getRegion(regionName);
        if (r == null) return;
        r.setFallY(y);
        saveRegions();
    }

    public void setCooldown(String regionName, String time) {
        Region r = getRegion(regionName);
        if (r == null) return;
        r.setCooldown(time);
        saveRegions();
    }

    public void addBlacklist(String regionName, Material mat) {
        Region r = getRegion(regionName);
        if (r == null) return;
        r.addBlacklist(mat);
        saveRegions();
    }

    public void removeBlacklist(String regionName, Material mat) {
        Region r = getRegion(regionName);
        if (r == null) return;
        r.removeBlacklist(mat);
        saveRegions();
    }

    // ========== OUTLINE ==========
    public void showRegionOutline(String name, Player player, Particle particle) {
        Region r = getRegion(name);
        if (r == null) return;

        Bukkit.getScheduler().runTaskTimer(plugin, task -> {
            if (!player.isOnline()) {
                task.cancel();
                return;
            }
            r.spawnOutline(particle, player);
        }, 0L, 20L); // Show outline every second
    }

    // ========== SAVE/LOAD ==========
    public void saveRegions() {
        FileConfiguration config = plugin.getConfig();
        config.set("regions", null); // clear old data

        for (Region r : regions.values()) {
            String path = "regions." + r.getName();
            r.saveToConfig(config.createSection(path));
        }

        plugin.saveConfig();
    }

    public void loadRegions() {
        regions.clear();
        FileConfiguration config = plugin.getConfig();
        ConfigurationSection section = config.getConfigurationSection("regions");
        if (section == null) return;

        for (String key : section.getKeys(false)) {
            ConfigurationSection rs = section.getConfigurationSection(key);
            if (rs != null) {
                Region r = Region.loadFromConfig(key, rs);
                regions.put(key, r);
            }
        }
    }
}
