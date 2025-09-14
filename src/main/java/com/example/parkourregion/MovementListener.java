package com.example.parkourregion;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashMap;
import java.util.Map;

public class MovementListener implements Listener {

    private final ParkourRegion plugin;
    private final Map<Player, Location> lastSafeLocation = new HashMap<>();

    public MovementListener(ParkourRegion plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location loc = player.getLocation();
        Region region = plugin.getRegionManager().getRegion(loc);
        if (region == null) return;

        if (region.isBlacklistedBlock(loc.getBlock().getType())) {
            // Prevent standing
            event.setCancelled(true);
            return;
        }

        // Fall check
        double fallY = region.getFallY() != null ? region.getFallY() : -64;
        if (loc.getY() <= fallY) {
            Location safe = lastSafeLocation.getOrDefault(player, region.getStart());
            player.teleport(safe);
        }

        // Check checkpoints
        for (int i = 0; i < region.getCheckpoints().size(); i++) {
            if (loc.distance(region.getCheckpoints().get(i)) < 1.5) {
                lastSafeLocation.put(player, region.getCheckpoints().get(i));
                player.sendActionBar("Checkpoint " + i + " reached!");
                player.playSound(loc, Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
            }
        }

        // Finish
        if (region.getFinish() != null && loc.distance(region.getFinish()) < 1.5) {
            player.sendActionBar("You finished!");
            region.getFinishCommands().forEach(cmd -> plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), cmd.replace("{player}", player.getName())));
        }
    }
}
