package com.example.parkourregion.managers;

import org.bukkit.Location;
import java.util.HashMap;
import java.util.Map;

public class Region {

    private String name;
    private Location start;
    private Location end;
    private Map<Integer, Location> checkpoints = new HashMap<>();

    public Region(String name) {
        this.name = name;
    }

    public String getName() { return name; }
    public Location getStart() { return start; }
    public void setStart(Location start) { this.start = start; }
    public Location getEnd() { return end; }
    public void setEnd(Location end) { this.end = end; }
    public Map<Integer, Location> getCheckpoints() { return checkpoints; }
    public void setCheckpoints(Map<Integer, Location> checkpoints) { this.checkpoints = checkpoints; }
}
