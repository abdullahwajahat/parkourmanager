// File: src/main/java/com/example/parkourregion/InteractionListener.java
package com.example.parkourregion;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class InteractionListener implements Listener {

    private final ParkourRegionPlugin plugin;

    public InteractionListener(ParkourRegionPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        plugin.getRegionManager().getRegions().values().forEach(region -> {
            if (region.contains(event.getClickedBlock().getLocation())) {
                if (region.getBlacklist().contains(event.getClickedBlock().getType().name())) {
                    event.setCancelled(true);
                }
            }
        });
    }
}
