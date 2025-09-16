package com.example.parkourmanager.commands;

import com.example.parkourmanager.data.RegionManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TabCompletion implements TabCompleter {

    private final RegionManager regionManager;

    public TabCompletion(RegionManager regionManager) {
        this.regionManager = regionManager;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (!(sender instanceof Player)) {
            return completions;
        }

        if (args.length == 1) {
            completions.addAll(Arrays.asList(
                    "pos1", "pos2", "create", "setstart", "setfinish",
                    "addcheckpoint", "removecheckpoint", "editcheckpoint",
                    "region", "list", "info", "delete"
            ));
        } else if (args.length == 2 && args[0].equalsIgnoreCase("region")) {
            completions.addAll(regionManager.getRegionNames());
        } else if (args.length == 3 && args[0].equalsIgnoreCase("region")) {
            completions.addAll(Arrays.asList(
                    "removecheckpoint", "addfinishcommand", "removefinishcommand",
                    "setfinishcommand", "setfallY", "cooldown",
                    "addblacklist", "removeblacklist", "tp"
            ));
        }

        return completions;
    }
}
