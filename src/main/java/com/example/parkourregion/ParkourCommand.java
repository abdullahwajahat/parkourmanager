package com.example.parkourregion;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class ParkourCommand implements CommandExecutor {

    private final ParkourRegion plugin;

    public ParkourCommand(ParkourRegion plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!sender.hasPermission("parkour.admin")) {
            sender.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Usage: /por <region> <subcommand>");
            return true;
        }

        String regionName = args[0];
        String subcommand = args[1].toLowerCase();

        Region region = plugin.getRegionManager().getRegion(regionName);
        if (region == null && !subcommand.equals("create")) {
            sender.sendMessage(ChatColor.RED + "Region not found!");
            return true;
        }

        switch (subcommand) {
            case "create":
                if (plugin.getRegionManager().getRegion(regionName) != null) {
                    sender.sendMessage(ChatColor.RED + "Region already exists!");
                    return true;
                }
                plugin.getRegionManager().createRegion(regionName);
                sender.sendMessage(ChatColor.GREEN + "Region '" + regionName + "' created.");
                break;

            case "delete":
                plugin.getRegionManager().deleteRegion(regionName);
                sender.sendMessage(ChatColor.GREEN + "Region '" + regionName + "' deleted.");
                break;

            case "setstart":
                if (!(sender instanceof Player p)) return true;
                region.setStart(((Player) sender).getLocation());
                sender.sendMessage(ChatColor.GREEN + "Start point set!");
                break;

            case "addcheckpoint":
                if (!(sender instanceof Player p)) return true;
                if (args.length < 5) {
                    sender.sendMessage(ChatColor.RED + "Usage: /por <region> addcheckpoint <x> <y> <z>");
                    return true;
                }
                try {
                    int x = Integer.parseInt(args[2]);
                    int y = Integer.parseInt(args[3]);
                    int z = Integer.parseInt(args[4]);
                    region.addCheckpoint(new Location(((Player) sender).getWorld(), x, y, z));
                    sender.sendMessage(ChatColor.GREEN + "Checkpoint added!");
                } catch (NumberFormatException e) {
                    sender.sendMessage(ChatColor.RED + "Invalid coordinates!");
                }
                break;

            case "removecheckpoint":
                if (args.length < 3) {
                    sender.sendMessage(ChatColor.RED + "Usage: /por <region> removecheckpoint <index>");
                    return true;
                }
                try {
                    int index = Integer.parseInt(args[2]);
                    if (region.removeCheckpoint(index)) {
                        sender.sendMessage(ChatColor.GREEN + "Checkpoint removed!");
                    } else {
                        sender.sendMessage(ChatColor.RED + "Checkpoint not found!");
                    }
                } catch (NumberFormatException e) {
                    sender.sendMessage(ChatColor.RED + "Invalid index!");
                }
                break;

            case "setfinish":
                if (!(sender instanceof Player p)) return true;
                region.setFinish(((Player) sender).getLocation());
                sender.sendMessage(ChatColor.GREEN + "Finish point set!");
                break;

            case "addfinishcommand":
                if (args.length < 3) {
                    sender.sendMessage(ChatColor.RED + "Usage: /por <region> addfinishcommand <command>");
                    return true;
                }
                String finishCommand = String.join(" ", args).substring(args[0].length() + args[1].length() + 2);
                region.addFinishCommand(finishCommand);
                sender.sendMessage(ChatColor.GREEN + "Finish command added!");
                break;

            case "removefinishcommand":
                if (args.length < 3) {
                    sender.sendMessage(ChatColor.RED + "Usage: /por <region> removefinishcommand <index>");
                    return true;
                }
                try {
                    int idx = Integer.parseInt(args[2]);
                    if (region.removeFinishCommand(idx)) {
                        sender.sendMessage(ChatColor.GREEN + "Finish command removed!");
                    } else {
                        sender.sendMessage(ChatColor.RED + "Command not found!");
                    }
                } catch (NumberFormatException e) {
                    sender.sendMessage(ChatColor.RED + "Invalid index!");
                }
                break;

            case "editfinishcommand":
                if (args.length < 4) {
                    sender.sendMessage(ChatColor.RED + "Usage: /por <region> editfinishcommand <index> <command>");
                    return true;
                }
                try {
                    int idx = Integer.parseInt(args[2]);
                    String newCmd = String.join(" ", args).substring(args[0].length() + args[1].length() + args[2].length() + 3);
                    if (region.editFinishCommand(idx, newCmd)) {
                        sender.sendMessage(ChatColor.GREEN + "Finish command edited!");
                    } else {
                        sender.sendMessage(ChatColor.RED + "Command not found!");
                    }
                } catch (NumberFormatException e) {
                    sender.sendMessage(ChatColor.RED + "Invalid index!");
                }
                break;

            default:
                sender.sendMessage(ChatColor.RED + "Unknown subcommand!");
        }

        return true;
    }
}
