package com.example.parkourregion;


import org.bukkit.Location;


import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;


public class PlayerDataManager {
public static class RunData {
public String regionName = null;
public boolean started = false; // whether player stepped on start
public int lastCheckpointIndex = -1; // -1 = none
public long lastFinishTime = 0; // cooldown tracking
}


private final Map<UUID, RunData> data = new ConcurrentHashMap<>();


public RunData get(UUID uuid) { return data.computeIfAbsent(uuid, k -> new RunData()); }
public void remove(UUID uuid) { data.remove(uuid); }
}
