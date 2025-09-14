package com.example.parkourregion;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class InteractionListener implements Listener {

    private final ParkourRegionPlugin plugin;

    public InteractionListener(ParkourRegionPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Region region = plugin.getRegionManager().getRegionAt(event.getBlock().getLocation());
        if (region != null && region.getBlacklistedBlocks().contains(event.getBlock().getType())) {
            event.setCancelled(true);
            event.getPlayer().sendMessage("§cYou cannot break this block in a parkour region!");
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Region region = plugin.getRegionManager().getRegionAt(event.getBlock().getLocation());
        if (region != null && region.getBlacklistedBlocks().contains(event.getBlock().getType())) {
            event.setCancelled(true);
            event.getPlayer().sendMessage("§cYou cannot place this block in a parkour region!");
        }
    }
}
