package com.example.parkourregion.gui;

import com.example.parkourregion.objects.ParkourRegionObject;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class FinishCommandGUI {

    private final Player player;
    private final Inventory inv;
    private final ParkourRegionObject region;
    private final List<String> commands;

    public FinishCommandGUI(Player player, ParkourRegionObject region) {
        this.player = player;
        this.region = region;
        this.commands = new ArrayList<>();
        inv = Bukkit.createInventory(null, 54, "§bFinish Commands");
        setup();
    }

    private void setup() {
        inv.setItem(10, createItem(Material.GREEN_WOOL, "§aAdd Command"));
        inv.setItem(12, createItem(Material.PAPER, "§bEdit Command"));
        inv.setItem(14, createItem(Material.RED_WOOL, "§cRemove Command"));
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
            case ARROW -> new RegionOptionsGUI(player, region).open();
            case GREEN_WOOL -> player.sendMessage("§aType command in chat to add (not fully implemented in GUI).");
            case RED_WOOL -> player.sendMessage("§cRemove command by number in chat (not implemented in GUI).");
            case PAPER -> player.sendMessage("§eEdit command by number in chat (not implemented in GUI).");
        }
    }
}

