package com.example.parkourregion.gui;

import com.example.parkourregion.ParkourRegion;
import com.example.parkourregion.objects.ParkourRegionObject;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class RegionOptionsGUI {

    private final Player player;
    private final Inventory inv;
    private final ParkourRegionObject region;

    public RegionOptionsGUI(Player player, ParkourRegionObject region) {
        this.player = player;
        this.region = region;
        inv = Bukkit.createInventory(null, 54, "§aRegion: " + region.getName());
        setup();
    }

    private void setup() {
        inv.setItem(10, createItem(Material.EMERALD, "§aSet Start Point"));
        inv.setItem(12, createItem(Material.REDSTONE, "§cSet Finish Point"));
        inv.setItem(14, createItem(Material.BANNER, "§eSet Checkpoints"));
        inv.setItem(16, createItem(Material.PAPER, "§bEdit Finish Commands"));
        inv.setItem(28, createItem(Material.BARRIER, "§cEdit Blacklist Blocks"));
        inv.setItem(30, createItem(Material.LIGHT_BLUE_STAINED_GLASS_PANE, region.isLocked() ? "§cLocked" : "§aUnlock"));
        inv.setItem(49, createItem(Material.ARROW, "§7Back"));
    }

    private ItemStack createItem(Material mat, String name) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        return item;
    }

    public void open() {
        player.openInventory(inv);
    }

    public void handleClick(InventoryClickEvent e) {
        e.setCancelled(true);
        if (e.getCurrentItem() == null) return;
        Material mat = e.getCurrentItem().getType();

        switch (mat) {
            case EMERALD -> {
                region.setStart(player.getLocation());
                ParkourRegion.getInstance().getRegionManager().saveRegions();
                player.sendMessage("§aStart point set!");
            }
            case REDSTONE -> {
                region.setFinish(player.getLocation());
                ParkourRegion.getInstance().getRegionManager().saveRegions();
                player.sendMessage("§aFinish point set!");
            }
            case BANNER -> new CheckpointsGUI(player, region).open();
            case PAPER -> new FinishCommandGUI(player, region).open();
            case BARRIER -> new BlacklistGUI(player, region).open();
            case LIGHT_BLUE_STAINED_GLASS_PANE -> {
                region.setLocked(!region.isLocked());
                ParkourRegion.getInstance().getRegionManager().saveRegions();
                player.sendMessage(region.isLocked() ? "§cRegion locked!" : "§aRegion unlocked!");
                open();
            }
            case ARROW -> new MainMenuGUI(player).open();
        }
    }
}

