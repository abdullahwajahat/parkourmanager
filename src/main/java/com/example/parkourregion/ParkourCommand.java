package com.example.parkourregion;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class ParkourCommand implements CommandExecutor {

    private final ParkourRegionPlugin plugin;

    public ParkourCommand(ParkourRegionPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) return false;

        if (args.length == 0) {
            player.sendMessage("§aUse /por create <name>");
            return true;
        }

        if (args[0].equalsIgnoreCase("create")) {
            if (args.length < 2) {
                player.sendMessage("§cPlease provide a name for the region.");
                return true;
            }
            String name = args[1];
            Location min = player.getLocation(); // Example: use player location as min
            Location max = player.getLocation().add(5, 5, 5); // Example: 5x5x5 region
            plugin.getRegionManager().addRegion(name, min, max, Arrays.asList(Material.LAVA, Material.CACTUS));
            player.sendMessage("§aRegion §e" + name + "§a created!");
            return true;
        }

        return true;
    }
}
