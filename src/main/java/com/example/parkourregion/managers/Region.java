package com.example.parkourregion.managers;

import org.bukkit.Location;
import org.bukkit.Material;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Region {

    private String name;
    private Location pos1, pos2;
    private Location start;
    private Map<Integer, Location> checkpoints = new HashMap<>();
    private Location finish;
    private Set<Material> blacklist = new HashSet<>();
    private Integer yLimit;
    private Location finishTeleport;

    public Region(String name) { this.name = name; }

    public String getName() { return name; }
    public Location getPos1() { return pos1; }
    public void setPos1(Location pos1) { this.pos1 = pos1; }
    public Location getPos2() { return pos2; }
    public void setPos2(Location pos2) { this.pos2 = pos2; }

    public Location getStart() { return start; }
    public void setStart(Location start) { this.start = start; }

    public Map<Integer, Location> getCheckpoints() { return checkpoints; }

    public Location getFinish() { return finish; }
    public void setFinish(Location finish) { this.finish = finish; }

    public Set<Material> getBlacklist() { return blacklist; }

    public Integer getYLimit() { return yLimit; }
    public void setYLimit(Integer yLimit) { this.yLimit = yLimit; }

    public Location getFinishTeleport() { return finishTeleport; }
    public void setFinishTeleport(Location finishTeleport) { this.finishTeleport = finishTeleport; }
}
