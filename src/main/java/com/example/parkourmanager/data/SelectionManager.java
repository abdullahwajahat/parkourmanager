package com.example.parkourmanager.data;

import org.bukkit.entity.Player;
import org.bukkit.Location;
import java.util.HashMap;
import java.util.Map;

public class SelectionManager {
    private final Map<Player, Location> pos1 = new HashMap<>();
    private final Map<Player, Location> pos2 = new HashMap<>();

    public void setPos1(Player player, Location loc) {
        pos1.put(player, loc);
    }

    public void setPos2(Player player, Location loc) {
        pos2.put(player, loc);
    }

    public Location getPos1(Player player) {
        return pos1.get(player);
    }

    public Location getPos2(Player player) {
        return pos2.get(player);
    }

    public boolean hasSelection(Player player) {
        return pos1.containsKey(player) && pos2.containsKey(player);
    }
}
