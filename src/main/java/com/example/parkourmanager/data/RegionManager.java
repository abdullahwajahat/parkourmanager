package com.example.parkourmanager.data;

import com.example.parkourmanager.ParkourManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.*;

public class RegionManager {

    private final ParkourManager plugin;
    private final Map<String, Region> regions;
    private final Map<UUID, String> playerRegionMap;

    public RegionManager(ParkourManager plugin) {
        this.plugin = plugin;
        this.regions = new HashMap<>();
        this.playerRegionMap = new HashMap<>();
        loadRegions();
    }

    public Region getRegion(String name) {
        return regions.get(name.toLowerCase());
    }

    public Collection<Region> getRegions() {
        return regions.values();
    }

    public void addRegion(Region region) {
        regions.put(region.getName().toLowerCase(), region);
    }

    public void removeRegion(String name) {
        regions.remove(name.toLowerCase());
    }

    public Region getRegionAt(Location loc) {
        for (Region region : regions.values()) {
            if (region.isInside(loc)) {
                return region;
            }
        }
        return null;
    }

    public void setPlayerRegion(Player player, Region region) {
        if (region == null) {
            playerRegionMap.remove(player.getUniqueId());
        } else {
            playerRegionMap.put(player.getUniqueId(), region.getName().toLowerCase());
        }
    }

    public Region getPlayerRegion(Player player) {
        String regionName = playerRegionMap.get(player.getUniqueId());
        return regionName == null ? null : regions.get(regionName);
    }

    public void clearPlayerData(Player player) {
        playerRegionMap.remove(player.getUniqueId());
    }

    public void saveRegions() {
        FileConfiguration config = plugin.getConfig();
        config.set("regions", null);

        for (Region region : regions.values()) {
            String path = "regions." + region.getName();

            if (region.getPos1() != null) {
                config.set(path + ".pos1", serializeLocation(region.getPos1()));
            }
            if (region.getPos2() != null) {
                config.set(path + ".pos2", serializeLocation(region.getPos2()));
            }
            if (region.getStart() != null) {
                config.set(path + ".start", serializeLocation(region.getStart()));
            }
            if (region.getFinish() != null) {
                config.set(path + ".finish", serializeLocation(region.getFinish()));
            }

            // Checkpoints
            Map<String, Object> cps = new HashMap<>();
            for (Map.Entry<Integer, Location> entry : region.getCheckpoints().entrySet()) {
                cps.put(String.valueOf(entry.getKey()), serializeLocation(entry.getValue()));
            }
            config.set(path + ".checkpoints", cps);

            // Finish commands
            config.set(path + ".finishCommands", region.getFinishCommands());

            // Blacklist blocks
            List<String> blacklist = new ArrayList<>();
            for (Material m : region.getBlacklistBlocks()) {
                blacklist.add(m.name());
            }
            config.set(path + ".blacklistBlocks", blacklist);

            config.set(path + ".fallY", region.getFallY());
            config.set(path + ".cooldown", region.getCooldownMillis());
        }

        plugin.saveConfig();
    }

    public void loadRegions() {
        FileConfiguration config = plugin.getConfig();
        ConfigurationSection section = config.getConfigurationSection("regions");
        if (section == null) return;

        for (String name : section.getKeys(false)) {
            ConfigurationSection rs = section.getConfigurationSection(name);
            if (rs == null) continue;

            Region region = new Region(name);

            if (rs.contains("pos1")) region.setPos1(deserializeLocation(rs.getConfigurationSection("pos1")));
            if (rs.contains("pos2")) region.setPos2(deserializeLocation(rs.getConfigurationSection("pos2")));
            if (rs.contains("start")) region.setStart(deserializeLocation(rs.getConfigurationSection("start")));
            if (rs.contains("finish")) region.setFinish(deserializeLocation(rs.getConfigurationSection("finish")));

            // Checkpoints
            ConfigurationSection cps = rs.getConfigurationSection("checkpoints");
            if (cps != null) {
                for (String key : cps.getKeys(false)) {
                    int num = Integer.parseInt(key);
                    region.setCheckpoint(num, deserializeLocation(cps.getConfigurationSection(key)));
                }
            }

            // Finish commands
            List<String> cmds = rs.getStringList("finishCommands");
            region.setFinishCommands(cmds);

            // Blacklist blocks
            List<String> bl = rs.getStringList("blacklistBlocks");
            List<Material> blMats = new ArrayList<>();
            for (String s : bl) {
                try {
                    blMats.add(Material.valueOf(s));
                } catch (IllegalArgumentException ignored) {}
            }
            region.setBlacklistBlocks(blMats);

            region.setFallY(rs.getInt("fallY", Integer.MIN_VALUE));
            region.setCooldownMillis(rs.getLong("cooldown", 0));

            addRegion(region);
        }
    }

    private Map<String, Object> serializeLocation(Location loc) {
        Map<String, Object> map = new HashMap<>();
        map.put("world", loc.getWorld().getName());
        map.put("x", loc.getX());
        map.put("y", loc.getY());
        map.put("z", loc.getZ());
        map.put("yaw", loc.getYaw());
        map.put("pitch", loc.getPitch());
        return map;
    }

    private Location deserializeLocation(ConfigurationSection section) {
        if (section == null) return null;
        return new Location(
                plugin.getServer().getWorld(section.getString("world")),
                section.getDouble("x"),
                section.getDouble("y"),
                section.getDouble("z"),
                (float) section.getDouble("yaw"),
                (float) section.getDouble("pitch")
        );
    }
}
