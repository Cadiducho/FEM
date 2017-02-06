package com.cadiducho.fem.lucky.listeners;

import com.cadiducho.fem.lucky.LuckyWarriors;
import com.cadiducho.fem.lucky.manager.GameState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.server.ServerListPingEvent;

public class ServerListener implements Listener {

    private final LuckyWarriors plugin;

    public ServerListener(LuckyWarriors instance) {
        plugin = instance;
    }
    
    @EventHandler
    public void onMotd(ServerListPingEvent e) {
        e.setMotd(GameState.getParsedStatus() + "#" + plugin.getWorld().getName());
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
        if (GameState.state == GameState.LUCKY || GameState.state == GameState.CRAFT || GameState.state == GameState.PREPARING || GameState.state == GameState.LOBBY) {
            e.setCancelled(true);
        }
    }
    
}
