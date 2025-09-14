package com.example.parkourregion.commands;

import com.example.parkourregion.ParkourRegion;
import com.example.parkourregion.gui.MainMenuGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ParkourGUICommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player p)) return true;
        if (!p.hasPermission("parkourregion.admin")) {
            p.sendMessage("§cNo permission.");
            return true;
        }

        new MainMenuGUI(p).open();
        return true;
    }
}

