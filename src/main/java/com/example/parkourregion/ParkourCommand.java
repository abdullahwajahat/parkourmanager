package com.example.parkourregion;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class ParkourCommand implements CommandExecutor {

    private final RegionManager regionManager;

    public ParkourCommand(RegionManager regionManager) {
        this.regionManager = regionManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command!");
            return true;
        }

        if (args.length == 0) {
            player.sendMessage("§e/por create <name>");
            player.sendMessage("§e/por setcorner1 <name>");
            player.sendMessage("§e/por setcorner2 <name>");
            return true;
        }

        String sub = args[0].toLowerCase();

        switch (sub) {
            case "create" -> {
                if (args.length < 2) {
                    player.sendMessage("§cUsage: /por create <name>");
                    return true;
                }
                String name = args[1];
                if (regionManager.getRegion(name) != null) {
                    player.sendMessage("§cRegion already exists!");
                    return true;
                }
                regionManager.createRegion(name);
                player.sendMessage("§aRegion §e" + name + " §acreated!");
            }

            case "setcorner1" -> {
                if (args.length < 2) {
                    player.sendMessage("§cUsage: /por setcorner1 <name>");
                    return true;
                }
                String name = args[1];
                Region r = regionManager.getRegion(name);
                if (r == null) {
                    player.sendMessage("§cRegion not found!");
                    return true;
                }
                r.setCorner1(player.getLocation());
                player.sendMessage("§aCorner 1 set for §e" + name);
            }

            case "setcorner2" -> {
                if (args.length < 2) {
                    player.sendMessage("§cUsage: /por setcorner2 <name>");
                    return true;
                }
                String name = args[1];
                Region r = regionManager.getRegion(name);
                if (r == null) {
                    player.sendMessage("§cRegion not found!");
                    return true;
                }
                r.setCorner2(player.getLocation());
                player.sendMessage("§aCorner 2 set for §e" + name);
            }

            default -> player.sendMessage("§cUnknown subcommand!");
        }
        return true;
    }
}
