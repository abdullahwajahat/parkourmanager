package com.example.parkourregion;

import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.entity.Player;
import org.bukkit.Material;

public class MovementListener implements Listener {

    private final RegionManager regionManager;

    public MovementListener(RegionManager regionManager) {
        this.regionManager = regionManager;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        for (Region region : regionManager.getRegions().values()) {
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
