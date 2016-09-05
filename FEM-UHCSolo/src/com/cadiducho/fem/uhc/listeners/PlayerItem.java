package com.cadiducho.fem.uhc.listeners;

import com.cadiducho.fem.uhc.Main;
import com.cadiducho.fem.uhc.manager.GameState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class PlayerItem implements Listener {

    public Main plugin;

    public PlayerItem(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerItemPickup(PlayerPickupItemEvent e) {
        if (GameState.state == GameState.LOBBY || GameState.state == GameState.TELETRANSPORTE) {
            e.setCancelled(true);
        } else {
            e.setCancelled(false);
        }
    }

    @EventHandler
    public void PlayerDropItem(PlayerDropItemEvent e) {
        if (GameState.state == GameState.LOBBY || GameState.state == GameState.TELETRANSPORTE) {
            e.setCancelled(true);
        } else {
            e.setCancelled(false);
        }
    }

}
