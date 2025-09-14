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

import java.util.List;

public class MainMenuGUI {

    private final Player player;
    private final Inventory inv;

    public MainMenuGUI(Player player) {
        this.player = player;
        inv = Bukkit.createInventory(null, 54, "§aParkour Regions");
        setup();
    }

    private void setup() {
        // Add items dynamically
        int slot = 10;
        for (ParkourRegionObject region : ParkourRegion.getInstance().getRegionManager().getRegions()) {
            ItemStack item = new ItemStack(Material.PAPER);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName("§b" + region.getName());
            item.setItemMeta(meta);
            inv.setItem(slot++, item);
            if (slot == 17) slot += 2; // spacing
        }
    }

    public void open() {
        player.openInventory(inv);
    }

    public void handleClick(InventoryClickEvent e) {
        e.setCancelled(true);
        if (e.getCurrentItem() == null) return;
        // Handle clicks: open region options or delete etc.
    }
}
