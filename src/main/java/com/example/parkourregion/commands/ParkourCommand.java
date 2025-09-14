package com.example.parkourregion.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.Location;
import com.example.parkourregion.managers.RegionManager;
import com.example.parkourregion.managers.Region;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ParkourCommand implements CommandExecutor, TabCompleter {

    private final RegionManager regionManager;

    public ParkourCommand(RegionManager manager) {
        this.regionManager = manager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) return true;

        if (args.length == 0) {
            sendUsage(player);
            return true;
        }

        String sub = args[0].toLowerCase();

        switch (sub) {
            case "create":
                if (args.length < 2) { sendUsage(player); return true; }
                createRegion(player, args[1]);
                break;

            case "delete":
                if (args.length < 2) { sendUsage(player); return true; }
                deleteRegion(player, args[1]);
                break;

            case "setstart":
                if (args.length < 2) { sendUsage(player); return true; }
                setStart(player, args[1]);
                break;

            case "setend":
                if (args.length < 2) { sendUsage(player); return true; }
                setEnd(player, args[1]);
                break;

            case "removestart":
                if (args.length < 2) { sendUsage(player); return true; }
                removeStart(player, args[1]);
                break;

            case "removeend":
                if (args.length < 2) { sendUsage(player); return true; }
                removeEnd(player, args[1]);
                break;

            case "addcheckpoint":
                if (args.length < 3) { sendUsage(player); return true; }
                addCheckpoint(player, args[1], args[2]);
                break;

            case "removecheckpoint":
                if (args.length < 3) { sendUsage(player); return true; }
                removeCheckpoint(player, args[1], args[2]);
                break;

            case "list":
                listRegions(player);
                break;

            default:
                sendUsage(player);
        }
        return true;
    }

    private void sendUsage(Player player) {
        player.sendMessage("§aUsage: /por <create|delete|setstart|setend|removestart|removeend|addcheckpoint|removecheckpoint|list>");
    }

    private void createRegion(Player player, String name) {
        if (regionManager.getRegion(name) != null) {
            player.sendMessage("§cRegion " + name + " already exists!");
            return;
        }
        Region region = new Region(name);
        regionManager.addRegion(region);
        regionManager.saveRegions();
        player.sendMessage("§aRegion " + name + " created!");
    }

    private void deleteRegion(Player player, String name) {
        if (regionManager.getRegion(name) == null) {
            player.sendMessage("§cRegion " + name + " does not exist!");
            return;
        }
        regionManager.getRegions().remove(name);
        regionManager.saveRegions();
        player.sendMessage("§aRegion " + name + " deleted!");
    }

    private void setStart(Player player, String name) {
        Region region = regionManager.getRegion(name);
        if (region == null) { player.sendMessage("§cRegion not found!"); return; }
        region.setStart(player.getLocation());
        regionManager.saveRegions();
        player.sendMessage("§aStart point set for region " + name);
    }

    private void setEnd(Player player, String name) {
        Region region = regionManager.getRegion(name);
        if (region == null) { player.sendMessage("§cRegion not found!"); return; }
        region.setEnd(player.getLocation());
        regionManager.saveRegions();
        player.sendMessage("§aEnd point set for region " + name);
    }

    private void removeStart(Player player, String name) {
        Region region = regionManager.getRegion(name);
        if (region == null || region.getStart() == null) { player.sendMessage("§cStart point not set!"); return; }
        region.setStart(null);
        regionManager.saveRegions();
        player.sendMessage("§aStart point removed for region " + name);
    }

    private void removeEnd(Player player, String name) {
        Region region = regionManager.getRegion(name);
        if (region == null || region.getEnd() == null) { player.sendMessage("§cEnd point not set!"); return; }
        region.setEnd(null);
        regionManager.saveRegions();
        player.sendMessage("§aEnd point removed for region " + name);
    }

    private void addCheckpoint(Player player, String name, String numberStr) {
        Region region = regionManager.getRegion(name);
        if (region == null) { player.sendMessage("§cRegion not found!"); return; }
        int number;
        try { number = Integer.parseInt(numberStr); } catch (NumberFormatException e) { player.sendMessage("§cInvalid checkpoint number!"); return; }
        region.getCheckpoints().put(number, player.getLocation());
        regionManager.saveRegions();
        player.sendMessage("§aCheckpoint " + number + " added to region " + name);
    }

    private void removeCheckpoint(Player player, String name, String numberStr) {
        Region region = regionManager.getRegion(name);
        if (region == null) { player.sendMessage("§cRegion not found!"); return; }
        int number;
        try { number = Integer.parseInt(numberStr); } catch (NumberFormatException e) { player.sendMessage("§cInvalid checkpoint number!"); return; }
        if (region.getCheckpoints().remove(number) == null) {
            player.sendMessage("§cCheckpoint " + number + " does not exist!");
        } else {
            regionManager.saveRegions();
            player.sendMessage("§aCheckpoint " + number + " removed from region " + name);
        }
    }

    private void listRegions(Player player) {
        if (regionManager.getRegions().isEmpty()) {
            player.sendMessage("§cNo regions found!");
            return;
        }
        player.sendMessage("§aRegions:");
        for (String name : regionManager.getRegions().keySet()) {
            player.sendMessage(" §7- " + name);
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            List<String> subs = List.of("create","delete","setstart","setend","removestart","removeend","addcheckpoint","removecheckpoint","list");
            return filterList(subs, args[0]);
        } else if (args.length == 2) {
            // Suggest region names
            return filterList(new ArrayList<>(regionManager.getRegions().keySet()), args[1]);
        }
        return Collections.emptyList();
    }

    private List<String> filterList(List<String> list, String prefix) {
        List<String> result = new ArrayList<>();
        for (String s : list) {
            if (s.toLowerCase().startsWith(prefix.toLowerCase())) result.add(s);
        }
        return result;
    }
}
