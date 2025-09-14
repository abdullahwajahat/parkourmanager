package com.example.parkourregion.managers;

import com.example.parkourregion.ParkourRegion;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CooldownManager {

    private final Map<UUID, Map<String, Long>> cooldowns = new HashMap<>();

    public boolean isOnCooldown(Player player, String region, long cooldownMillis) {
        Map<String, Long> playerMap = cooldowns.get(player.getUniqueId());
        if (playerMap == null) return false;
        if (!playerMap.containsKey(region)) return false;

        long time = playerMap.get(region);
        return System.currentTimeMillis() < time + cooldownMillis;
    }

    public void setCooldown(Player player, String region) {
        cooldowns.putIfAbsent(player.getUniqueId(), new HashMap<>());
        cooldowns.get(player.getUniqueId()).put(region, System.currentTimeMillis());
    }

    public long getTimeLeft(Player player, String region, long cooldownMillis) {
        if (!isOnCooldown(player, region, cooldownMillis)) return 0;
        return (cooldowns.get(player.getUniqueId()).get(region) + cooldownMillis) - System.currentTimeMillis();
    }
}

