package com.cadiducho.fem.uhc.listeners;

import com.cadiducho.fem.uhc.Main;
import com.cadiducho.fem.uhc.manager.GameState;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

public class InventoryClick implements Listener {

    public Main plugin;

    public InventoryClick(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onInventoryClick(InventoryClickEvent e) {
        if (GameState.state == GameState.LOBBY) {
            plugin.iv.onPlayerClickSVMenu(e);
            plugin.ivip.openMenuVIP(e);
            plugin.ic.onClickCage(e);
            if (e.getSlotType() == InventoryType.SlotType.QUICKBAR || e.getSlotType() == InventoryType.SlotType.CONTAINER || e.getSlotType() == InventoryType.SlotType.ARMOR || e.getSlotType() == InventoryType.SlotType.RESULT) {
                e.setCancelled(true);
            }
        }

    }

}
