package com.example.parkourregion;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class ParkourTabCompleter implements TabCompleter {

    private final RegionManager regionManager;

    public ParkourTabCompleter(RegionManager regionManager) {
        this.regionManager = regionManager;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            completions.add("create");
            completions.add("delete");
        } else if (args.length == 2 && args[0].equalsIgnoreCase("delete")) {
            completions.addAll(regionManager.getRegions().keySet());
        }
        return completions;
    }
}
