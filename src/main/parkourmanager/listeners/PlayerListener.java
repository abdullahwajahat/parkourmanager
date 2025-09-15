package com.example.parkourmanager.listeners;

import com.example.parkourmanager.ParkourManager;
import com.example.parkourmanager.data.Region;
import com.example.parkourmanager.data.RegionManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.*;

public class PlayerListener implements Listener {

    private final ParkourManager plugin;
    private final RegionManager regionManager;

    // Track player progress
    private final Map<UUID, Region> currentRegion = new HashMap<>();
    private final Map<UUID, Integer> checkpointProgress = new HashMap<>();

    public PlayerListener(ParkourManager plugin) {
        this.plugin = plugin;
        this.regionManager = plugin.getRegionManager();
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        Location to = e.getTo();
        Location from = e.getFrom();

        if (to == null) return;

        Region regionAtTo = getRegionAt(to);
        Region regionAtFrom = getRegionAt(from);

        // Entering region
        if (regionAtTo != null && regionAtFrom == null) {
            currentRegion.put(player.getUniqueId(), regionAtTo);
            checkpointProgress.put(player.getUniqueId(), 0);
        }

        // Leaving region
        if (regionAtTo == null && regionAtFrom != null) {
            currentRegion.remove(player.getUniqueId());
            checkpointProgress.remove(player.getUniqueId());
        }

        // Check blacklist blocks
        if (regionAtTo != null) {
            Material block = to.getBlock().getType();
            if (regionAtTo.getBlacklist().contains(block.name())) {
                // Teleport player back to previous safe location
                Location tp = getLastCheckpoint(player, regionAtTo);
                if (tp != null) {
                    player.teleport(tp);
                } else if (regionAtTo.getStart() != null) {
                    player.teleport(regionAtTo.getStart());
                }
            }
        }

        // Check fallY
        if (regionAtTo != null && regionAtTo.getFallY() != Double.MIN_VALUE) {
            if (to.getY() < regionAtTo.getFallY()) {
                Location tp = getLastCheckpoint(player, regionAtTo);
                if (tp != null) {
                    player.teleport(tp);
                } else if (regionAtTo.getStart() != null) {
                    player.teleport(regionAtTo.getStart());
                }
            }
        }

        // Check checkpoints
        if (regionAtTo != null) {
            for (Map.Entry<Integer, Location> entry : regionAtTo.getCheckpoints().entrySet()) {
                if (isNear(to, entry.getValue(), 1.5)) {
                    checkpointProgress.put(player.getUniqueId(), entry.getKey());
                }
            }
        }

        // Check finish
        if (regionAtTo != null && regionAtTo.getFinish() != null) {
            if (isNear(to, regionAtTo.getFinish(), 1.5)) {
                // Execute finish commands
                for (String cmd : regionAtTo.getFinishCommands()) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replace("%player%", player.getName()));
                }
                // Apply cooldown (basic placeholder)
                player.sendMessage("§aYou finished parkour " + regionAtTo.getName() +
                        " (cooldown " + regionAtTo.getCooldown() + ")");
                // Reset progress
                checkpointProgress.remove(player.getUniqueId());
                currentRegion.remove(player.getUniqueId());
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        UUID id = e.getPlayer().getUniqueId();
        currentRegion.remove(id);
        checkpointProgress.remove(id);
    }

    private Region getRegionAt(Location loc) {
        if (loc == null) return null;
        for (Region r : regionManager.getRegions().values()) {
            if (r.isInside(loc)) return r;
        }
        return null;
    }

    private Location getLastCheckpoint(Player player, Region region) {
        int prog = checkpointProgress.getOrDefault(player.getUniqueId(), 0);
        if (prog > 0 && region.getCheckpoints().containsKey(prog)) {
            return region.getCheckpoints().get(prog);
        }
        return null;
    }

    private boolean isNear(Location a, Location b, double radius) {
        if (a.getWorld() == null || b.getWorld() == null) return false;
        if (!a.getWorld().equals(b.getWorld())) return false;
        return a.distance(b) <= radius;
    }
}
