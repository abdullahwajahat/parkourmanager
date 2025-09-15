package com.example.parkourmanager.commands;

import com.example.parkourmanager.ParkourManager;
import com.example.parkourmanager.data.Region;
import com.example.parkourmanager.data.RegionManager;
import com.example.parkourmanager.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class ParkourCommand implements CommandExecutor, TabCompleter {

    private final ParkourManager plugin;
    private final RegionManager regionManager;

    // Store temporary pos1/pos2 for each player
    private final Map<UUID, Location> pos1 = new HashMap<>();
    private final Map<UUID, Location> pos2 = new HashMap<>();

    public ParkourCommand(ParkourManager plugin) {
        this.plugin = plugin;
        this.regionManager = plugin.getRegionManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Players only!");
            return true;
        }
        if (!player.hasPermission("parkourmanager.use")) {
            player.sendMessage(MessageUtil.get("no-permission"));
            return true;
        }

        if (args.length == 0) {
            player.sendMessage("§e/pm help");
            return true;
        }

        String sub = args[0].toLowerCase();

        switch (sub) {
            case "pos1" -> {
                pos1.put(player.getUniqueId(), player.getLocation());
                player.sendMessage("§aPosition 1 set!");
            }
            case "pos2" -> {
                pos2.put(player.getUniqueId(), player.getLocation());
                player.sendMessage("§aPosition 2 set!");
            }
            case "create" -> {
                if (args.length < 2) {
                    player.sendMessage("§cUsage: /pm create <name>");
                    return true;
                }
                if (!pos1.containsKey(player.getUniqueId()) || !pos2.containsKey(player.getUniqueId())) {
                    player.sendMessage(MessageUtil.get("need-positions"));
                    return true;
                }
                String name = args[1].toLowerCase();
                if (regionManager.getRegion(name) != null) {
                    player.sendMessage(MessageUtil.get("region-exists").replace("%region%", name));
                    return true;
                }
                Region region = new Region(name);
                region.setPos1(pos1.get(player.getUniqueId()));
                region.setPos2(pos2.get(player.getUniqueId()));
                regionManager.addRegion(region);
                player.sendMessage(MessageUtil.get("region-created").replace("%region%", name));
            }
            case "setstart" -> {
                Region region = getRegionAt(player.getLocation());
                if (region == null) {
                    player.sendMessage("§cYou must be inside a region!");
                    return true;
                }
                Location loc = (args.length == 4) ?
                        new Location(player.getWorld(),
                                Double.parseDouble(args[1]),
                                Double.parseDouble(args[2]),
                                Double.parseDouble(args[3]))
                        : player.getLocation();
                region.setStart(loc);
                player.sendMessage(MessageUtil.get("set-start"));
            }
            case "setfinish" -> {
                Region region = getRegionAt(player.getLocation());
                if (region == null) {
                    player.sendMessage("§cYou must be inside a region!");
                    return true;
                }
                Location loc = (args.length == 4) ?
                        new Location(player.getWorld(),
                                Double.parseDouble(args[1]),
                                Double.parseDouble(args[2]),
                                Double.parseDouble(args[3]))
                        : player.getLocation();
                region.setFinish(loc);
                player.sendMessage(MessageUtil.get("set-finish"));
            }
            case "addcheckpoint" -> {
                if (args.length < 2) {
                    player.sendMessage("§cUsage: /pm addcheckpoint <number> [x y z]");
                    return true;
                }
                Region region = getRegionAt(player.getLocation());
                if (region == null) {
                    player.sendMessage("§cYou must be inside a region!");
                    return true;
                }
                int num = Integer.parseInt(args[1]);
                Location loc;
                if (args.length == 5) {
                    loc = new Location(player.getWorld(),
                            Double.parseDouble(args[2]),
                            Double.parseDouble(args[3]),
                            Double.parseDouble(args[4]));
                } else {
                    loc = player.getLocation();
                }
                region.getCheckpoints().put(num, loc);
                player.sendMessage(MessageUtil.get("checkpoint-added").replace("%number%", "" + num));
            }
            case "region" -> {
                if (args.length < 3) {
                    player.sendMessage("§cUsage: /pm region <name> <subcommand>");
                    return true;
                }
                String name = args[1].toLowerCase();
                Region region = regionManager.getRegion(name);
                if (region == null) {
                    player.sendMessage(MessageUtil.get("no-region"));
                    return true;
                }
                String sub2 = args[2].toLowerCase();
                switch (sub2) {
                    case "removecheckpoint" -> {
                        if (args.length < 4) {
                            player.sendMessage("§cUsage: /pm region <name> removecheckpoint <number>");
                            return true;
                        }
                        int num = Integer.parseInt(args[3]);
                        region.getCheckpoints().remove(num);
                        player.sendMessage(MessageUtil.get("checkpoint-removed").replace("%number%", "" + num));
                    }
                    case "addfinishcommand" -> {
                        if (args.length < 4) {
                            player.sendMessage("§cUsage: /pm region <name> addfinishcommand \"<command>\"");
                            return true;
                        }
                        String command = String.join(" ", Arrays.copyOfRange(args, 3, args.length));
                        command = command.replace("\"", "");
                        region.getFinishCommands().add(command);
                        player.sendMessage("§aAdded finish command: " + command);
                    }
                    case "removefinishcommand" -> {
                        if (args.length < 4) {
                            player.sendMessage("§cUsage: /pm region <name> removefinishcommand <number>");
                            return true;
                        }
                        int index = Integer.parseInt(args[3]) - 1;
                        if (index >= 0 && index < region.getFinishCommands().size()) {
                            String removed = region.getFinishCommands().remove(index);
                            player.sendMessage("§cRemoved command: " + removed);
                        } else {
                            player.sendMessage("§cInvalid number");
                        }
                    }
                    case "setfinishcommand" -> {
                        if (args.length < 5) {
                            player.sendMessage("§cUsage: /pm region <name> setfinishcommand <number> <command>");
                            return true;
                        }
                        int index = Integer.parseInt(args[3]) - 1;
                        String command = String.join(" ", Arrays.copyOfRange(args, 4, args.length));
                        command = command.replace("\"", "");
                        if (index >= 0 && index < region.getFinishCommands().size()) {
                            region.getFinishCommands().set(index, command);
                            player.sendMessage("§aSet command #" + (index + 1) + " to: " + command);
                        } else {
                            player.sendMessage("§cInvalid number");
                        }
                    }
                    case "setfally" -> {
                        if (args.length < 4) {
                            player.sendMessage("§cUsage: /pm region <name> setfallY <y>");
                            return true;
                        }
                        double y = Double.parseDouble(args[3]);
                        region.setFallY(y);
                        player.sendMessage(MessageUtil.get("fallY-set")
                                .replace("%region%", name)
                                .replace("%y%", "" + y));
                    }
                    case "cooldown" -> {
                        if (args.length < 4) {
                            player.sendMessage("§cUsage: /pm region <name> cooldown <time>");
                            return true;
                        }
                        region.setCooldown(args[3]);
                        player.sendMessage(MessageUtil.get("cooldown-set")
                                .replace("%region%", name)
                                .replace("%time%", args[3]));
                    }
                    case "addblacklist" -> {
                        if (args.length < 4) {
                            player.sendMessage("§cUsage: /pm region <name> addblacklist <block>");
                            return true;
                        }
                        Material mat = Material.matchMaterial(args[3]);
                        if (mat == null) {
                            player.sendMessage("§cInvalid block!");
                            return true;
                        }
                        region.getBlacklist().add(mat.name());
                        player.sendMessage(MessageUtil.get("blacklist-added").replace("%block%", mat.name()));
                    }
                    case "removeblacklist" -> {
                        if (args.length < 4) {
                            player.sendMessage("§cUsage: /pm region <name> removeblacklist <block>");
                            return true;
                        }
                        region.getBlacklist().remove(args[3].toUpperCase());
                        player.sendMessage(MessageUtil.get("blacklist-removed").replace("%block%", args[3].toUpperCase()));
                    }
                    case "tp" -> {
                        Location center = region.getCenter();
                        if (center != null) {
                            player.teleport(center);
                            player.sendMessage(MessageUtil.get("teleport").replace("%region%", name));
                        } else {
                            player.sendMessage("§cRegion has no positions!");
                        }
                    }
                    default -> player.sendMessage("§cUnknown subcommand!");
                }
            }
            case "editcheckpoint" -> {
                if (args.length < 2) {
                    player.sendMessage("§cUsage: /pm editcheckpoint <number> [coords]");
                    return true;
                }
                Region region = getRegionAt(player.getLocation());
                if (region == null) {
                    player.sendMessage("§cYou must be inside a region!");
                    return true;
                }
                int num = Integer.parseInt(args[1]);
                Location loc;
                if (args.length == 5) {
                    loc = new Location(player.getWorld(),
                            Double.parseDouble(args[2]),
                            Double.parseDouble(args[3]),
                            Double.parseDouble(args[4]));
                } else {
                    loc = player.getLocation();
                }
                region.getCheckpoints().put(num, loc);
                player.sendMessage("§aEdited checkpoint #" + num);
            }
            case "list" -> {
                player.sendMessage("§eRegions:");
                for (Region r : regionManager.getRegions().values()) {
                    player.sendMessage(" - " + r.getName());
                }
            }
            case "info" -> {
                Region region;
                if (args.length >= 2) {
                    region = regionManager.getRegion(args[1].toLowerCase());
                } else {
                    region = getRegionAt(player.getLocation());
                }
                if (region == null) {
                    player.sendMessage(MessageUtil.get("no-region"));
                    return true;
                }
                player.sendMessage(MessageUtil.get("info-title").replace("%region%", region.getName()));
                if (region.getPos1() != null && region.getPos2() != null) {
                    outlineRegion(player, region);
                }
            }
            case "delete" -> {
                if (args.length < 2) {
                    player.sendMessage("§cUsage: /pm delete <name>");
                    return true;
                }
                String name = args[1].toLowerCase();
                if (regionManager.getRegion(name) == null) {
                    player.sendMessage(MessageUtil.get("no-region"));
                    return true;
                }
                player.sendMessage(MessageUtil.get("confirmation").replace("%region%", name));
            }
            default -> player.sendMessage("§cUnknown command!");
        }

        return true;
    }

    private Region getRegionAt(Location loc) {
        for (Region r : regionManager.getRegions().values()) {
            if (r.isInside(loc)) return r;
        }
        return null;
    }

    private void outlineRegion(Player player, Region region) {
        Location p1 = region.getPos1();
        Location p2 = region.getPos2();
        if (p1 == null || p2 == null) return;

        double minX = Math.min(p1.getX(), p2.getX());
        double minY = Math.min(p1.getY(), p2.getY());
        double minZ = Math.min(p1.getZ(), p2.getZ());
        double maxX = Math.max(p1.getX(), p2.getX());
        double maxY = Math.max(p1.getY(), p2.getY());
        double maxZ = Math.max(p1.getZ(), p2.getZ());

        for (double x = minX; x <= maxX; x++) {
            for (double y = minY; y <= maxY; y++) {
                player.spawnParticle(Particle.VILLAGER_HAPPY, x, y, minZ, 1);
                player.spawnParticle(Particle.VILLAGER_HAPPY, x, y, maxZ, 1);
            }
        }
        for (double z = minZ; z <= maxZ; z++) {
            for (double y = minY; y <= maxY; y++) {
                player.spawnParticle(Particle.VILLAGER_HAPPY, minX, y, z, 1);
                player.spawnParticle(Particle.VILLAGER_HAPPY, maxX, y, z, 1);
            }
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        if (!(sender instanceof Player)) return Collections.emptyList();
        if (args.length == 1) {
            return Arrays.asList("pos1","pos2","create","setstart","setfinish","addcheckpoint","editcheckpoint",
                    "region","list","info","delete");
        }
        if (args.length >= 2 && args[0].equalsIgnoreCase("region")) {
            if (args.length == 2) {
                return new ArrayList<>(regionManager.getRegions().keySet());
            }
            if (args.length == 3) {
                return Arrays.asList("removecheckpoint","addfinishcommand","removefinishcommand",
                        "setfinishcommand","setfally","cooldown","addblacklist","removeblacklist","tp");
            }
        }
        return Collections.emptyList();
    }
}
