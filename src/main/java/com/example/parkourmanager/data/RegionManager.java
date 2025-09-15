package com.example.parkourmanager.data;

import com.example.parkourmanager.ParkourManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import java.util.*;

public class RegionManager {

    private final ParkourManager plugin;
    private final Map<String, Region> regions = new HashMap<>();

    public RegionManager(ParkourManager plugin) {
        this.plugin = plugin;
    }

    public Map<String, Region> getRegions() {
        return regions;
    }

    public Region getRegion(String name) {
        return regions.get(name.toLowerCase());
    }

    public void addRegion(Region region) {
        regions.put(region.getName().toLowerCase(), region);
    }

    public void removeRegion(String name) {
        regions.remove(name.toLowerCase());
    }

    public void saveRegions() {
        plugin.getConfig().set("regions", null); // clear old data

        for (Region region : regions.values()) {
            String path = "regions." + region.getName();
            plugin.getConfig().set(path + ".world", 
                region.getPos1() != null ? region.getPos1().getWorld().getName() : null);

            saveLocation(path + ".pos1", region.getPos1());
            saveLocation(path + ".pos2", region.getPos2());
            saveLocation(path + ".start", region.getStart());
            saveLocation(path + ".finish", region.getFinish());

            // Save checkpoints
            Map<String, Object> cps = new HashMap<>();
            for (Map.Entry<Integer, Location> entry : region.getCheckpoints().entrySet()) {
                cps.put(String.valueOf(entry.getKey()), serializeLocation(entry.getValue()));
            }
            plugin.getConfig().createSection(path + ".checkpoints", cps);

            // Save blacklist
            plugin.getConfig().set(path + ".blacklist", new ArrayList<>(region.getBlacklist()));

            // Save cooldown
            plugin.getConfig().set(path + ".cooldown", region.getCooldown());

            // Save fallY
            plugin.getConfig().set(path + ".fallY", region.getFallY());

            // Save finish commands
            plugin.getConfig().set(path + ".finishCommands", region.getFinishCommands());
        }

        plugin.saveConfig();
    }

    public void loadRegions() {
        regions.clear();

        ConfigurationSection sec = plugin.getConfig().getConfigurationSection("regions");
        if (sec == null) return;

        for (String name : sec.getKeys(false)) {
            String path = "regions." + name;

            Region region = new Region(name);
            region.setPos1(loadLocation(path + ".pos1"));
            region.setPos2(loadLocation(path + ".pos2"));
            region.setStart(loadLocation(path + ".start"));
            region.setFinish(loadLocation(path + ".finish"));

            // Load checkpoints
            ConfigurationSection cps = plugin.getConfig().getConfigurationSection(path + ".checkpoints");
            if (cps != null) {
                for (String key : cps.getKeys(false)) {
                    int index = Integer.parseInt(key);
                    Location loc = deserializeLocation(cps.getString(key));
                    region.getCheckpoints().put(index, loc);
                }
            }

            // Load blacklist
            List<String> blacklist = plugin.getConfig().getStringList(path + ".blacklist");
            region.getBlacklist().addAll(blacklist);

            // Load cooldown
            region.setCooldown(plugin.getConfig().getInt(path + ".cooldown", String.valueOf(0)));

            // Load fallY
            region.setFallY(plugin.getConfig().getDouble(path + ".fallY", Double.MIN_VALUE));

            // Load finish commands
            List<String> cmds = plugin.getConfig().getStringList(path + ".finishCommands");
            region.getFinishCommands().addAll(cmds);

            regions.put(name.toLowerCase(), region);
        }
    }

    private void saveLocation(String path, Location loc) {
        if (loc == null) {
            plugin.getConfig().set(path, null);
            return;
        }
        plugin.getConfig().set(path, serializeLocation(loc));
    }

    private Location loadLocation(String path) {
        String s = plugin.getConfig().getString(path);
        if (s == null) return null;
        return deserializeLocation(s);
    }

    private String serializeLocation(Location loc) {
        if (loc == null) return null;
        return loc.getWorld().getName() + "," +
                loc.getX() + "," +
                loc.getY() + "," +
                loc.getZ() + "," +
                loc.getYaw() + "," +
                loc.getPitch();
    }

    private Location deserializeLocation(String s) {
        if (s == null) return null;
        String[] parts = s.split(",");
        if (parts.length < 4) return null;
        return new Location(
                Bukkit.getWorld(parts[0]),
                Double.parseDouble(parts[1]),
                Double.parseDouble(parts[2]),
                Double.parseDouble(parts[3]),
                parts.length > 4 ? Float.parseFloat(parts[4]) : 0f,
                parts.length > 5 ? Float.parseFloat(parts[5]) : 0f
        );
    }
}
