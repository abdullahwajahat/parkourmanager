package com.example.parkourregion;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MovementListener implements Listener {

    private final RegionManager regionManager;
    private final Map<UUID, Map<String, Integer>> playerCheckpoints = new HashMap<>();

    public MovementListener(RegionManager regionManager) {
        this.regionManager = regionManager;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location loc = player.getLocation();
        UUID uuid = player.getUniqueId();

        for (Region region : regionManager.getRegions().values()) {

            if (!region.isInside(loc)) continue; // only act if player is inside

            // Initialize player's checkpoint map
            playerCheckpoints.putIfAbsent(uuid, new HashMap<>());
            Map<String, Integer> checkpointsMap = playerCheckpoints.get(uuid);
            checkpointsMap.putIfAbsent(region.getName(), -1); // -1 = no checkpoint yet

            // Check fall
            if (loc.getY() <= region.getFallY()) {
                Location start = region.getStart();
                if (start != null) {
                    player.teleport(start);
                    player.sendMessage("§cYou fell! Teleported to start.");
                    checkpointsMap.put(region.getName(), -1); // reset checkpoint
                }
                return;
            }

            // Check checkpoints
            List<Location> checkpoints = region.getCheckpoints();
            for (int i = 0; i < checkpoints.size(); i++) {
                Location cp = checkpoints.get(i);
                if (cp.getWorld().equals(loc.getWorld()) &&
                    loc.getBlockX() == cp.getBlockX() &&
                    loc.getBlockY() == cp.getBlockY() &&
                    loc.getBlockZ() == cp.getBlockZ()) {

                    int lastCheckpoint = checkpointsMap.get(region.getName());
                    if (i > lastCheckpoint) { // only update if forward
                        checkpointsMap.put(region.getName(), i);
                        player.sendMessage("§aCheckpoint " + (i + 1) + " reached!");
                    }
                }
            }

            // Check finish
            Location finish = region.getFinish();
            if (finish != null &&
                loc.getWorld().equals(finish.getWorld()) &&
                loc.getBlockX() == finish.getBlockX() &&
                loc.getBlockY() == finish.getBlockY() &&
                loc.getBlockZ() == finish.getBlockZ()) {

                player.sendMessage("§6You finished the parkour!");

                // Run finish commands
                List<String> cmds = region.getFinishCommands();
                if (cmds != null) {
                    for (String cmd : cmds) {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replace("%player%", player.getName()));
                    }
                }

                // Teleport back to start
                if (region.getStart() != null) {
                    player.teleport(region.getStart());
                }

                // Reset checkpoint
                checkpointsMap.put(region.getName(), -1);
            }

            // Check blacklisted blocks
            Material block = loc.getBlock().getType();
            if (region.getBlacklistedBlocks().contains(block.name())) {
                if (region.getStart() != null) {
                    player.teleport(region.getStart());
                    player.sendMessage("§cYou touched a forbidden block! Teleported to start.");
                    checkpointsMap.put(region.getName(), -1); // reset checkpoint
                }
            }
        }
    }
}
