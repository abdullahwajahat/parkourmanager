package com.example.parkourregion;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class ParkourCommand implements CommandExecutor {

    private final RegionManager regionManager;

    public ParkourCommand(RegionManager regionManager) {
        this.regionManager = regionManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) return false;

        if (args.length == 0) {
            player.sendMessage("§6ParkourRegion Commands:");
            player.sendMessage("§e/por create <name>");
            player.sendMessage("§e/por delete <name>");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "create" -> {
                if (args.length < 2) {
                    player.sendMessage("§cUsage: /por create <name>");
                    return true;
                }
                String name = args[1];
                Location min = player.getLocation();
                Location max = player.getLocation(); // For simplicity, min=max. You can expand later.
                List<String> blacklist = new ArrayList<>(ParkourRegionPlugin.getInstance().getConfig().getStringList("blacklist-default"));
                int cooldown = ParkourRegionPlugin.getInstance().getConfig().getInt("cooldown-default");
                Region region = new Region(name, min, max, blacklist, cooldown);
                regionManager.addRegion(region);
                player.sendMessage("§aRegion " + name + " created!");
            }
            case "delete" -> {
                if (args.length < 2) {
                    player.sendMessage("§cUsage: /por delete <name>");
                    return true;
                }
                String name = args[1];
                regionManager.removeRegion(name);
                player.sendMessage("§aRegion " + name + " deleted!");
            }
            default -> player.sendMessage("§cUnknown subcommand!");
        }
        return true;
    }
}
