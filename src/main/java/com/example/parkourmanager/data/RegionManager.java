package com.example.parkourmanager.data;

import com.example.parkourmanager.ParkourManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
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

    public List<String> listRegions() {
        return new ArrayList<>(regions.keySet());
    }

    public void deleteRegion(String name) {
        regions.remove(name.toLowerCase());
        saveRegions();
    }

    // -------- Commands called from ParkourCommand --------

    public void createRegion(Player player, String name) {
        Region region = new Region(name);
        addRegion(region);
        saveRegions();
    }

    public void setStart(Player player, Location loc) {
        Region region = getRegionAt(loc);
        if (region != null) {
            region.setStart(loc);
            saveRegions();
        }
    }

    public void setFinish(Player player, Location loc) {
        Region region = getRegionAt(loc);
        if (region != null) {
            region.setFinish(loc);
            saveRegions();
        }
    }

    public void addCheckpoint(Player player, int number, Location loc) {
        Region region = getRegionAt(loc);
        if (region != null) {
            region.getCheckpoints().put(number, loc);
            saveRegions();
        }
    }

    public void editCheckpoint(Player player, int number, Location loc) {
        Region region = getRegionAt(loc);
        if (region != null) {
            region.getCheckpoints().put(number, loc);
            saveRegions();
        }
    }

    public void removeCheckpoint(String regionName, int number) {
        Region region = getRegion(regionName);
        if (region != null) {
            region.getCheckpoints().remove(number);
            saveRegions();
        }
    }

    public void addFinishCommand(String regionName, String cmd) {
        Region region = getRegion(regionName);
        if (region != null) {
            region.getFinishCommands().add(cmd);
            saveRegions();
        }
    }

    public void removeFinishCommand(String regionName, int index) {
        Region region = getRegion(regionName);
        if (region != null && index >= 0 && index < region.getFinishCommands().size()) {
            region.getFinishCommands().remove(index);
            saveRegions();
        }
    }

    public void setFinishCommand(String regionName, int index, String cmd) {
        Region region = getRegion(regionName);
        if (region != null && index >= 0 && index < region.getFinishCommands().size()) {
            region.getFinishCommands().set(index, cmd);
            saveRegions();
        }
    }

    public void setFallY(String regionName, double y) {
        Region region = getRegion(regionName);
        if (region != null) {
            region.setFallY(y);
            saveRegions();
        }
    }

    public void setCooldown(String regionName, String time) {
        Region region = getRegion(regionName);
        if (region != null) {
            try {
                int cooldown = Integer.parseInt(time);
                region.setCooldown(cooldown);
                saveRegions();
            } catch (NumberFormatException ignored) {}
        }
    }

    public void addBlacklist(String regionName, Material mat) {
        Region region = getRegion(regionName);
        if (region != null) {
            region.getBlacklist().add(mat.name());
            saveRegions();
        }
    }

    public void removeBlacklist(String regionName, Material mat) {
        Region region = getRegion(regionName);
        if (region != null) {
            region.getBlacklist().remove(mat.name());
            saveRegions();
        }
    }

    public Location getRegionMiddle(String regionName) {
        Region region = getRegion(regionName);
        if (region == null || region.getPos1() == null || region.getPos2() == null) return null;

        double midX = (region.getPos1().getX() + region.getPos2().getX()) / 2.0;
        double midY = (region.getPos1().getY() + region.getPos2().getY()) / 2.0;
        double midZ = (region.getPos1().getZ() + region.getPos2().getZ()) / 2.0;

        return new Location(region.getPos1().getWorld(), midX, midY, midZ);
    }

    public String getRegionAt(Location loc) {
        for (Region region : regions.values()) {
            if (region.isInside(loc)) {
                return region;
            }
        }
        return null;
    }

    public void showRegionOutline(String regionName, Player player, Particle particle) {
        Region region = getRegion(regionName);
        if (region == null || region.getPos1() == null || region.getPos2() == null) return;

        Location pos1 = region.getPos1();
        Location pos2 = region.getPos2();

        int minX = Math.min(pos1.getBlockX(), pos2.getBlockX());
        int maxX = Math.max(pos1.getBlockX(), pos2.getBlockX());
        int minY = Math.min(pos1.getBlockY(), pos2.getBlockY());
        int maxY = Math.max(pos1.getBlockY(), pos2.getBlockY());
        int minZ = Math.min(pos1.getBlockZ(), pos2.getBlockZ());
        int maxZ = Math.max(pos1.getBlockZ(), pos2.getBlockZ());

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    if (x == minX || x == maxX || y == minY || y == maxY || z == minZ || z == maxZ) {
                        player.spawnParticle(particle, new Location(pos1.getWorld(), x, y, z), 1);
                    }
                }
            }
        }
    }

    public String getRegionInfo(String regionName) {
        Region region = getRegion(regionName);
        if (region == null) return "Region not found.";

        return "Region: " + region.getName() +
                "\nStart: " + formatLoc(region.getStart()) +
                "\nFinish: " + formatLoc(region.getFinish()) +
                "\nCheckpoints: " + region.getCheckpoints().size() +
                "\nCooldown: " + region.getCooldown() +
                "\nFallY: " + region.getFallY() +
                "\nBlacklist: " + String.join(", ", region.getBlacklist()) +
                "\nFinishCommands: " + String.join(", ", region.getFinishCommands());
    }

    private String formatLoc(Location loc) {
        if (loc == null) return "none";
        return loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ();
    }

    // -------- Save / Load --------

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
            region.setCooldown(plugin.getConfig().getInt(path + ".cooldown", 0));

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
