// File: src/main/java/com/example/parkourregion/RegionManager.java
package com.example.parkourregion;

import java.util.HashMap;
import java.util.Map;

public class RegionManager {

    private final Map<String, Region> regions = new HashMap<>();
    private final ParkourRegionPlugin plugin;

    public RegionManager(ParkourRegionPlugin plugin) {
        this.plugin = plugin;
    }

    public Map<String, Region> getRegions() {
        return regions;
    }

    public Region getRegion(String name) {
        return regions.get(name);
    }
}
