package com.example.parkourregion;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.Location;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegionManager {

    private final ParkourRegionPlugin plugin;
    private final Map<String, Region> regions = new HashMap<>();
    private final File regionFile;
    private final YamlConfiguration regionConfig;

    public RegionManager(ParkourRegionPlugin plugin) {
        this.plugin = plugin;
        regionFile = new File(plugin.getDataFolder(), "regions.yml");

        if (!regionFile.exists()) {
            plugin.saveResource("regions.yml", false);
        }

        regionConfig = YamlConfiguration.loadConfiguration(regionFile);
        loadRegions();
    }

    public void loadRegions() {
        regions.clear();
        for (String key : regionConfig.getKeys(false)) {
            Location min = regionConfig.getLocation(key + ".min");
            Location max = regionConfig.getLocation(key + ".max");
            List<String> blacklist = regionConfig.getStringList(key + ".blacklist");
            int cooldown = regionConfig.getInt(key + ".cooldown", plugin.getConfig().getInt("cooldown-default"));
            regions.put(key, new Region(key, min, max, blacklist, cooldown));
        }
    }

    public void saveRegions() {
        for (Map.Entry<String, Region> entry : regions.entrySet()) {
            Region r = entry.getValue();
            regionConfig.set(entry.getKey() + ".min", r.getMin());
            regionConfig.set(entry.getKey() + ".max", r.getMax());
            regionConfig.set(entry.getKey() + ".blacklist", r.getBlacklist());
            regionConfig.set(entry.getKey() + ".cooldown", r.getCooldown());
        }
        try {
            regionConfig.save(regionFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<String, Region> getRegions() {
        return regions;
    }

    public void addRegion(Region region) {
        regions.put(region.getName(), region);
        saveRegions();
    }

    public void removeRegion(String name) {
        regions.remove(name);
        regionConfig.set(name, null);
        saveRegions();
    }
}
