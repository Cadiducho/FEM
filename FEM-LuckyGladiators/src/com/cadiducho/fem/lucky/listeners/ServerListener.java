package com.cadiducho.fem.lucky.listeners;

import com.cadiducho.fem.lucky.LuckyGladiators;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class ServerListener implements Listener {

    private final LuckyGladiators plugin;

    public ServerListener(LuckyGladiators instance) {
        plugin = instance;
    }

    @EventHandler
    public void onPlayerDrop(PlayerDropItemEvent e) {
        if (!plugin.getGm().isInGame()) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerPickupItem(PlayerPickupItemEvent e) {
        if (!plugin.getGm().isInGame()) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent e) {
        if (!plugin.getGm().isInGame()) {
            e.setCancelled(true);
        }
    }
}
