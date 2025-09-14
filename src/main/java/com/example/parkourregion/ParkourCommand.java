// File: src/main/java/com/example/parkourregion/ParkourCommand.java
package com.example.parkourregion;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ParkourCommand implements CommandExecutor, TabCompleter {

    private final ParkourRegionPlugin plugin;

    public ParkourCommand(ParkourRegionPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("setstart")) {
            String regionName = args[1];
            Region region = plugin.getRegionManager().getRegion(regionName);
            if (region != null) {
                region.setStart(player.getLocation());
                player.sendMessage("§aStart location for region '" + regionName + "' set!");
            } else {
                player.sendMessage("§cRegion not found!");
            }
            return true;
        }

        sender.sendMessage("§cUsage: /por setstart <region>");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            if ("setstart".startsWith(args[0].toLowerCase())) completions.add("setstart");
        } else if (args.length == 2) {
            for (String regionName : plugin.getRegionManager().getRegions().keySet()) {
                if (regionName.toLowerCase().startsWith(args[1].toLowerCase()))
                    completions.add(regionName);
            }
        }
        return completions;
    }
}
