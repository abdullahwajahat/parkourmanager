package com.example.parkourmanager.listeners;

import com.example.parkourmanager.data.Region;
import com.example.parkourmanager.data.RegionManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    private final RegionManager regionManager;

    public PlayerListener(RegionManager regionManager) {
        this.regionManager = regionManager;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        // Only handle if moved between blocks
        if (event.getFrom().getBlockX() == event.getTo().getBlockX()
                && event.getFrom().getBlockY() == event.getTo().getBlockY()
                && event.getFrom().getBlockZ() == event.getTo().getBlockZ()) {
            return;
        }

        Region regionAtTo = regionManager.getRegionAt(event.getTo());

        if (regionAtTo != null) {
            // Check blacklist
            if (regionAtTo.getBlacklist().contains(event.getTo().getBlock().getType())) {
                player.sendMessage("§cYou cannot step on this block in parkour region " + regionAtTo.getName() + "!");
                event.setCancelled(true);
                return;
            }

            // Check fall Y
            if (regionAtTo.getFallY() > 0 && player.getLocation().getY() < regionAtTo.getFallY()) {
                player.teleport(regionAtTo.getPos1()); // fallback teleport to pos1
                player.sendMessage("§cYou fell too low in region " + regionAtTo.getName());
                return;
            }

            // Check finish line
            if (regionAtTo.getFinishRegion() != null
                    && regionAtTo.getFinishRegion().contains(event.getTo().toVector())) {

                // Run finish commands
                for (String cmd : regionAtTo.getFinishCommands()) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replace("%player%", player.getName()));
                }

                player.sendMessage("§aYou completed parkour in region " + regionAtTo.getName() + "!");
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Region region = regionManager.getRegionAt(event.getBlock().getLocation());

        if (region != null && region.getBlacklist().contains(event.getBlock().getType())) {
            event.setCancelled(true);
            player.sendMessage("§cYou cannot break blacklisted blocks in region " + region.getName());
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        // Cleanup if needed (like checkpoints or cooldowns per player)
        Player player = event.getPlayer();
        regionManager.clearPlayerData(player);
    }
}
