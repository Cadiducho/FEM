package com.cadiducho.fem.royale.listeners;

import com.cadiducho.fem.royale.BattleRoyale;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class ServerListener implements Listener {

    private final BattleRoyale plugin;

    public ServerListener(BattleRoyale plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockIgnite(BlockIgniteEvent e){
        if (plugin.getGm().acceptPlayers() || plugin.getGm().isFinished()) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDrop(PlayerDropItemEvent e) {
        if (plugin.getGm().acceptPlayers() || plugin.getGm().isFinished()) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerPickupItem(PlayerPickupItemEvent e) {
        if (plugin.getGm().acceptPlayers() || plugin.getGm().isFinished()) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent e) {
        if (plugin.getGm().acceptPlayers() || plugin.getGm().isFinished()) {
            e.setCancelled(true);
        }
    }
}
