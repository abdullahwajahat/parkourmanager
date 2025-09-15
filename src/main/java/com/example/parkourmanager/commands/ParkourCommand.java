package com.example.parkourmanager;

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

    private final ParkourManagerPlugin plugin;

    public ParkourCommand(ParkourManagerPlugin plugin) {
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
            case "region" -> handleRegion(player, args);
            case "editcheckpoint" -> handleEditCheckpoint(player, args);
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
        MessageUtil.send(player, "pos1-set", Map.of(
                "x", String.valueOf(loc.getBlockX()),
                "y", String.valueOf(loc.getBlockY()),
                "z", String.valueOf(loc.getBlockZ())
        ));
    }

    private void handlePos2(Player player) {
        Location loc = player.getLocation();
        plugin.getSelectionManager().setPos2(player, loc);
        MessageUtil.send(player, "pos2-set", Map.of(
                "x", String.valueOf(loc.getBlockX()),
                "y", String.valueOf(loc.getBlockY()),
                "z", String.valueOf(loc.getBlockZ())
        ));
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
    }

    private void handleSetStart(Player player, String[] args) {
        plugin.getRegionManager().setStart(player, player.getLocation());
        MessageUtil.send(player, "start-set");
    }

    private void handleSetFinish(Player player, String[] args) {
        plugin.getRegionManager().setFinish(player, player.getLocation());
        MessageUtil.send(player, "finish-set");
    }

    private void handleAddCheckpoint(Player player, String[] args) {
        if (args.length < 2) {
            MessageUtil.send(player, "checkpoint-usage");
            return;
        }
        int number = Integer.parseInt(args[1]);
        plugin.getRegionManager().addCheckpoint(player, number, player.getLocation());
        MessageUtil.send(player, "checkpoint-added", Map.of("num", String.valueOf(number)));
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
                int num = Integer.parseInt(args[3]);
                plugin.getRegionManager().removeCheckpoint(regionName, num);
                MessageUtil.send(player, "checkpoint-removed", Map.of("num", String.valueOf(num)));
            }
            case "addfinishcommand" -> {
                if (args.length < 4) {
                    MessageUtil.send(player, "addfinishcommand-usage");
                    return;
                }
                String cmd = String.join(" ", Arrays.copyOfRange(args, 3, args.length));
                plugin.getRegionManager().addFinishCommand(regionName, cmd);
                MessageUtil.send(player, "finishcommand-added", Map.of("cmd", cmd));
            }
            case "removefinishcommand" -> {
                if (args.length < 4) {
                    MessageUtil.send(player, "removefinishcommand-usage");
                    return;
                }
                int index = Integer.parseInt(args[3]);
                plugin.getRegionManager().removeFinishCommand(regionName, index);
                MessageUtil.send(player, "finishcommand-removed", Map.of("index", String.valueOf(index)));
            }
            case "setfinishcommand" -> {
                if (args.length < 5) {
                    MessageUtil.send(player, "setfinishcommand-usage");
                    return;
                }
                int index = Integer.parseInt(args[3]);
                String cmd = String.join(" ", Arrays.copyOfRange(args, 4, args.length));
                plugin.getRegionManager().setFinishCommand(regionName, index, cmd);
                MessageUtil.send(player, "finishcommand-set", Map.of("index", String.valueOf(index), "cmd", cmd));
            }
            case "setfally" -> {
                if (args.length < 4) {
                    MessageUtil.send(player, "setfally-usage");
                    return;
                }
                int y = Integer.parseInt(args[3]);
                plugin.getRegionManager().setFallY(regionName, y);
                MessageUtil.send(player, "fally-set", Map.of("y", String.valueOf(y)));
            }
            case "cooldown" -> {
                if (args.length < 4) {
                    MessageUtil.send(player, "cooldown-usage");
                    return;
                }
                String time = args[3];
                plugin.getRegionManager().setCooldown(regionName, time);
                MessageUtil.send(player, "cooldown-set", Map.of("time", time));
            }
            case "addblacklist" -> {
                if (args.length < 4) {
                    MessageUtil.send(player, "addblacklist-usage");
                    return;
                }
                Material mat = Material.matchMaterial(args[3]);
                if (mat == null) {
                    MessageUtil.send(player, "invalid-block", Map.of("block", args[3]));
                    return;
                }
                plugin.getRegionManager().addBlacklist(regionName, mat);
                MessageUtil.send(player, "blacklist-added", Map.of("block", mat.name()));
            }
            case "removeblacklist" -> {
                if (args.length < 4) {
                    MessageUtil.send(player, "removeblacklist-usage");
                    return;
                }
                Material mat = Material.matchMaterial(args[3]);
                if (mat == null) {
                    MessageUtil.send(player, "invalid-block", Map.of("block", args[3]));
                    return;
                }
                plugin.getRegionManager().removeBlacklist(regionName, mat);
                MessageUtil.send(player, "blacklist-removed", Map.of("block", mat.name()));
            }
            case "tp" -> {
                Location middle = plugin.getRegionManager().getRegionMiddle(regionName);
                player.teleport(middle);
                MessageUtil.send(player, "region-tp", Map.of("region", regionName));
            }
            default -> MessageUtil.send(player, "region-unknown-action", Map.of("action", action));
        }
    }

    private void handleEditCheckpoint(Player player, String[] args) {
        if (args.length < 2) {
            MessageUtil.send(player, "editcheckpoint-usage");
            return;
        }
        int num = Integer.parseInt(args[1]);
        plugin.getRegionManager().editCheckpoint(player, num, player.getLocation());
        MessageUtil.send(player, "checkpoint-edited", Map.of("num", String.valueOf(num)));
    }

    private void handleList(Player player) {
        List<String> regions = plugin.getRegionManager().listRegions();
        MessageUtil.send(player, "region-list", Map.of("list", String.join(", ", regions)));
    }

    private void handleInfo(Player player, String[] args) {
        String regionName;
        if (args.length >= 2) {
            regionName = args[1];
        } else {
            regionName = plugin.getRegionManager().getRegionAt(player.getLocation());
        }
        if (regionName == null) {
            MessageUtil.send(player, "no-region-info");
            return;
        }
        plugin.getRegionManager().showRegionOutline(regionName, player, Particle.VILLAGER_HAPPY);
        String info = plugin.getRegionManager().getRegionInfo(regionName);
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

    // -------- TAB COMPLETION --------
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        if (args.length == 1) {
            return Arrays.asList("pos1", "pos2", "create", "setstart", "setfinish", "addcheckpoint",
                    "region", "editcheckpoint", "list", "info", "delete");
        }
        if (args.length == 2 && args[0].equalsIgnoreCase("create")) {
            return Collections.singletonList("<region>");
        }
        if (args[0].equalsIgnoreCase("region")) {
            if (args.length == 2) {
                return plugin.getRegionManager().listRegions();
            }
            if (args.length == 3) {
                return Arrays.asList("removecheckpoint", "addfinishcommand", "removefinishcommand",
                        "setfinishcommand", "setfally", "cooldown", "addblacklist", "removeblacklist", "tp");
            }
        }
        return Collections.emptyList();
    }
}
