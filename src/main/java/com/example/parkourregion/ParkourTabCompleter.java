package com.example.parkourregion;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class ParkourTabCompleter implements TabCompleter {

    private final ParkourRegionPlugin plugin;

    public ParkourTabCompleter(ParkourRegionPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            completions.add("create");
        } else if (args.length == 2 && args[0].equalsIgnoreCase("delete")) {
            completions.addAll(plugin.getRegionManager().getRegions().keySet());
        }
        return completions;
    }
}
