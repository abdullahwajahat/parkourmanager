package com.example.parkourregion.managers;

import com.example.parkourregion.ParkourRegion;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;

public class RegionManager {

    private final ParkourRegion plugin;
    private final Map<String, Region> regions = new HashMap<>();

    public RegionManager(ParkourRegion plugin) {
        this.plugin = plugin;
        loadRegions();
    }

    public void addRegion(Region region) { regions.put(region.getName(), region); }
    public Region getRegion(String name) { return regions.get(name); }
    public Map<String, Region> getRegions() { return regions; }

    public void loadRegions() {
        FileConfiguration config = plugin.getConfig();
        if (!config.isConfigurationSection("regions")) return;

        for (String name : config.getConfigurationSection("regions").getKeys(false)) {
            Region region = new Region(name);
            var section = config.getConfigurationSection("regions." + name);

            if (section.contains("pos1")) region.setPos1(deserializeLocation(section.getConfigurationSection("pos1")));
            if (section.contains("pos2")) region.setPos2(deserializeLocation(section.getConfigurationSection("pos2")));
            if (section.contains("start")) region.setStart(deserializeLocation(section.getConfigurationSection("start")));
            if (section.contains("finish")) region.setFinish(deserializeLocation(section.getConfigurationSection("finish")));
            if (section.contains("finishTeleport")) region.setFinishTeleport(deserializeLocation(section.getConfigurationSection("finishTeleport")));

            if (section.contains("checkpoints")) {
                for (String key : section.getConfigurationSection("checkpoints").getKeys(false)) {
                    region.getCheckpoints().put(Integer.parseInt(key), deserializeLocation(section.getString("checkpoints." + key)));
                }
            }

            if (section.contains("blacklist")) {
                for (String mat : section.getStringList("blacklist")) region.getBlacklist().add(Material.getMaterial(mat));
            }
            if (section.contains("ylimit")) region.setYLimit(section.getInt("ylimit"));

            regions.put(name, region);
        }
    }

    public void saveRegions() {
        FileConfiguration config = plugin.getConfig();
        config.set("regions", null);

        for (Region region : regions.values()) {
            String path = "regions." + region.getName();
            if (region.getPos1() != null) config.set(path + ".pos1", serializeLocationMap(region.getPos1()));
            if (region.getPos2() != null) config.set(path + ".pos2", serializeLocationMap(region.getPos2()));
            if (region.getStart() != null) config.set(path + ".start", serializeLocationMap(region.getStart()));
            if (region.getFinish() != null) config.set(path + ".finish", serializeLocationMap(region.getFinish()));
            if (region.getFinishTeleport() != null) config.set(path + ".finishTeleport", serializeLocationMap(region.getFinishTeleport()));

            Map<Integer, String> cps = new HashMap<>();
            for (var e : region.getCheckpoints().entrySet()) cps.put(e.getKey(), serializeLocation(e.getValue()));
            config.set(path + ".checkpoints", cps);

            config.set(path + ".blacklist", new ArrayList<>(region.getBlacklist().stream().map(Enum::name).toList()));
            if (region.getYLimit() != null) config.set(path + ".ylimit", region.getYLimit());
        }
        plugin.saveConfig();
    }

    private String serializeLocation(Location loc) {
        return loc.getWorld().getName() + "," + loc.getX() + "," + loc.getY() + "," + loc.getZ();
    }

    private Map<String, Object> serializeLocationMap(Location loc) {
        Map<String, Object> map = new HashMap<>();
        map.put("world", loc.getWorld().getName());
        map.put("x", loc.getX());
        map.put("y", loc.getY());
        map.put("z", loc.getZ());
        return map;
    }

    private Location deserializeLocation(String str) {
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
