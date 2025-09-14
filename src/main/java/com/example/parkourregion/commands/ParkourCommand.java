package com.example.parkourregion.commands;

import com.example.parkourregion.ParkourRegion;
import com.example.parkourregion.managers.RegionManager;
import com.example.parkourregion.objects.ParkourRegionObject;
import com.example.parkourregion.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.stream.Collectors;

public class ParkourCommand implements CommandExecutor, TabCompleter {

    private final RegionManager regionManager = ParkourRegion.getInstance().getRegionManager();
    private final Map<UUID, Location[]> selectionMap = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage(MessageUtil.color("&cOnly players can run this command."));
            return true;
        }

        if (!p.hasPermission("parkourregion.admin")) {
            p.sendMessage(MessageUtil.get("no_permission"));
            return true;
        }

        if (args.length == 0) {
            p.sendMessage(MessageUtil.color("&aUsage: /por <create/setstart/setcheckpoint/setfinish/edit/lock/unlock/info/list>"));
            return true;
        }

        switch (args[0].toLowerCase()) {

            case "pos1":
                selectionMap.putIfAbsent(p.getUniqueId(), new Location[2]);
                selectionMap.get(p.getUniqueId())[0] = p.getLocation();
                p.sendMessage(MessageUtil.color("&aPosition 1 set!"));
                break;

            case "pos2":
                selectionMap.putIfAbsent(p.getUniqueId(), new Location[2]);
                selectionMap.get(p.getUniqueId())[1] = p.getLocation();
                p.sendMessage(MessageUtil.color("&aPosition 2 set!"));
                break;

            case "create":
                Location[] sel = selectionMap.get(p.getUniqueId());
                if (sel == null || sel[0] == null || sel[1] == null) {
                    p.sendMessage(MessageUtil.get("region_select_first"));
                    return true;
                }

                if (args.length == 1) {
                    p.sendMessage(MessageUtil.get("region_name_required"));
                    return true;
                }

                String name = args[1];
                if (regionManager.getRegion(name) != null) {
                    p.sendMessage(MessageUtil.color("&cRegion &b" + name + "&c already exists!"));
                    return true;
                }

                ParkourRegionObject region = new ParkourRegionObject(name);
                region.setPos1(sel[0]);
                region.setPos2(sel[1]);
                regionManager.addRegion(region);
                p.sendMessage(MessageUtil.get("region_created"));
                break;

            case "setstart":
                ParkourRegionObject regionStart = regionManager.getRegion(args.length > 1 ? args[1] : "");
                if (regionStart == null) {
                    p.sendMessage(MessageUtil.get("invalid_region").replace("{region}", args.length > 1 ? args[1] : ""));
                    return true;
                }
                regionStart.setStart(p.getLocation());
                regionManager.saveRegions();
                p.sendMessage(MessageUtil.get("start_set"));
                break;

            case "setcheckpoint":
                ParkourRegionObject regionCp = regionManager.getRegion(args.length > 1 ? args[1] : "");
                if (regionCp == null) {
                    p.sendMessage(MessageUtil.get("invalid_region").replace("{region}", args.length > 1 ? args[1] : ""));
                    return true;
                }
                int number = 1;
                if (args.length > 2) {
                    try { number = Integer.parseInt(args[2]); } catch (NumberFormatException ignored) {}
                } else {
                    while (regionCp.getCheckpoints().containsKey(number)) number++;
                }
                regionCp.setCheckpoint(number, p.getLocation());
                regionManager.saveRegions();
                p.sendMessage(MessageUtil.get("checkpoint_set").replace("{number}", String.valueOf(number)));
                break;

            case "setfinish":
                ParkourRegionObject regionFinish = regionManager.getRegion(args.length > 1 ? args[1] : "");
                if (regionFinish == null) {
                    p.sendMessage(MessageUtil.get("invalid_region").replace("{region}", args.length > 1 ? args[1] : ""));
                    return true;
                }
                regionFinish.setFinish(p.getLocation());
                regionManager.saveRegions();
                p.sendMessage(MessageUtil.get("finish_set"));
                break;

            case "lock":
                if (args.length < 2) { p.sendMessage("&cUsage: /por lock <region>"); return true; }
                ParkourRegionObject rLock = regionManager.getRegion(args[1]);
                if (rLock == null) { p.sendMessage(MessageUtil.get("invalid_region").replace("{region}", args[1])); return true; }
                rLock.setLocked(true);
                regionManager.saveRegions();
                p.sendMessage(MessageUtil.get("locked"));
                break;

            case "unlock":
                if (args.length < 2) { p.sendMessage("&cUsage: /por unlock <region>"); return true; }
                ParkourRegionObject rUnlock = regionManager.getRegion(args[1]);
                if (rUnlock == null) { p.sendMessage(MessageUtil.get("invalid_region").replace("{region}", args[1])); return true; }
                rUnlock.setLocked(false);
                regionManager.saveRegions();
                p.sendMessage(MessageUtil.get("unlocked"));
                break;

            case "list":
                Collection<ParkourRegionObject> all = regionManager.getRegions();
                if (all.isEmpty()) { p.sendMessage("&cNo regions found!"); return true; }
                p.sendMessage(MessageUtil.color("&aParkour Regions:"));
                all.forEach(r -> p.sendMessage("&b- " + r.getName()));
                break;

            default:
                p.sendMessage("&cUnknown subcommand.");
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return Arrays.asList("create","pos1","pos2","setstart","setcheckpoint","setfinish","lock","unlock","list")
                    .stream().filter(s -> s.startsWith(args[0].toLowerCase())).collect(Collectors.toList());
        } else if (args.length == 2) {
            // Suggest region names for second argument
            return regionManager.getRegions().stream().map(r -> r.getName())
                    .filter(r -> r.toLowerCase().startsWith(args[1].toLowerCase()))
                    .toList();
        }
        return Collections.emptyList();
    }
}

