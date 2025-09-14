package com.example.parkourregion.gui;

import com.example.parkourregion.objects.ParkourRegionObject;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Map;

public class CheckpointsGUI {

    private final Player player;
    private final Inventory inv;
    private final ParkourRegionObject region;

    public CheckpointsGUI(Player player, ParkourRegionObject region) {
        this.player = player;
        this.region = region;
        inv = Bukkit.createInventory(null, 54, "§eCheckpoints");
        setup();
    }

    private void setup() {
        inv.setItem(10, createItem(Material.GREEN_WOOL, "§aAdd Checkpoint"));
        inv.setItem(12, createItem(Material.PAPER, "§bEdit Checkpoint"));
        inv.setItem(14, createItem(Material.RED_WOOL, "§cRemove Checkpoint"));

        int slot = 19;
        for (Map.Entry<Integer, ?> cp : region.getCheckpoints().entrySet()) {
            ItemStack item = new ItemStack(Material.BOOK);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName("§bCheckpoint " + cp.getKey());
            item.setItemMeta(meta);
            inv.setItem(slot++, item);
            if (slot % 9 == 7) slot += 2;
        }

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

        if (mat == Material.ARROW) {
            new RegionOptionsGUI(player, region).open();
        } else if (mat == Material.GREEN_WOOL) {
            int num = 1;
            while (region.getCheckpoints().containsKey(num)) num++;
            region.setCheckpoint(num, player.getLocation());
            player.sendMessage("§aCheckpoint " + num + " set!");
            ParkourRegion.getInstance().getRegionManager().saveRegions();
            open();
        } else if (mat == Material.RED_WOOL) {
            player.sendMessage("§cTo remove a checkpoint, type the number in chat (not implemented in GUI yet).");
        } else if (mat == Material.PAPER) {
            player.sendMessage("§eTo edit a checkpoint, type the number in chat (not implemented in GUI yet).");
        }
    }
}

