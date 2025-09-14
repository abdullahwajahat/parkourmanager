package com.example.parkourregion.objects;

import java.util.*;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class ParkourRegionObject {

    private String name;
    private Location pos1;
    private Location pos2;
    private Location start;
    private Location finish;
    private Map<Integer, Location> checkpoints = new HashMap<>();
    private Set<String> blacklistBlocks = new HashSet<>();
    private boolean locked = false;
    private Integer yAxis = null;
    private Location finishTpLocation;

    public ParkourRegionObject(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Location getPos1() {
        return pos1;
    }

    public void setPos1(Location pos1) {
        this.pos1 = pos1;
    }

    public Location getPos2() {
        return pos2;
    }

    public void setPos2(Location pos2) {
        this.pos2 = pos2;
    }

    public Location getStart() {
        return start;
    }

    public void setStart(Location start) {
        this.start = start;
    }

    public Location getFinish() {
        return finish;
    }

    public void setFinish(Location finish) {
        this.finish = finish;
    }

    public Map<Integer, Location> getCheckpoints() {
        return checkpoints;
    }

    public void setCheckpoint(int number, Location loc) {
        checkpoints.put(number, loc);
    }

    public void removeCheckpoint(int number) {
        checkpoints.remove(number);
    }

    public Set<String> getBlacklistBlocks() {
        return blacklistBlocks;
    }

    public void addBlacklistBlock(String block) {
        blacklistBlocks.add(block);
    }

    public void removeBlacklistBlock(String block) {
        blacklistBlocks.remove(block);
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public Integer getYAxis() {
        return yAxis;
    }

    public void setYAxis(Integer yAxis) {
        this.yAxis = yAxis;
    }

    public Location getFinishTpLocation() {
        return finishTpLocation;
    }

    public void setFinishTpLocation(Location finishTpLocation) {
        this.finishTpLocation = finishTpLocation;
    }
}

