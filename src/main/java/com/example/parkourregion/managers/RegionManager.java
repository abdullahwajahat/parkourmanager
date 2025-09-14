package com.example.parkourregion.managers;

import com.example.parkourregion.ParkourRegion;
import com.example.parkourregion.objects.ParkourRegionObject;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;

public class RegionManager {

    private final ParkourRegion plugin;
    private final Map<String, ParkourRegionObject> regions = new HashMap<>();

    public RegionManager(ParkourRegion plugin) {
        this.plugin = plugin;
        loadRegions();
    }

    public void addRegion(ParkourRegionObject region) {
        regions.put(region.getName().toLowerCase(), region);
        saveRegions();
    }

    public void removeRegion(String name) {
        regions.remove(name.toLowerCase());
        saveRegions();
    }

    public ParkourRegionObject getRegion(String name) {
        return regions.get(name.toLowerCase());
    }

    public Collection<ParkourRegionObject> getRegions() {
        return regions.values();
    }

    public void saveRegions() {
        FileConfiguration config = plugin.getConfig();
        config.set("regions", null); // reset
        for (ParkourRegionObject region : regions.values()) {
            String path = "regions." + region.getName();
            if (region.getPos1() != null) config.set(path + ".pos1", serializeLocation(region.getPos1()));
            if (region.getPos2() != null) config.set(path + ".pos2", serializeLocation(region.getPos2()));
            if (region.getStart() != null) config.set(path + ".start", serializeLocation(region.getStart()));
            if (region.getFinish() != null) config.set(path + ".finish", serializeLocation(region.getFinish()));
            if (region.getFinishTpLocation() != null) config.set(path + ".finishTp", serializeLocation(region.getFinishTpLocation()));
            config.set(path + ".locked", region.isLocked());
            if (region.getYAxis() != null) config.set(path + ".yAxis", region.getYAxis());
            config.set(path + ".blacklist", new ArrayList<>(region.getBlacklistBlocks()));
            Map<Integer, String> checkpoints = new HashMap<>();
            for (Map.Entry<Integer, Location> entry : region.getCheckpoints().entrySet()) {
                checkpoints.put(entry.getKey(), serializeLocation(entry.getValue()));
            }
            config.set(path + ".checkpoints", checkpoints);
        }
        plugin.saveConfig();
    }

    @SuppressWarnings("unchecked")
    public void loadRegions() {
        FileConfiguration config = plugin.getConfig();
        if (!config.contains("regions")) return;

        for (String key : config.getConfigurationSection("regions").getKeys(false)) {
            ParkourRegionObject region = new ParkourRegionObject(key);
            String path = "regions." + key;

            if (config.contains(path + ".pos1")) region.setPos1(deserializeLocation(config.getString(path + ".pos1")));
            if (config.contains(path + ".pos2")) region.setPos2(deserializeLocation(config.getString(path + ".pos2")));
            if (config.contains(path + ".start")) region.setStart(deserializeLocation(config.getString(path + ".start")));
            if (config.contains(path + ".finish")) region.setFinish(deserializeLocation(config.getString(path + ".finish")));
            if (config.contains(path + ".finishTp")) region.setFinishTpLocation(deserializeLocation(config.getString(path + ".finishTp")));
            region.setLocked(config.getBoolean(path + ".locked", false));
            if (config.contains(path + ".yAxis")) region.setYAxis(config.getInt(path + ".yAxis"));
            if (config.contains(path + ".blacklist")) region.getBlacklistBlocks().addAll((List<String>) config.getList(path + ".blacklist"));
            if (config.contains(path + ".checkpoints")) {
                Map<String, String> cps = (Map<String, String>) config.getConfigurationSection(path + ".checkpoints").getValues(false);
                for (Map.Entry<String, String> entry : cps.entrySet()) {
                    region.setCheckpoint(Integer.parseInt(entry.getKey()), deserializeLocation(entry.getValue()));
                }
            }

            regions.put(key.toLowerCase(), region);
        }
    }

    private String serializeLocation(Location loc) {
        return loc.getWorld().getName() + "," + loc.getX() + "," + loc.getY() + "," + loc.getZ() + "," + loc.getYaw() + "," + loc.getPitch();
    }

    private Location deserializeLocation(String str) {
        String[] parts = str.split(",");
        if (parts.length < 6) return null;
        World world = Bukkit.getWorld(parts[0]);
        if (world == null) return null;
        double x = Double.parseDouble(parts[1]);
        double y = Double.parseDouble(parts[2]);
        double z = Double.parseDouble(parts[3]);
        float yaw = Float.parseFloat(parts[4]);
        float pitch = Float.parseFloat(parts[5]);
        return new Location(world, x, y, z, yaw, pitch);
    }
}

