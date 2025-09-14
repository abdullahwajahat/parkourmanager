package com.example.parkourregion.commands;

import com.example.parkourregion.ParkourRegion;
import com.example.parkourregion.managers.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class ParkourCommand implements CommandExecutor, TabCompleter {

    private final ParkourRegion plugin;

    public ParkourCommand(ParkourRegion plugin) {
        this.plugin = plugin;
    }

    private String color(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    private String getMessage(String key, Map<String, String> placeholders) {
        String msg = plugin.getConfig().getString("messages." + key);
        if (msg == null) return key;
        if (placeholders != null) {
            for (var entry : placeholders.entrySet()) {
                msg = msg.replace("{" + entry.getKey() + "}", entry.getValue());
            }
        }
        return color(msg);
    }

    private void send(Player player, String key, Map<String, String> placeholders) {
        player.sendMessage(getMessage(key, placeholders));
    }

    private void send(Player player, String key) { send(player, key, null); }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) return true;
        if (!player.hasPermission("parkourregion.admin")) { send(player, "no_permission"); return true; }

        SelectionManager sel = plugin.getSelectionManager();
        RegionManager rm = plugin.getRegionManager();
        CooldownManager cd = plugin.getCooldownManager();

        try {
            if (args.length == 0) {
                send(player, "prefix" + " Usage: /por <subcommand>");
                return true;
            }

            String sub = args[0].toLowerCase();

            switch (sub) {
                case "pos1":
                    sel.setPos1(player.getUniqueId(), player.getLocation());
                    player.sendMessage(color("&aPosition 1 set."));
                    break;

                case "pos2":
                    sel.setPos2(player.getUniqueId(), player.getLocation());
                    player.sendMessage(color("&aPosition 2 set."));
                    break;

                case "create":
                    if (!sel.isSelectionComplete(player.getUniqueId())) {
                        send(player, "select_pos_first");
                        return true;
                    }
                    if (args.length < 2) {
                        send(player, "provide_region_name");
                        return true;
                    }
                    String regionName = args[1];
                    if (rm.getRegion(regionName) != null) {
                        player.sendMessage(color("&cRegion already exists!"));
                        return true;
                    }
                    Region region = new Region(regionName);
                    region.setPos1(sel.getPos1(player.getUniqueId()));
                    region.setPos2(sel.getPos2(player.getUniqueId()));
                    rm.addRegion(region);
                    sel.clearSelection(player.getUniqueId());
                    send(player, "region_created", Map.of("region", regionName));
                    break;

                case "setstart":
                    if (args.length < 2) { send(player, "Usage: /por setstart <region>"); return true; }
                    regionName = args[1];
                    region = rm.getRegion(regionName);
                    if (region == null) { send(player, "invalid_region", Map.of("region", regionName)); return true; }
                    region.setStart(player.getLocation());
                    rm.saveRegions();
                    send(player, "start_set", Map.of("region", regionName));
                    break;

                case "addcheckpoint":
                    if (args.length < 3) { send(player, "Usage: /por addcheckpoint <region> <number>"); return true; }
                    regionName = args[1];
                    int cpNumber = Integer.parseInt(args[2]);
                    region = rm.getRegion(regionName);
                    if (region == null) { send(player, "invalid_region", Map.of("region", regionName)); return true; }
                    region.getCheckpoints().put(cpNumber, player.getLocation());
                    rm.saveRegions();
                    send(player, "checkpoint_set", Map.of("region", regionName, "number", String.valueOf(cpNumber)));
                    break;

                case "setfinish":
                    if (args.length < 5) { send(player, "Usage: /por setfinish <region> <x> <y> <z>"); return true; }
                    regionName = args[1];
                    double x = Double.parseDouble(args[2]);
                    double y = Double.parseDouble(args[3]);
                    double z = Double.parseDouble(args[4]);
                    region = rm.getRegion(regionName);
                    if (region == null) { send(player, "invalid_region", Map.of("region", regionName)); return true; }
                    region.setFinishTeleport(new Location(player.getWorld(), x, y, z));
                    rm.saveRegions();
                    send(player, "finish_tp_set", Map.of("region", regionName, "x", String.valueOf(x), "y", String.valueOf(y), "z", String.valueOf(z)));
                    break;

                case "addblacklist":
                    if (args.length < 3) { send(player, "Usage: /por addblacklist <region> <material>"); return true; }
                    regionName = args[1];
                    region = rm.getRegion(regionName);
                    Material mat = Material.getMaterial(args[2].toUpperCase());
                    if (region == null) { send(player, "invalid_region", Map.of("region", regionName)); return true; }
                    if (mat == null) { player.sendMessage(color("&cInvalid material!")); return true; }
                    region.getBlacklist().add(mat);
                    rm.saveRegions();
                    player.sendMessage(color("&aAdded &f" + mat + " &ato blacklist of " + regionName));
                    break;

                case "removeblacklist":
                    if (args.length < 3) { send(player, "Usage: /por removeblacklist <region> <material>"); return true; }
                    regionName = args[1];
                    region = rm.getRegion(regionName);
                    mat = Material.getMaterial(args[2].toUpperCase());
                    if (region == null) { send(player, "invalid_region", Map.of("region", regionName)); return true; }
                    if (mat == null || !region.getBlacklist().contains(mat)) { player.sendMessage(color("&cMaterial not in blacklist!")); return true; }
                    region.getBlacklist().remove(mat);
                    rm.saveRegions();
                    player.sendMessage(color("&aRemoved &f" + mat + " &afrom blacklist of " + regionName));
                    break;

                case "list":
                    if (rm.getRegions().isEmpty()) { player.sendMessage(color("&cNo regions found!")); return true; }
                    player.sendMessage(color("&aRegions:"));
                    for (String rn : rm.getRegions().keySet()) player.sendMessage(" &7- " + rn);
                    break;

                case "tp":
                    if (args.length < 2) { send(player, "Usage: /por tp <region>"); return true; }
                    regionName = args[1];
                    region = rm.getRegion(regionName);
                    if (region == null) { send(player, "invalid_region", Map.of("region", regionName)); return true; }
                    if (region.getStart() != null) { player.teleport(region.getStart()); send(player, "teleported", Map.of("location","start")); }
                    break;

                case "info":
                    if (args.length < 2) { send(player, "Usage: /por info <region>"); return true; }
                    regionName = args[1];
                    region = rm.getRegion(regionName);
                    if (region == null) { send(player, "invalid_region", Map.of("region", regionName)); return true; }
                    player.sendMessage(color("&aRegion Info: &f" + regionName));
                    player.sendMessage(color("&7Start: &f" + (region.getStart() != null ? formatLoc(region.getStart()) : "None")));
                    player.sendMessage(color("&7Finish TP: &f" + (region.getFinishTeleport() != null ? formatLoc(region.getFinishTeleport()) : "None")));
                    player.sendMessage(color("&7Checkpoints: &f" + region.getCheckpoints().keySet()));
                    player.sendMessage(color("&7Blacklist: &f" + region.getBlacklist()));
                    break;

                default:
                    send(player, "prefix" + " Usage: /por <subcommand>");
            }

        } catch (Exception e) { player.sendMessage(color("&cAn error occurred: " + e.getMessage())); e.printStackTrace(); }
        return true;
    }

    private String formatLoc(Location loc) {
        return loc.getWorld().getName() + " (" + loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ() + ")";
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!(sender instanceof Player)) return Collections.emptyList();
        RegionManager rm = plugin.getRegionManager();

        if (args.length == 1) {
            return filter(List.of("pos1","pos2","create","setstart","addcheckpoint","setfinish","addblacklist","removeblacklist","list","tp","info"), args[0]);
        } else if (args.length == 2) {
            if (List.of("setstart","addcheckpoint","setfinish","addblacklist","removeblacklist","tp","info").contains(args[0].toLowerCase()))
                return filter(new ArrayList<>(rm.getRegions().keySet()), args[1]);
        } else if (args.length == 3) {
            String sub = args[0].toLowerCase();
            if (sub.equals("addcheckpoint")) return Collections.emptyList();
            if (sub.equals("addblacklist") || sub.equals("removeblacklist"))
                return filter(Arrays.stream(Material.values()).map(Enum::name).toList(), args[2]);
        } else if (args.length == 4 && args[0].equalsIgnoreCase("setfinish")) {
            return Collections.emptyList();
        }
        return Collections.emptyList();
    }

    private List<String> filter(List<String> list, String prefix) {
        return list.stream().filter(s -> s.toLowerCase().startsWith(prefix.toLowerCase())).collect(Collectors.toList());
    }
}
