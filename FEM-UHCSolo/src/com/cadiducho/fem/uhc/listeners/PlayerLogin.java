package com.cadiducho.fem.uhc.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

import com.cadiducho.fem.uhc.Main;
import com.cadiducho.fem.uhc.manager.GameState;

public class PlayerLogin implements Listener {

    public Main plugin;

    public PlayerLogin(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent e) {
        if(GameState.state == null){
            e.setKickMessage("El servidor no está disponible");
        } else if (GameState.state == GameState.LOBBY && plugin.gm.getPlayers().size() < 40) {
            e.allow();
        } else if (GameState.state == GameState.LOBBY && plugin.gm.getPlayers().size() < 43) {
            if (e.getPlayer().hasPermission("DonkeyUHC.FullJoin")) {
                e.allow();
            } else {
                e.disallow(Result.KICK_FULL, "Necesitas ser VIP");
            }
        } else if (GameState.state == GameState.TELETRANSPORTE || GameState.state == GameState.JUEGO || GameState.state == GameState.TELETRANSPORTE || GameState.state == GameState.FIN) {
            if (e.getPlayer().hasPermission("DonkeyUHC.Bypass") || e.getPlayer().isOp()) {
                //espectador
                e.allow();
            } else {
                e.disallow(Result.KICK_FULL, "No puedes entrar");
            }
        } else if(GameState.state == GameState.PVE && plugin.gm.DESCONECTADOS.containsKey(e.getPlayer().getUniqueId())){
            e.allow();
        } else {
            e.disallow(Result.KICK_FULL, "Nope");
        }
    }

}
