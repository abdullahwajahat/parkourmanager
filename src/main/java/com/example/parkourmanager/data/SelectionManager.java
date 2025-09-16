package com.example.parkourmanager.data;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SelectionManager {

    private final Map<UUID, Location> pos1Map = new HashMap<>();
    private final Map<UUID, Location> pos2Map = new HashMap<>();

    /**
     * Set position 1 for a player
     */
    public void setPos1(Player player, Location loc) {
        pos1Map.put(player.getUniqueId(), loc.clone());
    }

    /**
     * Set position 2 for a player
     */
    public void setPos2(Player player, Location loc) {
        pos2Map.put(player.getUniqueId(), loc.clone());
    }

    /**
     * Get position 1 for a player
     */
    public Location getPos1(Player player) {
        return pos1Map.get(player.getUniqueId());
    }

    /**
     * Get position 2 for a player
     */
    public Location getPos2(Player player) {
        return pos2Map.get(player.getUniqueId());
    }

    /**
     * Check if player has made a full selection
     */
    public boolean hasSelection(Player player) {
        return getPos1(player) != null && getPos2(player) != null;
    }

    /**
     * Clear a player's selection (used when finished creating a region)
     */
    public void clearSelection(Player player) {
        pos1Map.remove(player.getUniqueId());
        pos2Map.remove(player.getUniqueId());
    }
}
