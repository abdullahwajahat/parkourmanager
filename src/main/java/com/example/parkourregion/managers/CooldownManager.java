package com.example.parkourregion.managers;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CooldownManager {

    private final Map<String, Map<UUID, Long>> cooldowns = new HashMap<>();

    public boolean isOnCooldown(String region, UUID player) {
        if (!cooldowns.containsKey(region)) return false;
        return cooldowns.get(region).getOrDefault(player, 0L) > System.currentTimeMillis();
    }

    public long getRemaining(String region, UUID player) {
        return cooldowns.getOrDefault(region, Map.of()).getOrDefault(player, 0L) - System.currentTimeMillis();
    }

    public void setCooldown(String region, UUID player, long millis) {
        cooldowns.computeIfAbsent(region, k -> new HashMap<>()).put(player, System.currentTimeMillis() + millis);
    }
}
