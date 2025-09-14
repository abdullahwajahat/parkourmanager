package com.example.parkourregion;

import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockBreakEvent;

public class InteractionListener implements Listener {

    private final RegionManager regionManager;

    public InteractionListener(RegionManager regionManager) {
        this.regionManager = regionManager;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        for (Region region : regionManager.getRegions().values()) {
            if (region.contains(event.getBlock().getLocation()) &&
                region.getBlacklist().contains(event.getBlock().getType().name())) {
                event.setCancelled(true);
                event.getPlayer().sendMessage("§cYou cannot place this block here!");
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        for (Region region : regionManager.getRegions().values()) {
            if (region.contains(event.getBlock().getLocation()) &&
                region.getBlacklist().contains(event.getBlock().getType().name())) {
                event.setCancelled(true);
                event.getPlayer().sendMessage("§cYou cannot break this block here!");
            }
        }
    }
}
