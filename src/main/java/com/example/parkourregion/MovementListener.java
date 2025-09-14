// File: src/main/java/com/example/parkourregion/MovementListener.java
package com.example.parkourregion;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class MovementListener implements Listener {

    private final ParkourRegionPlugin plugin;

    public MovementListener(ParkourRegionPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        // Loop through all regions via plugin's RegionManager
        for (Region region : plugin.getRegionManager().getRegions().values()) {
            if (region.contains(player.getLocation())) {
                for (String blk : region.getBlacklist()) {
                    if (player.getLocation().getBlock().getType().name().equalsIgnoreCase(blk)) {
                        player.sendMessage("§cYou stepped on a blacklisted block!");
                    }
                }
            }
        }
    }
}
