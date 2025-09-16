package com.example.parkourmanager.data;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class RegionManager {

    private final Map<String, Region> regions = new HashMap<>();
    private final File file;
    private final FileConfiguration config;

    public RegionManager(File dataFolder) {
        this.file = new File(dataFolder, "regions.yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.config = YamlConfiguration.loadConfiguration(file);
        loadRegions();
    }

    // ----- CRUD Operations -----

    public boolean createRegion(String name, Location pos1, Location pos2) {
        if (regions.containsKey(name.toLowerCase())) return false;
        Region region = new Region(name, pos1, pos2);
        regions.put(name.toLowerCase(), region);
        saveRegions();
        return true;
    }

    public boolean deleteRegion(String name) {
        if (!regions.containsKey(name.toLowerCase())) return false;
        regions.remove(name.toLowerCase());
        saveRegions();
        return true;
    }

    public Region getRegion(String name) {
        return regions.get(name.toLowerCase());
    }

    public Collection<Region> getAllRegions() {
        return regions.values();
    }

    // ----- Save / Load -----

    public void saveRegions() {
        config.getKeys(false).forEach(key -> config.set(key, null)); // clear old data

        for (Region r : regions.values()) {
            String path = r.getName();

            config.set(path + ".world", r.getWorldName());
            config.set(path + ".pos1", serializeLoc(r.getPos1()));
            config.set(path + ".pos2", serializeLoc(r.getPos2()));
            config.set(path + ".start", serializeLoc(r.getStart()));
            config.set(path + ".finish", serializeLoc(r.getFinish()));

            // Checkpoints
            Map<String, Object> cps = new HashMap<>();
            for (Map.Entry<Integer, Location> entry : r.getCheckpoints().entrySet()) {
                cps.put(entry.getKey().toString(), serializeLoc(entry.getValue()));
            }
            config.createSection(path + ".checkpoints", cps);

            // Finish Commands
            config.set(path + ".finishCommands", new ArrayList<>(r.getFinishCommands()));

            // Blacklist
            List<String> blacklist = new ArrayList<>();
            for (Material mat : r.getBlacklist()) {
                blacklist.add(mat.name());
            }
            config.set(path + ".blacklist", blacklist);

            config.set(path + ".fallY", r.getFallY());
            config.set(path + ".cooldown", r.getCooldown());
        }

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadRegions() {
        regions.clear();

        for (String name : config.getKeys(false)) {
            String world = config.getString(name + ".world");
            Location pos1 = deserializeLoc(config.getString(name + ".pos1"));
            Location pos2 = deserializeLoc(config.getString(name + ".pos2"));

            if (world == null || pos1 == null || pos2 == null) continue;

            Region r = new Region(name, pos1, pos2);

            r.setStart(deserializeLoc(config.getString(name + ".start")));
            r.setFinish(deserializeLoc(config.getString(name + ".finish")));

            // Checkpoints
            if (config.isConfigurationSection(name + ".checkpoints")) {
                for (String key : config.getConfigurationSection(name + ".checkpoints").getKeys(false)) {
                    int number = Integer.parseInt(key);
                    Location cpLoc = deserializeLoc(config.getString(name + ".checkpoints." + key));
                    if (cpLoc != null) {
                        r.addCheckpoint(number, cpLoc);
                    }
                }
            }

            // Finish Commands
            if (config.isList(name + ".finishCommands")) {
                r.getFinishCommands().addAll(config.getStringList(name + ".finishCommands"));
            }

            // Blacklist
            if (config.isList(name + ".blacklist")) {
                for (String matName : config.getStringList(name + ".blacklist")) {
                    try {
                        r.addBlacklist(Material.valueOf(matName));
                    } catch (IllegalArgumentException ignored) {}
                }
            }

            r.setFallY(config.getInt(name + ".fallY", Integer.MIN_VALUE));
            r.setCooldown(config.getString(name + ".cooldown", "0s"));

            regions.put(name.toLowerCase(), r);
        }
    }

    // ----- Utilities -----

    private String serializeLoc(Location loc) {
        if (loc == null) return null;
        return loc.getWorld().getName() + "," + loc.getX() + "," + loc.getY() + "," + loc.getZ()
                + "," + loc.getYaw() + "," + loc.getPitch();
    }

    private Location deserializeLoc(String s) {
        if (s == null) return null;
        try {
            String[] split = s.split(",");
            return new Location(
                    Bukkit.getWorld(split[0]),
                    Double.parseDouble(split[1]),
                    Double.parseDouble(split[2]),
                    Double.parseDouble(split[3]),
                    Float.parseFloat(split[4]),
                    Float.parseFloat(split[5])
            );
        } catch (Exception e) {
            return null;
        }
    }
}
