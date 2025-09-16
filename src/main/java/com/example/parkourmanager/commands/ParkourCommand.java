package com.example.parkourmanager.commands;

import com.example.parkourmanager.ParkourManager;
import com.example.parkourmanager.data.Region;
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

public class ParkourCommand implements CommandExecutor, TabCompleter {

    private final ParkourManager plugin;

    public ParkourCommand(ParkourManager plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            MessageUtil.send(sender, "player-only");
            return true;
        }

        if (args.length == 0) {
            MessageUtil.send(player, "invalid-usage", Map.of("usage", "/pm help"));
            return true;
        }

        String sub = args[0].toLowerCase();

        switch (sub) {
            case "pos1" -> handlePos1(player);
            case "pos2" -> handlePos2(player);
            case "create" -> handleCreate(player, args);
            case "setstart" -> handleSetStart(player, args);
            case "setfinish" -> handleSetFinish(player, args);
            case "addcheckpoint" -> handleAddCheckpoint(player, args);
            case "editcheckpoint" -> handleEditCheckpoint(player, args);
            case "region" -> handleRegion(player, args);
            case "list" -> handleList(player);
            case "info" -> handleInfo(player, args);
            case "delete" -> handleDelete(player, args);
            default -> MessageUtil.send(player, "unknown-command", Map.of("cmd", args[0]));
        }

        return true;
    }

    // -------- Commands Implementation --------

    private void handlePos1(Player player) {
        Location loc = player.getLocation();
        plugin.getSelectionManager().setPos1(player, loc);
        MessageUtil.send(player, "pos1-set", coords(loc));
    }

    private void handlePos2(Player player) {
        Location loc = player.getLocation();
        plugin.getSelectionManager().setPos2(player, loc);
        MessageUtil.send(player, "pos2-set", coords(loc));
    }

    private void handleCreate(Player player, String[] args) {
        if (args.length < 2) {
            MessageUtil.send(player, "create-usage");
            return;
        }
        String regionName = args[1];

        if (!plugin.getSelectionManager().hasSelection(player)) {
            MessageUtil.send(player, "need-selection");
            return;
        }

        plugin.getRegionManager().createRegion(player, regionName);
        MessageUtil.send(player, "region-created", Map.of("region", regionName));

        // Clear selection after creation
        plugin.getSelectionManager().clearSelection(player);
    }

    private void handleSetStart(Player player, String[] args) {
        Region region = getRegionForCommand(player, args);
        if (region == null) return;

        region.setStart(player.getLocation());
        MessageUtil.send(player, "start-set");
    }

    private void handleSetFinish(Player player, String[] args) {
        Region region = getRegionForCommand(player, args);
        if (region == null) return;

        region.setFinish(player.getLocation());
        MessageUtil.send(player, "finish-set");
    }

    private void handleAddCheckpoint(Player player, String[] args) {
        if (args.length < 3) {
            MessageUtil.send(player, "checkpoint-usage");
            return;
        }
        String regionName = args[1];
        int number = parseInt(args[2], player);
        if (number == -1) return;

        plugin.getRegionManager().addCheckpoint(regionName, number, player.getLocation());
        MessageUtil.send(player, "checkpoint-added", Map.of("num", String.valueOf(number)));
    }

    private void handleEditCheckpoint(Player player, String[] args) {
        if (args.length < 3) {
            MessageUtil.send(player, "editcheckpoint-usage");
            return;
        }
        String regionName = args[1];
        int number = parseInt(args[2], player);
        if (number == -1) return;

        plugin.getRegionManager().editCheckpoint(regionName, number, player.getLocation());
        MessageUtil.send(player, "checkpoint-edited", Map.of("num", String.valueOf(number)));
    }

    private void handleRegion(Player player, String[] args) {
        if (args.length < 3) {
            MessageUtil.send(player, "region-usage");
            return;
        }
        String regionName = args[1];
        String action = args[2].toLowerCase();

        switch (action) {
            case "removecheckpoint" -> {
                if (args.length < 4) {
                    MessageUtil.send(player, "checkpoint-remove-usage");
                    return;
                }
                int num = parseInt(args[3], player);
                if (num == -1) return;

                plugin.getRegionManager().removeCheckpoint(regionName, num);
                MessageUtil.send(player, "checkpoint-removed", Map.of("num", String.valueOf(num)));
            }
            case "tp" -> {
                Location middle = plugin.getRegionManager().getRegionMiddle(regionName);
                if (middle == null) {
                    MessageUtil.send(player, "no-region-info");
                    return;
                }
                player.teleport(middle);
                MessageUtil.send(player, "region-tp", Map.of("region", regionName));
            }
            default -> MessageUtil.send(player, "region-unknown-action", Map.of("action", action));
        }
    }

    private void handleList(Player player) {
        List<String> regions = plugin.getRegionManager().listRegions();
        if (regions.isEmpty()) {
            MessageUtil.send(player, "no-regions");
            return;
        }
        MessageUtil.send(player, "region-list", Map.of("list", String.join(", ", regions)));
    }

    private void handleInfo(Player player, String[] args) {
        Region region;
        if (args.length >= 2) {
            region = plugin.getRegionManager().getRegion(args[1]);
        } else {
            region = plugin.getRegionManager().getRegionAt(player.getLocation());
        }
        if (region == null) {
            MessageUtil.send(player, "no-region-info");
            return;
        }
        plugin.getRegionManager().showRegionOutline(region.getName(), player, Particle.HAPPY_VILLAGER);
        String info = plugin.getRegionManager().getRegionInfo(region.getName());
        MessageUtil.send(player, "region-info", Map.of("info", info));
    }

    private void handleDelete(Player player, String[] args) {
        if (args.length < 2) {
            MessageUtil.send(player, "delete-usage");
            return;
        }
        String regionName = args[1];
        plugin.getRegionManager().deleteRegion(regionName);
        MessageUtil.send(player, "region-deleted", Map.of("region", regionName));
    }

    // -------- Helpers --------

    private Region getRegionForCommand(Player player, String[] args) {
        if (args.length < 2) {
            MessageUtil.send(player, "region-needed");
            return null;
        }
        Region region = plugin.getRegionManager().getRegion(args[1]);
        if (region == null) {
            MessageUtil.send(player, "region-not-found", Map.of("region", args[1]));
        }
        return region;
    }

    private int parseInt(String input, Player player) {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            MessageUtil.send(player, "invalid-number", Map.of("input", input));
            return -1;
        }
    }

    private Map<String, String> coords(Location loc) {
        return Map.of(
                "x", String.valueOf(loc.getBlockX()),
                "y", String.valueOf(loc.getBlockY()),
                "z", String.valueOf(loc.getBlockZ())
        );
    }

    // -------- TAB COMPLETION --------
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        if (args.length == 1) {
            return Arrays.asList("pos1", "pos2", "create", "setstart", "setfinish",
                    "addcheckpoint", "editcheckpoint", "region", "list", "info", "delete");
        }
        if (args.length == 2 && args[0].equalsIgnoreCase("create")) {
            return Collections.singletonList("<region>");
        }
        if (args[0].equalsIgnoreCase("region")) {
            if (args.length == 2) {
                return plugin.getRegionManager().listRegions();
            }
            if (args.length == 3) {
                return Arrays.asList("removecheckpoint", "tp");
            }
        }
        return Collections.emptyList();
    }
}
