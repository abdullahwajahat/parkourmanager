package com.example.parkourmanager.commands;

import com.example.parkourmanager.data.Region;
import com.example.parkourmanager.data.RegionManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
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
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cOnly players can use this command!");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            player.sendMessage("§eUsage: /parkour <create|delete|list|setcheckpoint|addfinishcommand|clearfinishcommands|setfally|addblacklist|clearblacklist>");
            return true;
        }

        String sub = args[0].toLowerCase();

        switch (sub) {
            case "create":
                if (args.length < 2) {
                    player.sendMessage("§cUsage: /parkour create <name>");
                    return true;
                }
                String name = args[1];
                Location pos1 = player.getLocation();
                Location pos2 = player.getLocation();
                regionManager.createRegion(name, pos1, pos2);
                player.sendMessage("§aRegion created: " + name);
                break;

            case "delete":
                if (args.length < 2) {
                    player.sendMessage("§cUsage: /parkour delete <name>");
                    return true;
                }
                regionManager.removeRegion(args[1]);
                player.sendMessage("§aRegion deleted: " + args[1]);
                break;

            case "list":
                player.sendMessage("§eRegions:");
                for (Region r : regionManager.getRegions()) {
                    player.sendMessage(" - " + r.getName());
                }
                break;

            case "setcheckpoint":
                if (args.length < 3) {
                    player.sendMessage("§cUsage: /parkour setcheckpoint <region> <index>");
                    return true;
                }
                try {
                    int index = Integer.parseInt(args[2]);
                    regionManager.setCheckpoint(args[1], index, player.getLocation());
                    player.sendMessage("§aCheckpoint " + index + " set in region " + args[1]);
                } catch (NumberFormatException e) {
                    player.sendMessage("§cInvalid checkpoint index!");
                }
                break;

            case "addfinishcommand":
                if (args.length < 3) {
                    player.sendMessage("§cUsage: /parkour addfinishcommand <region> <command>");
                    return true;
                }
                String cmd = String.join(" ", java.util.Arrays.copyOfRange(args, 2, args.length));
                regionManager.addFinishCommand(args[1], cmd);
                player.sendMessage("§aFinish command added to " + args[1] + ": /" + cmd);
                break;

            case "clearfinishcommands":
                if (args.length < 2) {
                    player.sendMessage("§cUsage: /parkour clearfinishcommands <region>");
                    return true;
                }
                regionManager.clearFinishCommands(args[1]);
                player.sendMessage("§aFinish commands cleared for " + args[1]);
                break;

            case "setfally":
                if (args.length < 3) {
                    player.sendMessage("§cUsage: /parkour setfally <region> <y>");
                    return true;
                }
                try {
                    int y = Integer.parseInt(args[2]);
                    regionManager.setFallY(args[1], y);
                    player.sendMessage("§aFall Y set for region " + args[1] + ": " + y);
                } catch (NumberFormatException e) {
                    player.sendMessage("§cInvalid number!");
                }
                break;

            case "addblacklist":
                if (args.length < 3) {
                    player.sendMessage("§cUsage: /parkour addblacklist <region> <block>");
                    return true;
                }
                regionManager.addBlacklistBlock(args[1], args[2]);
                player.sendMessage("§aAdded " + args[2] + " to blacklist for " + args[1]);
                break;

            case "clearblacklist":
                if (args.length < 2) {
                    player.sendMessage("§cUsage: /parkour clearblacklist <region>");
                    return true;
                }
                regionManager.clearBlacklistBlocks(args[1]);
                player.sendMessage("§aBlacklist cleared for " + args[1]);
                break;

            case "showoutline":
                if (args.length < 2) {
                    player.sendMessage("§cUsage: /parkour showoutline <region>");
                    return true;
                }
                Region region = regionManager.getRegion(args[1]);
                if (region == null) {
                    player.sendMessage("§cRegion not found: " + args[1]);
                    return true;
                }
                region.spawnOutline(Particle.FLAME, player);
                player.sendMessage("§aOutline spawned for region " + args[1]);
                break;

            default:
                player.sendMessage("§cUnknown subcommand!");
                break;
        }

        return true;
    }
}
