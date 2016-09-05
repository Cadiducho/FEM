package com.cadiducho.fem.uhc.listeners;

import com.cadiducho.fem.uhc.Main;
import com.cadiducho.fem.uhc.manager.GameState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMove implements Listener {

    public Main plugin;

    public PlayerMove(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        // || GameState.state == GameState.LOBBY
        if (GameState.state == GameState.TELETRANSPORTE || GameState.state == GameState.LOBBY) {
            if (!(e.getFrom().getBlockZ() == e.getTo().getBlockZ()) || !(e.getFrom().getBlockX() == e.getTo().getBlockX())) {
                e.setTo(e.getFrom());
            }
        }
    }
}
