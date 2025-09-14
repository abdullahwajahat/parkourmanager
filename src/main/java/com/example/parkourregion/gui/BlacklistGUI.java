package com.example.parkourregion.gui;

import com.example.parkourregion.objects.ParkourRegionObject;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class BlacklistGUI {

    private final Player player;
    private final Inventory inv;
    private final ParkourRegionObject region;

    public BlacklistGUI(Player player, ParkourRegionObject region) {
        this.player = player;
        this.region = region;
        inv = Bukkit.createInventory(null, 54, "§cBlacklist Blocks");
        setup();
    }

    private void setup() {
        int slot = 10;
        Material[] blocks = {Material.CHEST, Material.IRON_BARS, Material.OAK_SLAB, Material.STONE_SLAB};
        for (Material b : blocks) {
            ItemStack item = new ItemStack(b);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(region.getBlacklistBlocks().contains(b.name()) ? "§a" + b.name() : "§c" + b.name());
            item.setItemMeta(meta);
            inv.setItem(slot++, item);
            if (slot % 9 == 7) slot += 3; // spacing
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
            return;
        }

        if (region.getBlacklistBlocks().contains(mat.name())) {
            region.removeBlacklistBlock(mat.name());
            player.sendMessage("§cRemoved " + mat.name() + " from blacklist");
        } else {
            region.addBlacklistBlock(mat.name());
            player.sendMessage("§aAdded " + mat.name() + " to blacklist");
        }

        ParkourRegion.getInstance().getRegionManager().saveRegions();
        open();
    }
}

