package com.example.parkourregion;


import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;


public class RegionBoundaryListener implements Listener {
private final RegionManager rm;
private final PlayerDataManager pdm;
public RegionBoundaryListener(RegionManager rm, PlayerDataManager pdm) { this.rm = rm; this.pdm = pdm; }


@EventHandler
public void onQuit(PlayerQuitEvent e) { pdm.remove(e.getPlayer().getUniqueId()); }
}
