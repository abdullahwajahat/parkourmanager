package com.example.parkourregion.managers;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import com.example.parkourregion.ParkourRegion;
import java.util.HashMap;
import java.util.Map;

public class RegionManager {

    private ParkourRegion plugin;
    private Map<String, Region> regions = new HashMap<>();

    public RegionManager(ParkourRegion plugin) {
        this.plugin = plugin;
        loadRegions();
    }

    public void addRegion(Region region) {
        regions.put(region.getName(), region);
    }

    public Region getRegion(String name) {
        return regions.get(name);
    }

    public Map<String, Region> getRegions() {
        return regions;
    }

    public void loadRegions() {
        FileConfiguration config = plugin.getConfig();
        if (!config.isConfigurationSection("regions")) return;

        for (String regionName : config.getConfigurationSection("regions").getKeys(false)) {
            Region region = new Region(regionName);

            if (config.contains("regions." + regionName + ".start"))
                region.setStart(deserializeLocation(config.getConfigurationSection("regions." + regionName + ".start")));

            if (config.contains("regions." + regionName + ".end"))
                region.setEnd(deserializeLocation(config.getConfigurationSection("regions." + regionName + ".end")));

            Map<Integer, Location> checkpoints = new HashMap<>();
            if (config.contains("regions." + regionName + ".checkpoints")) {
                for (String key : config.getConfigurationSection("regions." + regionName + ".checkpoints").getKeys(false)) {
                    Location loc = deserializeLocation(config.getString("regions." + regionName + ".checkpoints." + key));
                    checkpoints.put(Integer.parseInt(key), loc);
                }
            }
            region.setCheckpoints(checkpoints);
            regions.put(regionName, region);
        }
    }

    public void saveRegions() {
        FileConfiguration config = plugin.getConfig();

        for (Region region : regions.values()) {
            if (region.getStart() != null)
                config.set("regions." + region.getName() + ".start", serializeLocation(region.getStart()));
            if (region.getEnd() != null)
                config.set("regions." + region.getName() + ".end", serializeLocation(region.getEnd()));

            Map<Integer, String> checkpoints = new HashMap<>();
            for (Map.Entry<Integer, Location> entry : region.getCheckpoints().entrySet()) {
                checkpoints.put(entry.getKey(), serializeLocation(entry.getValue()));
            }
            config.set("regions." + region.getName() + ".checkpoints", checkpoints);
        }

        plugin.saveConfig();
    }

    public String serializeLocation(Location loc) {
        return loc.getWorld().getName() + "," + loc.getX() + "," + loc.getY() + "," + loc.getZ();
    }

    public Location deserializeLocation(String str) {
        String[] parts = str.split(",");
        return new Location(Bukkit.getWorld(parts[0]),
                Double.parseDouble(parts[1]),
                Double.parseDouble(parts[2]),
                Double.parseDouble(parts[3]));
    }

    private Location deserializeLocation(org.bukkit.configuration.ConfigurationSection section) {
        return new Location(Bukkit.getWorld(section.getString("world")),
                section.getDouble("x"),
                section.getDouble("y"),
                section.getDouble("z"));
    }
}
