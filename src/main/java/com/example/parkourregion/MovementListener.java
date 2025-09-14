package com.example.parkourregion;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.Material;

public class MovementListener implements Listener {

    private final ParkourRegionPlugin plugin;

    public MovementListener(ParkourRegionPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Region region = plugin.getRegionManager().getRegionAt(event.getPlayer().getLocation());
        if (region != null && region.getBlacklistedBlocks() != null) {
            if (region.getBlacklistedBlocks().contains(event.getPlayer().getLocation().getBlock().getType())) {
                event.getPlayer().sendMessage("§cYou stepped on a blacklisted block!");
            }
        }
    }
}
