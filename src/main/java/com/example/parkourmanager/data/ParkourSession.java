package com.example.parkourmanager.data;

import org.bukkit.Location;
import java.util.UUID;

public class ParkourSession {

    private final UUID playerId;
    private final String regionName;

    private int lastCheckpoint = -1; // -1 means none reached
    private long startTime;

    public ParkourSession(UUID playerId, String regionName) {
        this.playerId = playerId;
        this.regionName = regionName;
        this.startTime = System.currentTimeMillis();
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public String getRegionName() {
        return regionName;
    }

    public int getLastCheckpoint() {
        return lastCheckpoint;
    }

    public void setLastCheckpoint(int lastCheckpoint) {
        this.lastCheckpoint = lastCheckpoint;
    }

    public Location getLastCheckpointLocation(Region region) {
        if (lastCheckpoint == -1) {
            return region.getStart();
        }
        return region.getCheckpoint(lastCheckpoint);
    }

    public long getStartTime() {
        return startTime;
    }

    public void reset() {
        this.lastCheckpoint = -1;
        this.startTime = System.currentTimeMillis();
    }
}
