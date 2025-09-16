package com.example.parkourmanager.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TabCompletion implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            completions.addAll(Arrays.asList("pos1", "pos2", "create", "setstart", "setfinish",
                    "addcheckpoint", "removecheckpoint", "editcheckpoint",
                    "region", "list", "info", "delete"));
        } else if (args.length == 2 && args[0].equalsIgnoreCase("region")) {
            completions.addAll(Arrays.asList("addfinishcommand", "removefinishcommand", "setfinishcommand",
                    "setfallY", "cooldown", "addblacklist", "removeblacklist", "tp"));
        }

        return completions;
    }
}
