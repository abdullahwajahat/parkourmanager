// File: src/main/java/com/example/parkourregion/ParkourCommand.java
package com.example.parkourregion;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ParkourCommand implements CommandExecutor {

    private final ParkourRegionPlugin plugin;

    public ParkourCommand(ParkourRegionPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) return true;

        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "Usage: /por <region> <setstart|setend>");
            return true;
        }

        String regionName = args[0];
        String action = args[1];

        if (action.equalsIgnoreCase("setstart")) {
            Region r = plugin.getRegionManager().getRegions().get(regionName);
            if (r == null) r = new Region(player.getLocation(), player.getLocation(), plugin.getConfig().getStringList("blacklist-default"));
            plugin.getRegionManager().addRegion(regionName, new Region(player.getLocation(), r.getEnd(), r.getBlacklist()));
            player.sendMessage(ChatColor.GREEN + "Set start for region " + regionName);
        } else if (action.equalsIgnoreCase("setend")) {
            Region r = plugin.getRegionManager().getRegions().get(regionName);
            if (r == null) r = new Region(player.getLocation(), player.getLocation(), plugin.getConfig().getStringList("blacklist-default"));
            plugin.getRegionManager().addRegion(regionName, new Region(r.getStart(), player.getLocation(), r.getBlacklist()));
            player.sendMessage(ChatColor.GREEN + "Set end for region " + regionName);
        } else {
            player.sendMessage(ChatColor.RED + "Unknown action! Use setstart or setend");
        }

        return true;
    }
}
