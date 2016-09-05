package com.cadiducho.fem.uhc.listeners;

import com.cadiducho.fem.uhc.Main;
import com.cadiducho.fem.uhc.manager.GameState;
import org.bukkit.event.EventHandler;

import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class PlayerPortal implements Listener {

    public Main plugin;

    public PlayerPortal(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerPortal(PlayerPortalEvent e) {
        if (e.getCause() == PlayerTeleportEvent.TeleportCause.END_PORTAL) {
            e.setCancelled(true);
        }

        if (e.getCause() == PlayerTeleportEvent.TeleportCause.NETHER_PORTAL) {
            if (GameState.state == GameState.ELIMINACION) {
                e.setCancelled(true);
                e.getPlayer().sendMessage("ELIMINACIÓN activada.");
            }

        }
    }

}
