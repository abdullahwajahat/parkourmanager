package com.example.parkourregion;


import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;


public class InteractionListener implements Listener {
private final RegionManager regionManager;
private final PlayerDataManager pdm;


public InteractionListener(ParkourRegionPlugin plugin, RegionManager rm, PlayerDataManager pdm) { this.regionManager = rm; this.pdm = pdm; }


@EventHandler
public void onInteract(PlayerInteractEvent e) {
Block b = e.getClickedBlock();
if (b == null) return;
Location loc = b.getLocation();
Player p = e.getPlayer();
// If player tries to stand on a blacklisted block via interact (e.g., place block), prevent by cancelling
for (Region r : regionManager.getRegions()) {
if (!r.contains(loc)) continue;
if (r.getBlacklist().contains(b.getType().name())) {
e.setCancelled(true);
return;
}
}
}
}
