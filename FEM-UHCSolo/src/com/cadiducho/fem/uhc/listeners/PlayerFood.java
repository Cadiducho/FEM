package com.cadiducho.fem.uhc.listeners;

import com.cadiducho.fem.uhc.Main;
import com.cadiducho.fem.uhc.manager.GameState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class PlayerFood implements Listener {

    public Main plugin;

    public PlayerFood(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onFoodLevel(FoodLevelChangeEvent e) {
        if (GameState.state == GameState.LOBBY || GameState.state == GameState.TELETRANSPORTE || GameState.state == GameState.FIN) {
            e.setCancelled(true);
        } else if (GameState.state == GameState.PVE || GameState.state == GameState.JUEGO || GameState.state == GameState.ELIMINACION) {
            e.setCancelled(false);
        }
    }

}
