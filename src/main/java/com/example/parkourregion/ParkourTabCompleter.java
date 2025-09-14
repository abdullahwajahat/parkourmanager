package com.example.parkourregion;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ParkourTabCompleter implements TabCompleter {

    private final ParkourRegion plugin;

    public ParkourTabCompleter(ParkourRegion plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        List<String> regions = plugin.getRegionManager().getAllRegionNames();

        if (args.length == 1) {
            completions.addAll(regions);
        } else if (args.length == 2) {
            completions.add("create");
            completions.add("delete");
            completions.add("setstart");
            completions.add("addcheckpoint");
            completions.add("removecheckpoint");
            completions.add("setfinish");
            completions.add("addfinishcommand");
            completions.add("removefinishcommand");
            completions.add("editfinishcommand");
        } else if (args.length == 3) {
            String sub = args[1].toLowerCase();
            Region region = plugin.getRegionManager().getRegion(args[0]);
            if (region == null) return completions;

            if (sub.equals("removecheckpoint")) {
                for (int i = 0; i < region.getCheckpoints().size(); i++) completions.add(String.valueOf(i));
            } else if (sub.equals("removefinishcommand") || sub.equals("editfinishcommand")) {
                for (int i = 0; i < region.getFinishCommands().size(); i++) completions.add(String.valueOf(i));
            }
        }

        return completions.stream().filter(s -> s.toLowerCase().startsWith(args[args.length - 1].toLowerCase())).collect(Collectors.toList());
    }
}
